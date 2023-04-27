
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.*;
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
    private Key consumerKey;
    private Key consumerSecret;
    private Key token;
    private Key tokenSecret;
    private OauthFlowManager oauthFlow;
    private Instant timeOfLastAccessTokenRenewal;
    private final EnvironmentType environmentType;
    private String SAVE_FILE_NAME;
    private String oauthBaseURL;
    public static boolean loadFromSave = true;
    
    // Static data fields
    private static int numberOfInstances = 0;
    private static EtradeClient currentSession;
    private transient static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    private transient static final ProgramLogger apiLogger = ProgramLogger.getAPILogger();
    
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        setSaveFileName();
        networkLogger.log("Current environment type", environmentType.name());
        
        setConsumerKeyAndSecret();
        determineBaseURL(environmentType);
        performOauthFlow();
        networkLogger.log("Access token retrieved at", timeOfLastAccessTokenRenewal.toString());
        networkLogger.log("Logged into Etrade successfully");
        
        numberOfInstances++;
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
        
        return currentSession;
    }
    
    public static EtradeClient getLiveClient() throws NetworkException {
        return getClient(EnvironmentType.LIVE);
    }
    
    public static EtradeClient getSandboxClient() throws NetworkException {
        return getClient(EnvironmentType.SANDBOX);
    }
    
    public String getAccountsList() throws NetworkException {
        renewAccessTokenIfNeeded();
        
        try {
            BaseURL requestBaseURL = new BaseURL(oauthBaseURL + KeyAndURLExtractor.API_ACCOUNT_LIST_URI);
            APIRequest request = new APIRequest(requestBaseURL, consumerKey, consumerSecret, token, tokenSecret, HttpMethod.GET);
            String response =  request.sendAndGetResponse().parseResponse();
            apiLogger.log("Accounts list retrieved successfully");
            
            return response;
        }
        catch (IOException ex) {
            apiLogger.log("Accounts list could not be retrieved");
            throw new NetworkException("");
        }
    }
    
    @Override
    public void close() {
        try {
            saveSession();
            networkLogger.log("Saved current EtradeClient session successfully.");
        }
        catch (IOException ex) {
            networkLogger.log("Could not save current EtradeClient session.");
        }
    }
    
    @Override
    public String toString() {
        return "EtradeClient session: " + environmentType.name();
    }
    
    
    // Private helper methods
    
    
    private static void establishNewCurrentSession(EnvironmentType environmentType) throws NetworkException {
        try {
            currentSession = loadLastSession(environmentType);
            networkLogger.log("Current environment type", environmentType.name());
        }
        catch (IOException ex) {
            networkLogger.log("No saved EtradeClient to retrieve. Creating new instance.");
            currentSession = new EtradeClient(environmentType);
        }
    }
    
    private void saveSession() throws IOException {
        FileOutputStream file = new FileOutputStream(SAVE_FILE_NAME);
        
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(file)) {
            objectOutput.writeObject(this);
        }
    }
    
    private static EtradeClient loadLastSession(EnvironmentType environmentType) throws IOException {
        FileInputStream file = new FileInputStream(getSaveFileName(environmentType));
        
        try (ObjectInputStream objectInput = new ObjectInputStream(file)) {
            if (loadFromSave == false)
                throw new IOException();
            
            var client = objectInput.readObject();
            networkLogger.log("Last EtradeClient session loaded successfully.");
            
            return (EtradeClient) client;
        }
        
        catch (ClassNotFoundException ex) {
            networkLogger.log("Last EtradeClient session could not be loaded.");
            throw new IOException("Last EtradeClient session could not be loaded.");
        }
    }    
    
    private void determineBaseURL(EnvironmentType environmentType) {
        if (environmentType == EnvironmentType.SANDBOX)
            oauthBaseURL = KeyAndURLExtractor.SANDBOX_BASE_URL;
        else
            oauthBaseURL = KeyAndURLExtractor.API_BASE_URL;
    }
    
    private void performOauthFlow() throws NetworkException {
        oauthFlow = new OauthFlowManager(
                oauthBaseURL, 
                KeyAndURLExtractor.OAUTH_AUTHORIZATION_BASE_URL, 
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI,
                consumerKey, 
                consumerSecret);
        
        try {
            token = oauthFlow.getToken();
            timeOfLastAccessTokenRenewal = Instant.now();
            tokenSecret = oauthFlow.getTokenSecret();
        }
        catch (OauthException ex) {
            throw new NetworkException("The oauth flow encountered an issue");
        }
    }
    
    private void renewAccessTokenIfNeeded() throws NetworkException {
        if (accessTokenExpired()) {
            networkLogger.log("Access token is expired. Re-performing Oauth flow...");
            performOauthFlow();
        }
        else if (hasBeenTwoHoursSinceLastRenewal()) {
            networkLogger.log("Access token is inactive. Renewing access token...");
            renewAccessToken();
        }
    }
    
    private boolean hasBeenTwoHoursSinceLastRenewal() {
        Instant now = Instant.now();
        Duration twoHours = Duration.ofHours(2);
        Duration timeSinceLastRenewal = Duration.between(timeOfLastAccessTokenRenewal, now);
        
        return timeSinceLastRenewal.compareTo(twoHours) > 0;
    }
    
    private boolean accessTokenExpired() {
        final ZoneId EST_ZONE_ID = ZoneId.of("America/New_York");
        
        var lastRenewalTimeEST = timeOfLastAccessTokenRenewal.atZone(EST_ZONE_ID);
        var now = Instant.now().atZone(EST_ZONE_ID);
        
        var daysBetweenTime = Period.between(lastRenewalTimeEST.toLocalDate(), now.toLocalDate());
        int numberOfDays = daysBetweenTime.getDays();
        
        return numberOfDays >= 1;  
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
    
    private void setSaveFileName() {
        SAVE_FILE_NAME = getSaveFileName(environmentType);
    }
    
    private static String getSaveFileName(EnvironmentType environmentType) {
        return environmentType.name().toLowerCase() + "save.dat";
    }
    
    private void setConsumerKeyAndSecret() {
        if (environmentType == EnvironmentType.LIVE) {
            consumerKey = KeyAndURLExtractor.getConsumerKey();
            consumerSecret = KeyAndURLExtractor.getConsumerSecret();
        }
        else {
            consumerKey = KeyAndURLExtractor.getSandboxConsumerKey();
            consumerSecret = KeyAndURLExtractor.getSandboxConsumerSecret();
        }
    }
}
