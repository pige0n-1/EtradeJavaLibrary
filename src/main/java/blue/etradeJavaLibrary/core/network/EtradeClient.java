
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthFlow;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.net.MalformedURLException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class EtradeClient implements Serializable {
    private String mainBaseURL;
    private String authorizeApplicationBaseURL = KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL;
    
    private Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    private OauthFlow oauthFlow;
    
    public final EnvironmentType environmentType;
    private static final ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    private static EtradeClient liveSession;
    private static EtradeClient sandboxSession;
    
    private static final String FILE_NAME = "etradeSession.dat";
    
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        logger.log("Current environment type", environmentType.name());
        determineBaseURL();
        performOauthFlow();
        logger.log("Logged into Etrade successfully");
    }
    
    public static EtradeClient getClient(EnvironmentType environmentType) throws NetworkException {
        if (environmentType == EnvironmentType.LIVE)
            return getLiveClient();
        else
            return getSandboxClient();
    }
    
    public static enum EnvironmentType {
        LIVE,
        SANDBOX
    }
    
    public void renewSession() throws NetworkException {
        try {
            oauthFlow.renewAccessToken();
        }
        catch (OauthException ex) {
            throw new NetworkException("Session could not be renewed.");
        }
    }
    
    public void endSession() throws NetworkException {
        try {
            oauthFlow.revokeAccessToken();
        }
        catch (OauthException ex) {
            throw new NetworkException("Session could not be ended.");
        }
    }
    
    public void saveSession() throws IOException {
        FileOutputStream file = new FileOutputStream(FILE_NAME);
        ObjectOutputStream objectOutput = new ObjectOutputStream(file);
        objectOutput.writeObject(this);
        objectOutput.close();
    }
    
    public static EtradeClient loadLastSavedSession() throws IOException {
        FileInputStream file = new FileInputStream(FILE_NAME);
        EtradeClient client;
        try (ObjectInputStream objectInput = new ObjectInputStream(file)) {
            client = (EtradeClient)objectInput.readObject();
        }
        catch (ClassNotFoundException ex) {
            logger.log("Last session could not be loaded.");
            return null;
        }
        
        return client;
    }
    
    
    // Private helper methods
    
    
    private void determineBaseURL() {
        if (environmentType == EnvironmentType.SANDBOX)
            mainBaseURL = KeyAndURLExtractor.SANDBOX_BASE_URL;
        else
            mainBaseURL = KeyAndURLExtractor.API_BASE_URL;
    }
    
    private void performOauthFlow() throws NetworkException {
        oauthFlow = new OauthFlow(
                mainBaseURL, 
                authorizeApplicationBaseURL, 
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI,
                consumerKey, 
                consumerSecret);
        
        try {
            token = oauthFlow.getToken();
            tokenSecret = oauthFlow.getToken();
        }
        catch (OauthException ex) {
            throw new NetworkException("The oauth flow encountered an issue");
        }
    }
    
    private static EtradeClient getLiveClient() throws NetworkException {
        if (liveSession == null)
            liveSession = new EtradeClient(EnvironmentType.LIVE);
        
        return liveSession;
    }
    
    private static EtradeClient getSandboxClient() throws NetworkException {
        if (sandboxSession == null)
            sandboxSession = new EtradeClient(EnvironmentType.SANDBOX);
        
        return sandboxSession;
    }
}
