
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.OauthFlowResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents an HTTP request in the Oauth authentication flow.
 * All it needs is a BaseURL object and an OauthKeySet to configure,
 * and it can be easily sent using the sendAndGetResponse() method.
 */
public final class OauthFlowRequest extends BaseRequest {

    // Instance data fields
    private Key verifier;
    
    // Static data fields
    public static final int MAX_ATTEMPTS = 5;
    public static final int MILLISECONDS_BETWEEN_RETRY = 50;
    
    /**
     * Constructs a standard OauthFlowRequest object.
     * @param baseURL
     * @param keys
     */
    public OauthFlowRequest(BaseURL baseURL, OauthKeySet keys) {
        super(baseURL, keys, HttpMethod.GET);
    }

    @Override
    /**
     * Sets the oauth_verifier parameter. The verifier is typically needed in the Oauth flow after the request token
     * is obtained. If using a custom OauthParameters object during the flow, be sure to set it first by calling
     * setOauthParameters(), and then call this method.
     * @param verifier
     */
    public void setVerifier(Key verifier) {
        super.setVerifier(verifier);
    }

    @Override
    /**
     * Send the OauthFlowRequest and returns an OauthFlowResponse object. The response object contains an
     * instance method "parse" than parses the response into a Parameters object, where the access token
     * or request token can be retrieved.
     */
    public OauthFlowResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException {
        return sendAndGetResponse(1);
    }
    
    
    // Private helper methods
    
    
    private OauthFlowResponse sendAndGetResponse(int attemptNumber) throws IOException, OauthException {
        HttpURLConnection connection = null;
        
        try {
            connection = getConnection();
            
            InputStream connectionResponse = connection.getInputStream();
            logger.log("Connection response", connection.getResponseMessage());
            
            return new OauthFlowResponse(connectionResponse);
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
