
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthFlow;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EtradeClient 
        implements Serializable, AutoCloseable {
    private String mainBaseURL;
    private String authorizeApplicationBaseURL = KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL;
    
    private Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    private OauthFlow oauthFlow;
    
    public final EnvironmentType environmentType;
    private transient static final ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    private static EtradeClient session;
    
    private static final String FILE_NAME = "etradeSession.dat";
    
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        logger.log("Current environment type", environmentType.name());
        determineBaseURL();
        performOauthFlow();
        logger.log("Logged into Etrade successfully");
    }
    
    public static EtradeClient getClient(EnvironmentType environmentType) throws NetworkException {
        try {
            EtradeClient previousSession = loadLastSession();
            
            if (previousSession.environmentType == environmentType)
                return previousSession;
            
            else
                logger.log("Saved EtradeClient object found, but different environment type");
        }
        catch (Exception ex) {
            logger.log("No saved EtradeClient to retrieve. Creating new instance.");   
        }
        
        return new EtradeClient(environmentType);
    }
    
    public static EtradeClient getLiveClient() throws NetworkException {
        return getClient(EnvironmentType.LIVE);
    }
    
    public static EtradeClient getSandboxClient() throws NetworkException {
        return getClient(EnvironmentType.SANDBOX);
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
    
    @Override
    public void close() {
        try {
            saveSession();
            logger.log("Saved current EtradeClient session successfully.");
        }
        catch (IOException ex) {
            logger.log("Could not save current EtradeClient session.");
        }
    }
    
    @Override
    public String toString() {
        return "EtradeClient session: " + environmentType.name();
    }
    
    
    // Private helper methods
    
    
    private void saveSession() throws IOException {
        FileOutputStream file = new FileOutputStream(FILE_NAME);
        ObjectOutputStream objectOutput = new ObjectOutputStream(file);
        objectOutput.writeObject(this);
        objectOutput.close();
    }
    
    private static EtradeClient loadLastSession() throws IOException {
        FileInputStream file = new FileInputStream(FILE_NAME);
        try (ObjectInputStream objectInput = new ObjectInputStream(file)) {
            var client = objectInput.readObject();
            logger.log("Last EtradeClient session loaded successfully.");
            return (EtradeClient) client;
        }
        catch (ClassNotFoundException ex) {
            logger.log("Last EtradeClient session could not be loaded.");
            throw new IOException("Last EtradeClient session could not be loaded.");
        }
    }    
    
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
}
