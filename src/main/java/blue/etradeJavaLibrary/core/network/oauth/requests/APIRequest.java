
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.APIResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An extension of BaseRequest that represents a general request in the
 * Oauth model to the service provider's API. When the request is sent,
 * an APIResponse object is returned. This is usually in XML format,
 * so it can be easily parsed from the APIResponse object.
 */
public abstract class APIRequest extends BaseRequest {
    
    // Instance data fields
    /**
     * The full URL of the destination that is built
     */
    private URL fullURL;
    
    // Static data fields
    public static final int MAX_ATTEMPTS = 20;
    public static final int MILLISECONDS_BETWEEN_RETRY = 10;
    
    /**
     * Constructs a complete APIRequest object
     * @param baseURL
     * @param pathParameters
     * @param queryParameters
     * @param keys
     * @param httpMethod
     */
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, OauthKeySet keys, HttpMethod httpMethod) {
        super(baseURL, keys, httpMethod);
        setPathParameters(pathParameters);
        setQueryParameters(queryParameters);
    }
    
    /**
     * Constructs an APIRequest without any query parameters
     * @param baseURL
     * @param pathParameters
     * @param keys
     * @param httpMethod
     */
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, OauthKeySet keys, HttpMethod httpMethod) {
        this(baseURL, pathParameters, new QueryParameters(), keys, httpMethod);
    }
    
    /**
     * Constructs an APIRequest without path parameters
     * @param baseURL
     * @param queryParameters
     * @param keys
     * @param httpMethod
     */
    public APIRequest(BaseURL baseURL, QueryParameters queryParameters, OauthKeySet keys, HttpMethod httpMethod) {
        this(baseURL, new PathParameters(), queryParameters, keys, httpMethod);
    }
    
    /**
     * Constructs an APIRequest without path or query parameters
     * @param baseURL
     * @param keys
     * @param httpMethod
     */
    public APIRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) {
        this(baseURL, new PathParameters(), new QueryParameters(), keys, httpMethod);
    }

    @Override
    public void setPathParameters(PathParameters pathParameters) {
        super.setPathParameters(pathParameters);
    }

    @Override
    public void setQueryParameters(QueryParameters queryParameters) {
        super.setQueryParameters(queryParameters);
    }

    @Override
    public void setRequestBody(BodyParameter requestBody) {
        super.setRequestBody(requestBody);
    }
    
    @Override
    /**
     * Sends the API request and returns an APIResponse object
     */
    public APIResponse sendAndGetResponse() throws IOException {
        return sendAndGetResponse(1);
    }
    
    
    // Private helper methods
    
    
    private APIResponse sendAndGetResponse(int attemptNumber) throws IOException {
        HttpURLConnection connection = null;
        
        try {
            connection = getConnection();

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
            
            if (attemptNumber < MAX_ATTEMPTS) {
                sleep(MILLISECONDS_BETWEEN_RETRY);

                return sendAndGetResponse(attemptNumber + 1);
            }
            else throw new OauthException("Connection to etrade unsuccessful.", ex);
        }
    }
}
