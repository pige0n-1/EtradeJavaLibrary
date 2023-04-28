
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import blue.etradeJavaLibrary.core.network.oauth.requests.OauthFlowRequest;
import java.io.IOException;
import java.io.Serializable;

public class OauthFlowManager implements Serializable {
    
    // Instance data fields
    private final BaseURLSet urls;
    private final OauthKeySet keys;
    private BrowserRequest browserRequestMethod = new BrowserRequest();
    
    // Static data fields
    private final static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    public OauthFlowManager(BaseURLSet urls, OauthKeySet keys) {
        this.urls = urls;
        this.keys = keys;
        
        if (keys.hasRetrievedAToken())
            keys.removeToken();
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
    
    public void getAccessToken() throws OauthException {
        keys.removeToken();
        performOauthFlow();
    }
    
    public void renewAccessToken() throws OauthException {
        performOauthFlowIfRequired();

        BaseURL etradeBaseURL = new BaseURL(urls.oauthBaseURL, urls.renewAccessTokenURI);
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
    
        BaseURL etradeBaseURL = new BaseURL(urls.oauthBaseURL, urls.revokeAccessTokenURI);
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
        BaseURL etradeBaseURL = new BaseURL(urls.oauthBaseURL, urls.requestTokenURI);
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        
        Parameters response = request.sendAndGetResponse().parse();
        
        Key token = new Key(response.getValue("oauth_token"));
        Key tokenSecret = new Key(response.getValue("oauth_token_secret"));
        
        keys.setToken(token, tokenSecret);
    }
    
    private Key fetchVerifier() throws IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(urls.authorizeAccountBaseURL);
        browserRequestMethod.configureBrowserRequest(etradeBaseURL, keys);
        
        Key verifier = browserRequestMethod.go();
        
        return verifier;
    }
    
    private void fetchAccessToken(Key verifier) throws IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(urls.oauthBaseURL, urls.accessTokenURI);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys, verifier);
        Parameters response = request.sendAndGetResponse().parse();
        
        Key token = new Key(response.getValue("oauth_token"));
        Key tokenSecret = new Key(response.getValue("oauth_token_secret"));
        
        keys.setToken(token, tokenSecret);
    }
}
