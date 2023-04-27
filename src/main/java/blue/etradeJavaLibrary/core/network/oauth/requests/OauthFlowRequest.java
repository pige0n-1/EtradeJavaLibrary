
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.responses.OauthFlowResponse;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

public final class OauthFlowRequest extends BaseRequest {
    
    private static final int MAX_ATTEMPTS = 5;
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret) throws OauthException {   
        super(baseURL, consumerKey, consumerSecret, HttpMethod.GET);
    }
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, HttpMethod.GET);
    }
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, Key verifier) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, verifier, HttpMethod.GET);
    }
    
    @Override
    public OauthFlowResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException {
        return sendAndGetResponse(1);
    }
    
    
    // Private helper methods
    
    
    public OauthFlowResponse sendAndGetResponse(int attemptNumber) throws IOException, OauthException {
        HttpURLConnection connection = null;
        
        try {
            URL fullURL = buildFullURL();
            logger.log("Full URL", fullURL.toString());
            connection = getConnection(fullURL);
            
            InputStream connectionResponse = connection.getInputStream();
            logger.log("Connection response", connection.getResponseMessage());
            
            return new OauthFlowResponse(connectionResponse);
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
