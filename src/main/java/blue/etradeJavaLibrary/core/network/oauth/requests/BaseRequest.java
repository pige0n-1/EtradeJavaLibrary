
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.SignatureBuilder;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.URLBuilder;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;
import blue.etradeJavaLibrary.core.network.oauth.responses.BaseResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public abstract class BaseRequest 
        implements Serializable {
    
    // Instance data fields
    private OauthParameters oauthParameters;
    private BaseURL baseURL;
    private Key consumerSecret;
    private Key tokenSecret;
    private HttpMethod httpMethod;
    private HttpURLConnection connection;
    
    // Static data fields
    protected static final ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    protected BaseRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, HttpMethod httpMethod) throws OauthException {
        this.oauthParameters = new OauthParameters(consumerKey);
        this.baseURL = baseURL;
        this.consumerSecret = consumerSecret;
        this.httpMethod = httpMethod;
        this.tokenSecret = new Key();
    }
    
    protected BaseRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, HttpMethod httpMethod) throws OauthException {
        this(baseURL, consumerKey, consumerSecret, httpMethod);
        this.tokenSecret = tokenSecret;
        oauthParameters.setToken(token);
    }
    
    protected BaseRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, Key verifier, HttpMethod httpMethod) throws OauthException {
        this(baseURL, consumerKey, consumerSecret, token, tokenSecret, httpMethod);
        oauthParameters.addVerifier(verifier);
    }
    
    protected abstract BaseResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException;
    
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
        return getConnection(fullURL, null);
    }
    
    protected HttpURLConnection getConnection(URL fullURL, Parameters allParameters) throws OauthException, IOException {
        if (allParameters == null)
            allParameters = oauthParameters;
        else
            allParameters = Parameters.merge(oauthParameters, allParameters);
        
        String signature = SignatureBuilder.buildSignature(fullURL, allParameters, consumerSecret, tokenSecret, httpMethod);
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
