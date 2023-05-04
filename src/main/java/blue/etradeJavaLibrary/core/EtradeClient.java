
package blue.etradeJavaLibrary.core;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.*;
import blue.etradeJavaLibrary.core.network.oauth.*;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.etradeObjects.accounts.BalanceResponse;
import blue.etradeJavaLibrary.etradeObjects.accounts.AccountsListResponse;
import blue.etradeJavaLibrary.etradeObjects.accounts.TransactionDetailsResponse;
import blue.etradeJavaLibrary.etradeObjects.accounts.TransactionListResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.*;
import java.util.Date;

/**
 * This class represents a connection to the E*Trade API. Upon instantiation, the Oauth authentication flow is performed
 * if an only if it is necessary. Consult the E*Trade API documentation for details on access token renewal and
 * expiration. EtradeClient implements AutoCloseable, so whenever close() is called, the current instance of
 * EtradeClient is serialized to a save file, where it can be loaded again. There can only be one instance during
 * runtime, and it can be retrieved by calling getLiveClient() or getSandboxClient(). These methods both assume
 * that the consumer keys are stored in system environment variables. For more details, see the KeyAndURLExtractor
 * class.
 */
public final class EtradeClient extends APIManager
        implements AutoCloseable {
    
    // Instance data fields
    /**
     * Specifies the environment of the instance object. The two options are LIVE and SANDBOX. Both of these
     * environments are defined by Etrade and explained in the API documentation.
     */
    public final EnvironmentType environmentType;

    /**
     * The name of the .dat file that the EtradeClient object is saved to. It is either named "livesave.dat" or
     * "sandboxsave.dat".
     */
    private String saveFileName;
    
    // Static data fields
    /**
     * The only instance of EtradeClient at runtime
     */
    private static EtradeClient currentSession;

    /**
     * A public option specifying if EtradeClient objects should be loaded from a save or not
     */
    public static boolean loadFromSave = true;

    /**
     * The only instance of the api logger in this class
     */
    protected static final ProgramLogger apiLogger = ProgramLogger.getAPILogger();
    
    /* Private constructor to prevent instantiation */
    private EtradeClient(EnvironmentType environmentType) throws NetworkException {
        this.environmentType = environmentType;
        networkLogger.log("Current environment type", environmentType.name());
        setSaveFileName();
        
        try {
            var etradeOauthParameters = new EtradeOauthParameters();

            super.configure(getBaseURLSet(), getKeys(), etradeOauthParameters,new EtradeBrowserRequest());
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

    /**
     * Returns an instance of EtradeClient in the Live environment. All trades executed with this object will use REAL
     * money.
     * @return The last saved instance of EtradeClient or a new instance of EtradeClient in the live environment
     * @throws NetworkException if something goes wrong with the connection to Etrade
     */
    public static EtradeClient getLiveClient() throws NetworkException {
        return getClient(EnvironmentType.LIVE);
    }

    /**
     * Returns an instance of EtradeClient in the Sandbox environment. All trades executed with this object will
     * NOT use real money, and market data retrieved will NOT be accurate.
     * @return The last saved instance of EtradeClient or a new instance of EtradeClient in the sandbox environment
     * @throws NetworkException
     */
    public static EtradeClient getSandboxClient() throws NetworkException {
        return getClient(EnvironmentType.SANDBOX);
    }

    /**
     * Returns a list of all the user's accounts
     * @return an AccountsList object representing all the user's accounts
     * @throws NetworkException
     */
    public AccountsListResponse getAccountsList() throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_ACCOUNT_LIST_URI;
        var accountsListResponse = new AccountsListResponse();
        
        try {
            sendAPIGetRequest(requestURI, accountsListResponse);
            apiLogger.log("Accounts list retrieved successfully.");
            apiLogger.log("Response", accountsListResponse.toString());
            
            return accountsListResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Accounts list could not be retrieved.");
            throw new NetworkException("Accounts list could not be retrieved", ex);
        }
    }

    public BalanceResponse getAccountBalance(String accountIdKey) throws NetworkException {
        String instType = "BROKERAGE";
        String realTimeNAV = "true";
        var balanceResponse = new BalanceResponse();

        String requestURI = KeyAndURLExtractor.API_ACCOUNT_BALANCE_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey);
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("instType", instType);
        queryParameters.addParameter("realTimeNAV", realTimeNAV);

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, balanceResponse);
            apiLogger.log("Account balance retrieved successfully.");
            apiLogger.log("Response", balanceResponse.toString());

            return balanceResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Account balance could not be retrieved.");
            throw new NetworkException("Account balance could not be retrieved", ex);
        }
    }

    public TransactionListResponse getTransactionList(String accountIdKey) throws NetworkException {
        var transactionListResponse = new TransactionListResponse();
        String requestURI = KeyAndURLExtractor.API_LIST_TRANSACTIONS_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey);

        try {
            sendAPIGetRequest(requestURI, pathParameters, transactionListResponse);
            apiLogger.log("Transactions list retrieved successfully.");
            apiLogger.log("Response", transactionListResponse.toString());

            return transactionListResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Transactions list could not be retrieved.");
            throw new NetworkException("Transactions list could not be retrieved.", ex);
        }
    }

    public TransactionListResponse getTransactionList(String accountIdKey, Date startDate, Date endDate) throws NetworkException {
        return getTransactionList(accountIdKey, startDate, endDate, 50, true);
    }

    public TransactionListResponse getTransactionList(String accountIdKey, Date startDate, Date endDate, int count, boolean ascending) throws NetworkException {
        var transactionListResponse = new TransactionListResponse();
        String startDateString = formatDateMMDDYYYY(startDate);
        String endDateString = formatDateMMDDYYYY(endDate);
        String sortOrder = (ascending) ? "ASC" : "DESC";
        String requestURI = KeyAndURLExtractor.API_LIST_TRANSACTIONS_URI;

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("startDate", startDateString);
        queryParameters.addParameter("endDate", endDateString);
        queryParameters.addParameter("count", count + "");
        var pathParameters = new PathParameters("accountIdKey", accountIdKey);

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, transactionListResponse);
            apiLogger.log("Transactions list retrieved successfully.");
            apiLogger.log("Response", transactionListResponse.toString());

            return transactionListResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Transactions list could not be retrieved.");
            throw new NetworkException("Transactions list could not be retrieved.", ex);
        }
    }

    public TransactionDetailsResponse getTransactionDetails(String accountIdKey, long transactionId) throws NetworkException {
        var transactionDetailsResponse = new TransactionDetailsResponse();
        String requestURI = KeyAndURLExtractor.API_TRANSACTION_DETAILS_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey, "transactionId", transactionId + "");

        try {
            sendAPIGetRequest(requestURI, pathParameters, transactionDetailsResponse);
            apiLogger.log("Transaction details retrieved successfully.");
            apiLogger.log("Response", transactionDetailsResponse.toString());

            return transactionDetailsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Transaction details could not be retrieved.");
            throw new NetworkException("Transaction details could not be retrieved.");
        }
    }

    public TransactionDetailsResponse getTransactionDetails(String accountIdKey, long transactionId, long storeId) throws NetworkException {
        var transactionDetailsResponse = new TransactionDetailsResponse();
        String requestURI = KeyAndURLExtractor.API_TRANSACTION_DETAILS_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey, "transactionId", transactionId + "");
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("storeId", storeId + "");

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, transactionDetailsResponse);
            apiLogger.log("Transaction details retrieved successfully.");
            apiLogger.log("Response", transactionDetailsResponse.toString());

            return transactionDetailsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Transaction details could not be retrieved.");
            throw new NetworkException("Transaction details could not be retrieved.");
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
        var file = new FileOutputStream(saveFileName);

        try (ObjectOutputStream objectOutput = new ObjectOutputStream(file)) {
            objectOutput.writeObject(this);
        }
    }

    private static EtradeClient loadLastSession(EnvironmentType environmentType) throws IOException {
        var file = new FileInputStream(getSaveFileName(environmentType));

        try (ObjectInputStream objectInput = new ObjectInputStream(file)) {
            if (!loadFromSave) throw new IOException();

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
            environmentBaseURL = KeyAndURLExtractor.LIVE_BASE_URL;

        return new BaseURLSet(environmentBaseURL,
                environmentBaseURL,
                KeyAndURLExtractor.OAUTH_REQUEST_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_RENEW_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_REVOKE_ACCESS_TOKEN_URI,
                KeyAndURLExtractor.OAUTH_VERIFIER_BASE_URL);
    }

    private boolean hasBeenTwoHoursSinceLastRequest() {
        var now = Instant.now();
        var twoHours = Duration.ofHours(2);
        var timeSinceLastRequest = Duration.between(timeOfLastRequest, now);
        
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
        var timeOfLastRequestAtThisTimeZone = timeOfLastRequest.atZone(ZoneId.systemDefault());
        
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(timeOfLastRequestAtThisTimeZone);
    }

    private static String formatDateMMDDYYYY(Date date) {
        var formatter = new SimpleDateFormat("MMddyyyy");

        return formatter.format(date);
    }
}
