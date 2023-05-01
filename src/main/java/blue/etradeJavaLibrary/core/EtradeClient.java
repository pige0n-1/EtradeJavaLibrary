
package blue.etradeJavaLibrary.core;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.*;
import blue.etradeJavaLibrary.core.network.oauth.*;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.model.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.*;
import java.time.format.*;

public final class EtradeClient extends APIManager
        implements AutoCloseable {
    
    // Instance data fields
    public final EnvironmentType environmentType;
    private String saveFileName;
    
    // Static data fields
    private static EtradeClient currentSession;
    public transient static boolean loadFromSave = true;
    protected transient static final ProgramLogger apiLogger = ProgramLogger.getAPILogger();
    
    /* Private constructor to prevent instantiation */
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        networkLogger.log("Current environment type", environmentType.name());
        setSaveFileName();
        
        try {
            super.configure(getBaseURLSet(), getKeys(), new EtradeBrowserRequest());
        }
        catch (OauthException ex) {
            throw new NetworkException("Could not log into Etrade.", ex);
        }
        
        networkLogger.log("Time of last request", formatLastRequestTime());
        networkLogger.log("Logged into Etrade successfully");
        
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
        if (currentSessionMatches(environmentType)) return currentSession;
        
        else establishNewCurrentSession(environmentType);
        
        return currentSession;
    }
    
    public static EtradeClient getLiveClient() throws NetworkException {
        return getClient(EnvironmentType.LIVE);
    }
    
    public static EtradeClient getSandboxClient() throws NetworkException {
        return getClient(EnvironmentType.SANDBOX);
    }
    
    public AccountsList getAccountsList() throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_ACCOUNT_LIST_URI;
        AccountsList accountsList = new AccountsList();
        
        try {
            sendAPIGetRequest(requestURI, accountsList);
            apiLogger.log("Accounts list retrieved successfully.");
            apiLogger.log("Response", accountsList.toString());
            
            return accountsList;
        }
        catch (OauthException ex) {
            apiLogger.log("Accounts list could not be retrieved");
            throw new NetworkException("Accounts list could not be retrieved", ex);
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
            networkLogger.log("Time of last request", currentSession.formatLastRequestTime());
        }
        catch (IOException ex) {
            networkLogger.log("No saved EtradeClient to retrieve. Creating new instance.");
            currentSession = new EtradeClient(environmentType);
        }
    }
    
    private void saveSession() throws IOException {
        FileOutputStream file = new FileOutputStream(saveFileName);
        
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(file)) {
            objectOutput.writeObject(this);
        }
    }
    
    private static EtradeClient loadLastSession(EnvironmentType environmentType) throws IOException {
        FileInputStream file = new FileInputStream(getSaveFileName(environmentType));
        
        try (ObjectInputStream objectInput = new ObjectInputStream(file)) {
            if (loadFromSave == false) throw new IOException();
            
            var client = objectInput.readObject();
            networkLogger.log("Last EtradeClient session loaded successfully.");
            
            return (EtradeClient)client;
        }
        
        catch (ClassNotFoundException ex) {
            networkLogger.log("Last EtradeClient session could not be loaded.");
            throw new IOException("Last EtradeClient session could not be loaded.", ex);
        }
    }
    
    private BaseURLSet getBaseURLSet() throws NetworkException {
        String environmentBaseURL;
        if (environmentType == EnvironmentType.SANDBOX)
            environmentBaseURL = KeyAndURLExtractor.SANDBOX_BASE_URL;
        else
            environmentBaseURL = KeyAndURLExtractor.MAIN_BASE_URL;
        
        BaseURLSet urls = new BaseURLSet(environmentBaseURL, 
                environmentBaseURL, 
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI, 
                KeyAndURLExtractor.OAUTH_VERIFIER_BASE_URL);
                
        return urls;
    }
    
    @Override
    protected void renewAccessTokenIfNeeded() throws OauthException {
        if (accessTokenExpired()) {
            networkLogger.log("Access token is expired. Re-performing Oauth flow...");
            getNewAccessToken();
        }
        else if (hasBeenTwoHoursSinceLastRequest()) {
            networkLogger.log("Access token is inactive. Renewing access token...");
            renewAccessToken();
        }
    }
    
    private boolean hasBeenTwoHoursSinceLastRequest() {
        Instant now = Instant.now();
        Duration twoHours = Duration.ofHours(2);
        Duration timeSinceLastRequest = Duration.between(timeOfLastRequest, now);
        
        return timeSinceLastRequest.compareTo(twoHours) > 0;
    }
    
    private boolean accessTokenExpired() {
        final ZoneId EST_ZONE_ID = ZoneId.of("America/New_York");
        
        var lastRequestTimeEST = timeOfLastRequest.atZone(EST_ZONE_ID);
        var now = Instant.now().atZone(EST_ZONE_ID);
        
        var daysBetweenTime = Period.between(lastRequestTimeEST.toLocalDate(), now.toLocalDate());
        int numberOfDays = daysBetweenTime.getDays();
        
        return numberOfDays >= 1;  
    }
    
    private static boolean currentSessionMatches(EnvironmentType environmentType) {
        return currentSession != null && currentSession.environmentType == environmentType;
    }
    
    private void setSaveFileName() {
        saveFileName = getSaveFileName(environmentType);
    }
    
    private static String getSaveFileName(EnvironmentType environmentType) {
        return environmentType.name().toLowerCase() + "save.dat";
    }
    
    private OauthKeySet getKeys() {
        Key consumerKey;
        Key consumerSecret;
        
        if (environmentType == EnvironmentType.LIVE) {
            consumerKey = KeyAndURLExtractor.getConsumerKey();
            consumerSecret = KeyAndURLExtractor.getConsumerSecret();
        }
        else {
            consumerKey = KeyAndURLExtractor.getSandboxConsumerKey();
            consumerSecret = KeyAndURLExtractor.getSandboxConsumerSecret();
        }
        
        return new OauthKeySet(consumerKey, consumerSecret);
    }
    
    private String formatLastRequestTime() {
        ZonedDateTime time = timeOfLastRequest.atZone(ZoneId.systemDefault());
        
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(time);
    }
}
