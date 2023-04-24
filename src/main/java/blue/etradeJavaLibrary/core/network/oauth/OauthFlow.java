
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

public class OauthFlow {
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
    
    private final static ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    public OauthFlow(String oauthBaseURL, String authorizeAccountBaseURL, String requestTokenURI, String accessTokenURI, String renewAccessTokenURI, String revokeAccessTokenURI, Key consumerKey, Key consumerSecret) {
        this.oauthBaseURL = oauthBaseURL;
        this.authorizeAccountBaseURL = authorizeAccountBaseURL;
        this.requestTokenURI = requestTokenURI;
        this.accessTokenURI = accessTokenURI;
        this.renewAccessTokenURI = renewAccessTokenURI;
        this.revokeAccessTokenURI = revokeAccessTokenURI;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;     
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
            performOauthFlow(0);
    }
    
    private void performOauthFlow(int depth) throws OauthException {
        final int RECURSION_DEPTH_LIMIT = 1;
        
        try {
            fetchRequestToken();
            fetchVerifier();
            fetchAccessToken();
            logger.log("Oauth authentication performed successfully.");
        }
        catch (Exception ex) {
            if (depth <= RECURSION_DEPTH_LIMIT)
                performOauthFlow(depth + 1);
            else {
                logger.log("Oauth authentication flow unsuccessful.");
                throw new OauthException("Oauth flow unsuccessful.");
            }      
        }
    }
    
    private void fetchRequestToken() throws MalformedURLException, IOException, OauthException {
        String urlString = oauthBaseURL + requestTokenURI;
        BaseURL etradeBaseURL = new BaseURL(urlString);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret);
        OauthFlowResponse response = request.sendAndGetResponse();
        Parameters responseParameters = response.parseResponse();
        
        token = new Key(responseParameters.getDecodedValue("oauth_token"));
        tokenSecret = new Key(responseParameters.getDecodedValue("oauth_token_secret"));
    }
    
    private void fetchVerifier() throws MalformedURLException, IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(authorizeAccountBaseURL);
        BrowserRequest br = new BrowserRequest(etradeBaseURL, consumerKey, token);
        
        br.go();
        
        verifier = getVerifierUserInput();
    }
    
    private void fetchAccessToken() throws MalformedURLException, IOException, OauthException {
        BaseURL etradeBaseURL = new BaseURL(oauthBaseURL + accessTokenURI);
        
        OauthFlowRequest request = new OauthFlowRequest(etradeBaseURL, consumerKey, consumerSecret, token, tokenSecret, verifier);
        OauthFlowResponse response = request.sendAndGetResponse();
        Parameters responseParameters = response.parseResponse();
        
        token = new Key(responseParameters.getDecodedValue("oauth_token"));
        tokenSecret = new Key(responseParameters.getDecodedValue("oauth_token_secret"));
    }
    
    private static Key getVerifierUserInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        System.out.print("Enter the verifier: ");
        
        String verifier = scanner.next();
        return new Key(verifier);
    }
    
    private boolean oauthFlowRequired() {
        return token == null;
    }
}
