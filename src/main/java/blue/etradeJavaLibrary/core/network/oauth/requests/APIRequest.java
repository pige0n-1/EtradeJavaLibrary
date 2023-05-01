
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
public class APIRequest extends BaseRequest {
    
    // Instance data fields
    /**
     * Holds all parameters that can be embedded into the URL path
     */
    private final PathParameters pathParameters;
    
    /**
     * Holds all parameters that can be added to the query portion of 
     * the URL
     */
    private final QueryParameters queryParameters;
    
    /**
     * The full URL of the destination that is built
     */
    private URL fullURL;
    
    // Static data fields
    protected static final int MAX_ATTEMPTS = 5;
    
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
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
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
    /**
     * Sends the API request and returns an APIResponse object
     */
    public APIResponse sendAndGetResponse() throws IOException, OauthException {
        return sendAndGetResponse(1);
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("APIRequest: ");
        stringBuilder.append("Full URL: ").append(fullURL.toString()).append(" ");
        stringBuilder.append(super.toString());
        stringBuilder.append(", ").append(pathParameters.toString()).append(", ");
        stringBuilder.append(", ").append(queryParameters.toString());
        
        return stringBuilder.toString();
    }
    
    
    // Private helper methods
    
    
    private APIResponse sendAndGetResponse(int attemptNumber) throws IOException, OauthException {
        HttpURLConnection connection = null;
        
        try {
            fullURL = buildFullURL(pathParameters, queryParameters);

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
            
            if (attemptNumber < MAX_ATTEMPTS) return sendAndGetResponse(attemptNumber + 1);
            else throw new OauthException("Connection to etrade unsuccessful.", ex);
        }
    }
}
