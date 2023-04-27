
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthFlowManager;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.*;

public class EtradeClient 
        implements Serializable, AutoCloseable {
    
    // Instance data fields
    public final EnvironmentType environmentType;
    private String oauthBaseURL;
    private final String authorizeApplicationBaseURL = KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL;
    private final Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private final Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    private OauthFlowManager oauthFlow;
    private Instant timeOfLastAccessTokenRenewal;
    
    // Static data fields
    private transient static final ProgramLogger logger = ProgramLogger.getProgramLogger();
    private static EtradeClient currentSession;
    private static final String SAVE_FILE_NAME = "etradeSession.dat";
    
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        logger.log("Current environment type", environmentType.name());
        determineBaseURL();
        performOauthFlow();
        logger.log("Access token retrieved at", timeOfLastAccessTokenRenewal.toString());
        logger.log("Logged into Etrade successfully");
        currentSession = this;
    }
    
    /**
     * Static factory method to return an EtradeClient object of the specified
     * EnvironmentType. The method will attempt to load a saved client session
     * first, but it that is not possible, or if the last saved session has a
     * different environment type, it will create a new EtradeClient object. At
     * a given time, there can only be one instance of EtradeClient.
     * 
     * @param environmentType Either LIVE or SANDBOX
     * @throws NetworkException if something goes wrong with the connection to
     * E*trade servers
     * @return The only current instance of EtradeClient
     */
    public static EtradeClient getClient(EnvironmentType environmentType) throws NetworkException {
        if (currentSessionMatches(environmentType))
            return currentSession;
        else
            establishNewCurrentSession(environmentType);
        
        logger.log("Last access token renewal for current session", currentSession.timeOfLastAccessTokenRenewal.atZone(ZoneId.of("America/Chicago")).toString());
        
        return currentSession;
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
    
    public static void main(String[] args) {
        
    }
    
    
    // Private helper methods
    
    
    private static void establishNewCurrentSession(EnvironmentType environmentType) throws NetworkException {
        try {
            EtradeClient previousSession = loadLastSession();
            
            if (previousSession.environmentType == environmentType) {
                previousSession.renewAccessTokenIfNeeded();
                currentSession = previousSession;
            }
            
            else
                logger.log("Saved EtradeClient object found, but different environment type");
        }
        catch (IOException ex) {
            logger.log("No saved EtradeClient to retrieve. Creating new instance.");
            currentSession = new EtradeClient(environmentType);
        }
    }
    
    private void saveSession() throws IOException {
        FileOutputStream file = new FileOutputStream(SAVE_FILE_NAME);
        
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(file)) {
            objectOutput.writeObject(this);
        }
    }
    
    private static EtradeClient loadLastSession() throws IOException {
        FileInputStream file = new FileInputStream(SAVE_FILE_NAME);
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
            oauthBaseURL = KeyAndURLExtractor.SANDBOX_BASE_URL;
        else
            oauthBaseURL = KeyAndURLExtractor.API_BASE_URL;
    }
    
    private void performOauthFlow() throws NetworkException {
        oauthFlow = new OauthFlowManager(
                oauthBaseURL, 
                authorizeApplicationBaseURL, 
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI,
                consumerKey, 
                consumerSecret);
        
        try {
            token = oauthFlow.getToken();
            timeOfLastAccessTokenRenewal = Instant.now();
            tokenSecret = oauthFlow.getToken();
        }
        catch (OauthException ex) {
            throw new NetworkException("The oauth flow encountered an issue");
        }
    }
    
    private void renewAccessTokenIfNeeded() throws NetworkException {
        if (accessTokenExpired()) {
            logger.log("Access token is expired. Re-performing Oauth flow...");
            performOauthFlow();
        }
        else if (hasBeenTwoHoursSinceLastRenewal()) {
            logger.log("Access token is inactive. Renewing access token...");
            renewAccessToken();
        }
    }
    
    private boolean hasBeenTwoHoursSinceLastRenewal() {
        Instant now = Instant.now();
        
        return Duration.between(timeOfLastAccessTokenRenewal, now).compareTo(Duration.ofHours(2)) > 0;
    }
    
    private boolean accessTokenExpired() {
        final ZoneId EST_ZONE_ID = ZoneId.of("America/New_York");
        
        var lastRenewalTimeEST = timeOfLastAccessTokenRenewal.atZone(EST_ZONE_ID);
        var now = Instant.now().atZone(EST_ZONE_ID);
        
        var daysBetweenTime = Period.between(lastRenewalTimeEST.toLocalDate(), now.toLocalDate());
        
        return !daysBetweenTime.isZero();  
    }
    
    private void renewAccessToken() throws NetworkException {
        try {
            oauthFlow.renewAccessToken();
            timeOfLastAccessTokenRenewal = Instant.now();
        }
        catch (OauthException ex) {
            throw new NetworkException("Session could not be renewed.");
        }
    }
    
    private static boolean currentSessionMatches(EnvironmentType environmentType) {
        return currentSession != null && currentSession.environmentType == environmentType;
    }
}
