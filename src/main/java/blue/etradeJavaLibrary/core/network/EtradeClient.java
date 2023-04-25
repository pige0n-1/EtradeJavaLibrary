
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthFlow;
import blue.etradeJavaLibrary.core.network.oauth.model.*;

public class EtradeClient {
    private String mainBaseURL;
    private String authorizeApplicationBaseURL = KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL;
    
    private Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    
    public final EnvironmentType environmentType;
    private static final ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    private static EtradeClient liveSession;
    private static EtradeClient sandboxSession;
    
    private EtradeClient(EnvironmentType environmentType) throws OauthException {
        this.environmentType = environmentType;
        logger.log("Current environment type", environmentType.name());
        determineBaseURL();
        performOauthFlow();
        logger.log("Logged into Etrade successfully");
    }
    
    public static EtradeClient getClient(EnvironmentType environmentType) throws NetworkException {
        try {
            if (environmentType == EnvironmentType.LIVE)
                return getLiveClient();
            else
                return getSandboxClient();
        }
        catch (OauthException ex) {
            throw new NetworkException("Connection to E*Trade was unsuccessful.");
        }
    }
    
    public static enum EnvironmentType {
        LIVE,
        SANDBOX
    }
    
    
    // Private helper methods
    
    
    private void determineBaseURL() {
        if (environmentType == EnvironmentType.SANDBOX)
            mainBaseURL = KeyAndURLExtractor.SANDBOX_BASE_URL;
        else
            mainBaseURL = KeyAndURLExtractor.API_BASE_URL;
    }
    
    private void performOauthFlow() throws OauthException {
        OauthFlow oauthFlow = new OauthFlow(
                mainBaseURL, 
                authorizeApplicationBaseURL, 
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI,
                consumerKey, 
                consumerSecret);
        token = oauthFlow.getToken();
        tokenSecret = oauthFlow.getToken();
    }
    
    private static EtradeClient getLiveClient() throws OauthException {
        if (liveSession == null)
            liveSession = new EtradeClient(EnvironmentType.LIVE);
        
        return liveSession;
    }
    
    private static EtradeClient getSandboxClient() throws OauthException {
        if (sandboxSession == null)
            sandboxSession = new EtradeClient(EnvironmentType.SANDBOX);
        
        return sandboxSession;
    }
}
