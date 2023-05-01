
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.SignatureBuilder;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.URLBuilder;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.BaseResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * This class represents an HTTP request in the Oauth 1.0a model.
 * It is used to model requests in the authentication flow and a typical
 * API request. All instances of this class have the sendAndGetResponse()
 * method to send the request and parse the response into some object.
 * This class may not be useful for retreiving the verifier code in the model
 * if the service provider requires that the verifier is retrieved through a browser
 * window. In that case, the BrowserRequest method can be used instead.
 */
public abstract class BaseRequest 
        implements Serializable {
    
    // Instance data fields
    /**
     * The base url of the destination
     */
    private final BaseURL baseURL;
    
    /**
     * The keys that are required by the service provider to access the resources
     */
    private final OauthKeySet keys;
    
    /**
     * The type of HTTP request that is used i.e. get, post, put
     */
    private final HttpMethod httpMethod;
    
    /**
     * All of the parameters in the oauth 1.0a model that are needed in every request.
     * This includes the consumer key, timestamp, signature, etc. These are not needed
     * to be configured by the user of this class; rather, they are automatically generated
     * when the sendAndGetResponse method is called.
     */
    private final OauthParameters oauthParameters;
    
    /**
     * The underlying connnection object from the Java API that is used to actually send the request
     */
    private HttpURLConnection connection;
    
    // Static data fields
    protected static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    /**
     * Constructs a BaseRequest object from the essentials.
     * @param baseURL
     * @param keys
     * @param httpMethod
     */
    protected BaseRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) {
        this.baseURL = baseURL;
        this.keys = keys;
        this.httpMethod = httpMethod;
        
        if (keys.hasRetrievedAToken())
            oauthParameters = new OauthParameters(keys.consumerKey, keys.getToken());
        else
            oauthParameters = new OauthParameters(keys.consumerKey);
    }
    
    /**
     * Send the request to the service provider and returns the response in a BaseResponse object
     * @return
     * @throws MalformedURLException
     * @throws OauthException if anything goes wrong with the request
     * @throws IOException 
     */
    protected abstract BaseResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException;
    
    /**
     * If a verifier code has been obtained in the Oauth model, it can be added to
     * the OauthParameters object with this method
     * @param verifier 
     */
    protected void setVerifier(Key verifier) {
        oauthParameters.addVerifier(verifier);
    }
    
    /**
     * Returns the full URL object, with the query and path parameters added in
     * @param pathParameters the object that encapsulates all path parameters of the oauth model
     * @param queryParameters the object that encapsulates all query parameters of the oauth model
     * @return
     * @throws MalformedURLException 
     */
    protected URL buildFullURL(PathParameters pathParameters, QueryParameters queryParameters) throws MalformedURLException {
        return URLBuilder.buildURL(baseURL, pathParameters, queryParameters);
    }
    
    /**
     * Returns the full URL object, with the path parameters added in
     * @param pathParameters the object that encapsulates all path parameters of the oauth model
     * @return
     * @throws MalformedURLException 
     */
    protected URL buildFullURL(PathParameters pathParameters) throws MalformedURLException {
        return URLBuilder.buildURL(baseURL, pathParameters);
    }
    
    /**
     * Returns the full URL object, with the query parameters added in
     * @param queryParameters the object that encapsulates all query parameters of the oauth model
     * @return
     * @throws MalformedURLException 
     */
    protected URL buildFullURL(QueryParameters queryParameters) throws MalformedURLException {
        return URLBuilder.buildURL(baseURL, queryParameters);
    }
    
    /**
     * Returns the full URL object by converting the baseURL to a fullURL. This method is not
     * strictly needed, but it is added for convenience.
     * @return
     * @throws MalformedURLException 
     */
    protected URL buildFullURL() throws MalformedURLException {
        return new URL(baseURL.toString());
    }
    
    /**
     * Returns the underlying HttpURLConnection object that is formed. The signature
     * is automatically formed, so the connection can be sent after this point.
     * @param fullURL
     * @return
     * @throws IOException 
     */
    protected HttpURLConnection getConnection(URL fullURL) throws IOException {
        return getConnection(fullURL, new Parameters());
    }
    
    /**
     * Returns the underlying HttpURLConnection. This method also takes in a Parameters object
     * that contains all query parameters and path parameters, or any other parameters, that are used
     * in the signature generation process.
     * @param fullURL
     * @param allParameters
     * @return
     * @throws IOException 
     */
    protected HttpURLConnection getConnection(URL fullURL, Parameters allParameters) throws IOException {
        if (connection != null)
            return connection;
        
        oauthParameters.resetNonceAndTimestamp();
        
        allParameters = Parameters.merge(oauthParameters, allParameters);
        
        String signature = SignatureBuilder.buildSignature(fullURL, allParameters, keys.consumerSecret, keys.getTokenSecret(), httpMethod);
        oauthParameters.setSignature(signature);
        
        connection = (HttpURLConnection)fullURL.openConnection();
        setAuthorizationHeader();
        
        return connection;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Base URL: ").append(baseURL.toString());
        stringBuilder.append(", Http method: ").append(httpMethod.name());
        stringBuilder.append(", Oauth parameters: ").append(oauthParameters.toString());
        
        return stringBuilder.toString();
    }
    
    
    // Private helper methods
    
    
    private void setAuthorizationHeader() {
        StringBuilder authorizationString = new StringBuilder("OAuth ");
        
        Iterator<Parameters.Parameter> parametersIterator = oauthParameters.iterator();
        
        while (parametersIterator.hasNext()) {
            Parameters.Parameter currentParameter = parametersIterator.next();
            authorizationString.append(currentParameter.getKey());
            authorizationString.append("=\"");
            authorizationString.append(currentParameter.getValue());
            authorizationString.append("\"");
            
            if (parametersIterator.hasNext())
                authorizationString.append(",");
        }
        logger.log("Authorization", authorizationString.toString());

        connection.addRequestProperty("Authorization", authorizationString.toString());
    }
}
