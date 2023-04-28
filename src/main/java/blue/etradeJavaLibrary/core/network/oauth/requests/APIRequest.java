
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;
import blue.etradeJavaLibrary.core.network.oauth.responses.APIResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIRequest extends BaseRequest {
    
    // Instance data fields
    private final PathParameters pathParameters;
    private final QueryParameters queryParameters;
    
    // Static data fields
    protected static final int MAX_ATTEMPTS = 3;
    
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, HttpMethod httpMethod) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, httpMethod);
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
    }
    
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, HttpMethod httpMethod) throws OauthException {
        this(baseURL, pathParameters, new QueryParameters(), consumerKey, consumerSecret, token, tokenSecret, httpMethod);
    }
    
    public APIRequest(BaseURL baseURL, QueryParameters queryParameters, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, HttpMethod httpMethod) throws OauthException {
        this(baseURL, new PathParameters(), queryParameters, consumerKey, consumerSecret, token, tokenSecret, httpMethod);
    }
    
    public APIRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, HttpMethod httpMethod) throws OauthException {
        this(baseURL, new PathParameters(), new QueryParameters(), consumerKey, consumerSecret, token, tokenSecret, httpMethod);
    }
    
    @Override
    public APIResponse sendAndGetResponse() throws IOException, OauthException {
        return sendAndGetResponse(1);
    }
    
    
    // Private helper methods
    
    
    private APIResponse sendAndGetResponse(int attemptNumber) throws IOException, OauthException {
        HttpURLConnection connection = null;
        
        try {
            URL fullURL = buildFullURL(pathParameters, queryParameters);

            logger.log("Full URL", fullURL.toString());

            Parameters allParameters = Parameters.merge(pathParameters, queryParameters);
            connection = getConnection(fullURL, allParameters);

            InputStream connectionResponse = connection.getInputStream();
            logger.log("Connection response", connection.getResponseMessage());

            return new APIResponse(connectionResponse);
        }
        
        catch (MalformedURLException ex) {
            logger.log("The provided URL was malformed.");
            throw new OauthException("the provided URL was malformed.");
        }
        
        catch (IOException ex) {
            logger.log("Connection to etrade unsuccessful");
            logger.log("Connection response", connection.getResponseCode() + "");
            
            if (attemptNumber < MAX_ATTEMPTS)
                return sendAndGetResponse(attemptNumber + 1);
            else
                throw new OauthException("Connection to etrade unsuccessful.");
        }
    }
}
