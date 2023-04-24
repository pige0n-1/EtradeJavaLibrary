
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthFlow;
import blue.etradeJavaLibrary.core.network.oauth.model.*;

public class EtradeClientWrapper {
    private String mainBaseURL;
    private String authorizeApplicationBaseURL = KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL;
    
    private Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    
    public final EnvironmentType environmentType;
    
    private final ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    public EtradeClientWrapper(EnvironmentType environmentType) throws OauthException {
        this.environmentType = environmentType;
        logger.log("Current environment type", environmentType.name());
        determineBaseURL();
        performOauthFlow();
        logger.log("Logged into Etrade successfully");
    }
    
    public enum EnvironmentType {
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
}
