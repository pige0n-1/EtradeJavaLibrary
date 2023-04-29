
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.*;
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
    protected static final int MAX_ATTEMPTS = 10;
    
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, OauthKeySet keys, HttpMethod httpMethod) throws OauthException {
        super(baseURL, keys, httpMethod);
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
    }
    
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, OauthKeySet keys, HttpMethod httpMethod) throws OauthException {
        this(baseURL, pathParameters, new QueryParameters(), keys, httpMethod);
    }
    
    public APIRequest(BaseURL baseURL, QueryParameters queryParameters, OauthKeySet keys, HttpMethod httpMethod) throws OauthException {
        this(baseURL, new PathParameters(), queryParameters, keys, httpMethod);
    }
    
    public APIRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) throws OauthException {
        this(baseURL, new PathParameters(), new QueryParameters(), keys, httpMethod);
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
            throw new OauthException("the provided URL was malformed.", ex);
        }
        
        catch (IOException ex) {
            logger.log("Connection to etrade unsuccessful");
            logger.log("Connection response", connection.getResponseCode() + "");
            
            if (attemptNumber < MAX_ATTEMPTS)
                return sendAndGetResponse(attemptNumber + 1);
            else
                throw new OauthException("Connection to etrade unsuccessful.", ex);
        }
    }
}
