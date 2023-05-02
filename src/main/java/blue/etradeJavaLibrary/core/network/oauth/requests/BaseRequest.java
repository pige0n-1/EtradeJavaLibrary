
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
 * This class may not be useful for retrieving the verifier code in the model
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
     * All the parameters in the oauth 1.0a model that are needed in every request.
     * This includes the consumer key, timestamp, signature, etc. These are not needed
     * to be configured by the user of this class; rather, they are automatically generated
     * when the sendAndGetResponse method is called.
     */
    private OauthParameters oauthParameters;
    
    /**
     * The underlying connection object from the Java API that is used to actually send the request
     */
    private HttpURLConnection connection;

    /**
     * All parameters that are part of the URL path
     */
    private PathParameters pathParameters = new PathParameters();

    /**
     * All parameters that are part of the query portion of the URL
     */
    private QueryParameters queryParameters = new QueryParameters();

    /**
     * The body of the HTTP request in XML format
     */
    private BodyParameter requestBody = new BodyParameter();
    
    // Static data fields
    protected static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    /**
     * Constructs a BaseRequest object from the essentials with a default OauthParameters object
     * @param baseURL
     * @param keys
     * @param httpMethod
     */
    protected BaseRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) {
        this.baseURL = baseURL;
        this.keys = keys;
        this.httpMethod = httpMethod;
        
        setOauthParameters(new OauthParameters());
    }

    /**
     * An optional method to use a custom object of a subclass of OauthParameters. Since every service provider
     * typically requires slightly different Oauth parameters, this method can be used to specify those differences.
     * See the OauthParameters javadoc for details on Oauth parameters and which are added automatically.
     * @param oauthParameters
     */
    public void setOauthParameters(OauthParameters oauthParameters) {
        this.oauthParameters = oauthParameters;

        if (keys.hasRetrievedAToken()) oauthParameters.configure(keys.consumerKey, keys.getToken());
        else oauthParameters.configure(keys.consumerKey);
    }

    /**
     * Sets the path parameters of this request. The key names in the PathParameters object must match ever variable
     * name in the BaseURL object, or a RuntimeException will be thrown.
     * @param pathParameters
     */
    protected void setPathParameters(PathParameters pathParameters) {
        this.pathParameters = pathParameters;
    }

    /**
     * Sets the query parameters of this request
     * @param queryParameters
     */
    protected void setQueryParameters(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }

    /**
     * Sets the request body
     * @param requestBody
     */
    protected void setRequestBody(BodyParameter requestBody) {
        if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE)
            throw new InvalidParameterException("No request body is allowed here.");

        this.requestBody = requestBody;
    }
    
    /**
     * Send the request to the service provider and returns the response in a BaseResponse object
     * @return
     * @throws MalformedURLException
     * @throws OauthException if anything goes wrong with the request
     * @throws IOException 
     */
    protected abstract BaseResponse sendAndGetResponse() throws MalformedURLException, IOException;
    
    /**
     * If a verifier code has been obtained in the Oauth model, it can be added to
     * the OauthParameters object with this method. If supplying a custom OauthParameters object to build a BaseRequest,
     * be sure to set the OauthParameters object before calling this method.
     * @param verifier 
     */
    protected void setVerifier(Key verifier) {
        oauthParameters.addVerifier(verifier);
    }
    
    /**
     * Returns the underlying HttpURLConnection. This method also takes in a Parameters object
     * that contains all query parameters and path parameters, or any other parameters, that are used
     * in the signature generation process.
     * @return
     * @throws IOException 
     */
    protected HttpURLConnection getConnection() throws IOException {
        Parameters allParameters = getAllParameters();

        URL fullURL = URLBuilder.buildURL(baseURL, pathParameters, queryParameters);
        logger.log("Full URL", fullURL.toString());
        oauthParameters.resetNonceAndTimestamp();
        
        String signature = SignatureBuilder.buildSignature(fullURL, allParameters, keys.consumerSecret, keys.getTokenSecret(), httpMethod);
        oauthParameters.setSignature(signature);
        
        connection = (HttpURLConnection)fullURL.openConnection();
        connection.setRequestMethod(httpMethod.name());
        setAuthorizationHeader();

        if (!requestBody.isEmpty()) addRequestBody(connection);
        logger.log("connection details", connection.toString());

        return connection;
    }
    
    @Override //TODO: Add PathParameters and QueryParameters
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
            
            if (parametersIterator.hasNext()) authorizationString.append(",");
        }
        logger.log("Authorization", authorizationString.toString());

        connection.addRequestProperty("Authorization", authorizationString.toString());
    }

    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (Exception ex) {}
    }

    private Parameters getAllParameters() {
        return Parameters.merge(pathParameters, queryParameters, oauthParameters, requestBody);
    }

    private void addRequestBody(HttpURLConnection connection) {
        logger.log("Adding request body...", requestBody.getValue());

        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/xml");

        try {
            var outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getValue().getBytes());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
