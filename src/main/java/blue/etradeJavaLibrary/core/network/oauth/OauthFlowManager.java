
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthKeySet;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import blue.etradeJavaLibrary.core.network.oauth.requests.OauthFlowRequest;
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
    private final OauthKeySet keys;
    private BrowserRequest browserRequestMethod = new BrowserRequest();
    
    // Static data fields
    private final static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    /**
     * Standard constructor of an OauthFlowManager.
     * @param oauthBaseURL
     * @param authorizeAccountBaseURL
     * @param requestTokenURI
     * @param accessTokenURI
     * @param renewAccessTokenURI
     * @param revokeAccessTokenURI 
     * @param keys 
     */
    public OauthFlowManager(String oauthBaseURL, String authorizeAccountBaseURL, String requestTokenURI, String accessTokenURI, String renewAccessTokenURI, String revokeAccessTokenURI, OauthKeySet keys) {
        this.oauthBaseURL = oauthBaseURL;
        this.authorizeAccountBaseURL = authorizeAccountBaseURL;
        this.requestTokenURI = requestTokenURI;
        this.accessTokenURI = accessTokenURI;
        this.renewAccessTokenURI = renewAccessTokenURI;
        this.revokeAccessTokenURI = revokeAccessTokenURI;
        this.keys = keys;
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
    
    public void setTokens() throws OauthException {
        performOauthFlowIfRequired();
    }
    
    public void renewAccessToken() throws OauthException {
        performOauthFlowIfRequired();

        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL, renewAccessTokenURI);
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        
        try {
            request.sendAndGetResponse();
            logger.log("Access token successfully renewed.");
        }
        catch (IOException ex) {
            logger.log("Attempt to renew access token failed.");
            throw new OauthException("Unable to renew access token.", ex);
        }
    }
    
    public void revokeAccessToken() throws OauthException {
        performOauthFlowIfRequired();
    
        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL, revokeAccessTokenURI);
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        
        try {
            request.sendAndGetResponse();
            logger.log("Access token successfully revoked");
        }
        catch (IOException ex) {
            logger.log("Attempt to revoke access token failed.");
            throw new OauthException("Unable to renew access token.", ex);
        }
    }
    
    
    // Private helper methods
    
    
    private void performOauthFlowIfRequired() throws OauthException {
        if (!keys.hasRetrievedAToken())
            performOauthFlow();
    }
    
    private void performOauthFlow() throws OauthException {
        try {
            fetchRequestToken();
            Key verifier = fetchVerifier();
            fetchAccessToken(verifier);
        }
        catch (IOException ex) {
            logger.log("The oauth flow was unsuccessful after maximum attempts.");
            throw new OauthException("The oauth flow was unsuccessful.", ex);
        }
    }
    
    private void fetchRequestToken() throws IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL, requestTokenURI);
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        
        Parameters response = request.sendAndGetResponse().parseResponse();
        
        Key token = new Key(response.getValue("oauth_token"));
        Key tokenSecret = new Key(response.getValue("oauth_token_secret"));
        
        keys.setToken(token, tokenSecret);
    }
    
    private Key fetchVerifier() throws IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(authorizeAccountBaseURL);
        browserRequestMethod.configureBrowserRequest(etradeBaseURL, keys);
        
        Key verifier = browserRequestMethod.go();
        
        return verifier;
    }
    
    private void fetchAccessToken(Key verifier) throws IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL, accessTokenURI);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys, verifier);
        Parameters response = request.sendAndGetResponse().parseResponse();
        
        Key token = new Key(response.getValue("oauth_token"));
        Key tokenSecret = new Key(response.getValue("oauth_token_secret"));
        
        keys.setToken(token, tokenSecret);
    }
}
