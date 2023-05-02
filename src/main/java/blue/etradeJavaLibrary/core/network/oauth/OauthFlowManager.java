
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
    private OauthParameters oauthParameters = new OauthParameters();
    private BrowserRequest browserRequestMethod = new BrowserRequest();
    
    // Static data fields
    private final static ProgramLogger logger = ProgramLogger.getNetworkLogger();

    /**
     * Constructs a new OauthFlowManager instance. All Oauth parameters will be set to the default values, unless
     * setOauthParameters() is called to pass a custom OauthParameters object. The setBrowserRequest method can also
     * be called for customization; however, neither of these two methods are strictly required unless the service
     * provider in the Oauth model requires slightly different or extra Oauth parameters.
     * @param urls
     * @param keys
     */
    public OauthFlowManager(BaseURLSet urls, OauthKeySet keys) {
        this.urls = urls;
        this.keys = keys;
        
        if (keys.hasRetrievedAToken()) keys.removeToken();
    }
    
    /**
     * This optional method is for customization of the authorize application
     * step of the Oauth authentication flow. Some applications open a browser
     * window to display a verifier code, but others send the verifier code 
     * through other means. To customize, create a class that extends 
     * BrowserRequest and pass it to this method.
     * @param browserRequestMethod an instance of BrowserRequest
     */
    public void setBrowserRequest(BrowserRequest browserRequestMethod) {
        this.browserRequestMethod = browserRequestMethod;
    }

    /**
     * Returns oauthParameters
     * @return
     */
    public OauthParameters getOauthParameters() {
        return oauthParameters;
    }

    /**
     * This optional method is for customization of the underlying oauth_parameters that are required by the
     * service provider in the oauth model. Every service provider requires slightly different Oauth parameters,
     * so this method allows the consumer to customize those parameters, and then the OauthManager object sends them
     * to the service provider. At this point, the OauthParameters object will still automatically be configured with
     * the appropriate keys, but any added oauth_parameters at this point will not be removed for the sake of
     * customization. Consult the OauthParameters javadoc for more information about which parameters are configured
     * automatically.
     * @param oauthParameters
     */
    public void setOauthParameters(OauthParameters oauthParameters) {
        this.oauthParameters = oauthParameters;
    }
    
    public void getAccessToken() throws OauthException {
        keys.removeToken();
        performOauthFlow();
    }
    
    public void renewAccessToken() throws OauthException {
        performOauthFlowIfRequired();

        BaseURL etradeBaseURL = new BaseURL(urls.oauthBaseURL, urls.renewAccessTokenURI);
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        request.setOauthParameters(oauthParameters);
        
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
        request.setOauthParameters(oauthParameters);
        
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
        if (!keys.hasRetrievedAToken()) performOauthFlow();
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
        request.setOauthParameters(oauthParameters);
        
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
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, keys);
        request.setOauthParameters(oauthParameters);
        request.setVerifier(verifier);

        Parameters response = request.sendAndGetResponse().parse();
        
        Key token = new Key(response.getValue("oauth_token"));
        Key tokenSecret = new Key(response.getValue("oauth_token_secret"));
        
        keys.setToken(token, tokenSecret);
    }
}
