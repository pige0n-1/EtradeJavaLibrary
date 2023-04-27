
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import blue.etradeJavaLibrary.core.network.oauth.requests.OauthFlowRequest;
import blue.etradeJavaLibrary.core.network.oauth.responses.OauthFlowResponse;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.Serializable;

public class OauthFlowManager implements Serializable {
    
    // Instance data fields
    private final String oauthBaseURL;
    private final String authorizeAccountBaseURL;
    private final String requestTokenURI;
    private final String accessTokenURI;
    private final String renewAccessTokenURI;
    private final String revokeAccessTokenURI;
    private final Key consumerKey;
    private final Key consumerSecret;
    private Key token;
    private Key tokenSecret;
    private Key verifier;
    private BrowserRequest browserRequestMethod = new BrowserRequest();
    private final int OAUTH_ATTEMPTS_LIMIT = 10;
    
    // Static data fields
    private final static ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    /**
     * Standard constructor of an OauthFlowManager.
     * @param oauthBaseURL
     * @param authorizeAccountBaseURL
     * @param requestTokenURI
     * @param accessTokenURI
     * @param renewAccessTokenURI
     * @param revokeAccessTokenURI
     * @param consumerKey
     * @param consumerSecret 
     */
    public OauthFlowManager(String oauthBaseURL, String authorizeAccountBaseURL, String requestTokenURI, String accessTokenURI, String renewAccessTokenURI, String revokeAccessTokenURI, Key consumerKey, Key consumerSecret) {
        this.oauthBaseURL = oauthBaseURL;
        this.authorizeAccountBaseURL = authorizeAccountBaseURL;
        this.requestTokenURI = requestTokenURI;
        this.accessTokenURI = accessTokenURI;
        this.renewAccessTokenURI = renewAccessTokenURI;
        this.revokeAccessTokenURI = revokeAccessTokenURI;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;     
    }
    
    /**
     * This optional method is for customization of the authorize application
     * step of the Oauth authentication flow. Some applications open a browser
     * window to display a verifier code, but others send the verifier code 
     * through other means. To customize, create a class that extends 
     * BrowserRequest and pass it to this method.
     * @param requestMethod an instance of BrowserRequest
     */
    public void setBrowserRequest(BrowserRequest requestMethod) {
        browserRequestMethod = requestMethod;
    }
    
    public Key getToken() throws OauthException {
        performOauthFlowIfRequired();
        
        return token;
    }
    
    public Key getTokenSecret() throws OauthException {
        performOauthFlowIfRequired();
        
        return tokenSecret;
    }
    
    public void renewAccessToken() throws OauthException {
        performOauthFlowIfRequired();
        
        String urlString = oauthBaseURL + renewAccessTokenURI;
        BaseURL etradeBaseURL = new BaseURL(urlString);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret, token, tokenSecret);
        try {
            request.sendAndGetResponse();
            logger.log("Access token successfully renewed.");
        }
        catch (Exception ex) {
            throw new OauthException("Unable to renew access token.");
        }
    }
    
    public void revokeAccessToken() throws OauthException {
        performOauthFlowIfRequired();
        
        String urlString = oauthBaseURL + revokeAccessTokenURI;
        BaseURL etradeBaseURL = new BaseURL(urlString);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret, token, tokenSecret);
        try {
            request.sendAndGetResponse();
            logger.log("Access token successfully revoked");
        }
        catch (Exception ex) {
            throw new OauthException("Unable to renew access token.");
        }
    }
    
    
    // Private helper methods
    
    
    private void performOauthFlowIfRequired() throws OauthException {
        if (oauthFlowRequired())
            performOauthFlow();
    }
    
    private void performOauthFlow() throws OauthException {
        performFirstHalf(1);
        performSecondHalf(1);
    }
    
    private void performFirstHalf(int attemptNumber) throws OauthException {
        try {
            fetchRequestToken();
            fetchVerifier();
        }
        catch (Exception ex) {
            if (attemptNumber > OAUTH_ATTEMPTS_LIMIT) {
                logger.log("First half of Oauth flow unsuccessful after maximum attempts");
                throw new OauthException("Oauth flow unsuccessful.");
            }
            else
                performFirstHalf(attemptNumber + 1);
        }
    }
    
    private void performSecondHalf(int attemptNumber) throws OauthException {
        try {
            fetchAccessToken();
        }
        catch (Exception ex) {
            if (attemptNumber > OAUTH_ATTEMPTS_LIMIT) {
                logger.log("Second half of oauth flow unsuccessful after maximum attempts");
                throw new OauthException("Oauth flow unsuccessful.");
            }
            else
                performSecondHalf(attemptNumber + 1);
        }
    }
    
    private void fetchRequestToken() throws MalformedURLException, IOException, OauthException {
        String urlString = oauthBaseURL + requestTokenURI;
        BaseURL etradeBaseURL = new BaseURL(urlString);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret);
        OauthFlowResponse response = request.sendAndGetResponse();
        Parameters responseParameters = response.parseResponse();
        
        token = new Key(responseParameters.getValue("oauth_token"));
        tokenSecret = new Key(responseParameters.getValue("oauth_token_secret"));
    }
    
    private void fetchVerifier() throws MalformedURLException, IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(authorizeAccountBaseURL);
        browserRequestMethod.configureBrowserRequest(etradeBaseURL, consumerKey, token);
        
        verifier = browserRequestMethod.go();
    }
    
    private void fetchAccessToken() throws MalformedURLException, IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL + accessTokenURI);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret, token, tokenSecret, verifier);
        OauthFlowResponse response = request.sendAndGetResponse();
        Parameters responseParameters = response.parseResponse();
        
        token = new Key(responseParameters.getValue("oauth_token"));
        tokenSecret = new Key(responseParameters.getValue("oauth_token_secret"));
    }
    
    private boolean oauthFlowRequired() {
        return token == null;
    }
}
