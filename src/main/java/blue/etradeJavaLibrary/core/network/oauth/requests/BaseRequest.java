
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

public abstract class BaseRequest 
        implements Serializable {
    
    // Instance data fields
    private final BaseURL baseURL;
    private final OauthKeySet keys;
    private final HttpMethod httpMethod;
    private final OauthParameters oauthParameters;
    private HttpURLConnection connection;
    
    // Static data fields
    protected static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    protected BaseRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) throws OauthException {
        this.baseURL = baseURL;
        this.keys = keys;
        this.httpMethod = httpMethod;
        
        if (keys.hasRetrievedAToken())
            oauthParameters = new OauthParameters(keys.consumerKey, keys.getToken());
        else
            oauthParameters = new OauthParameters(keys.consumerKey);
    }
    
    protected abstract BaseResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException;
    
    protected void setVerifier(Key verifier) {
        oauthParameters.addVerifier(verifier);
    }
    
    protected URL buildFullURL(PathParameters pathParameters, QueryParameters queryParameters) throws OauthException, MalformedURLException {
        return URLBuilder.buildURL(baseURL, pathParameters, queryParameters);
    }
    
    protected URL buildFullURL(PathParameters pathParameters) throws OauthException, MalformedURLException {
        return URLBuilder.buildURL(baseURL, pathParameters);
    }
    
    protected URL buildFullURL(QueryParameters queryParameters) throws OauthException, MalformedURLException {
        return URLBuilder.buildURL(baseURL, queryParameters);
    }
    
    protected URL buildFullURL() throws MalformedURLException {
        return new URL(baseURL.toString());
    }
    
    protected HttpURLConnection getConnection(URL fullURL) throws OauthException, IOException {
        return getConnection(fullURL, new Parameters());
    }
    
    protected HttpURLConnection getConnection(URL fullURL, Parameters allParameters) throws OauthException, IOException {
        oauthParameters.resetNonceAndTimestamp();
        
        allParameters = Parameters.merge(oauthParameters, allParameters);
        
        String signature = SignatureBuilder.buildSignature(fullURL, allParameters, keys.consumerSecret, keys.getTokenSecret(), httpMethod);
        oauthParameters.setSignature(signature);
        
        connection = (HttpURLConnection)fullURL.openConnection();
        setAuthorizationHeader();
        
        return connection;
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
