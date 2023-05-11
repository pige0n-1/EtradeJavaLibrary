
package blue.etradeJavaLibrary;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.*;
import blue.etradeJavaLibrary.core.network.oauth.*;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.model.PreviewOrder;
import blue.etradeJavaLibrary.model.etradeObjects.accounts.*;
import blue.etradeJavaLibrary.model.etradeObjects.alerts.Alert;
import blue.etradeJavaLibrary.model.etradeObjects.alerts.AlertDetailsResponse;
import blue.etradeJavaLibrary.model.etradeObjects.alerts.AlertsResponse;
import blue.etradeJavaLibrary.model.etradeObjects.alerts.DeleteAlertsResponse;
import blue.etradeJavaLibrary.model.etradeObjects.market.LookupResponse;
import blue.etradeJavaLibrary.model.etradeObjects.market.OptionChainResponse;
import blue.etradeJavaLibrary.model.etradeObjects.market.OptionExpireDateResponse;
import blue.etradeJavaLibrary.model.etradeObjects.market.QuoteResponse;
import blue.etradeJavaLibrary.model.etradeObjects.order.OrdersResponse;
import blue.etradeJavaLibrary.model.etradeObjects.order.PreviewOrderResponse;

import javax.management.Query;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.*;
import java.time.format.*;
import java.util.ArrayList;

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

    public TransactionListResponse getTransactionList(String accountIdKey, int count) throws NetworkException {
        var transactionListResponse = new TransactionListResponse();
        String requestURI = KeyAndURLExtractor.API_LIST_TRANSACTIONS_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey);
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count + "");

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

    public TransactionListResponse getTransactionList(String accountIdKey, LocalDate startDate, LocalDate endDate) throws NetworkException {
        return getTransactionList(accountIdKey, startDate, endDate, 50, true);
    }

    public TransactionListResponse getTransactionList(String accountIdKey, LocalDate startDate, LocalDate endDate, int count, boolean ascending) throws NetworkException {
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
        var pathParameters = new PathParameters("accountIdKey", accountIdKey, "transactionId", transactionId);

        try {
            sendAPIGetRequest(requestURI, pathParameters, transactionDetailsResponse);
            apiLogger.log("Transaction details retrieved successfully.");
            apiLogger.log("Response", transactionDetailsResponse.toString());

            return transactionDetailsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Transaction details could not be retrieved.");
            throw new NetworkException("Transaction details could not be retrieved.", ex);
        }
    }

    public TransactionDetailsResponse getTransactionDetails(String accountIdKey, long transactionId, long storeId) throws NetworkException {
        var transactionDetailsResponse = new TransactionDetailsResponse();
        String requestURI = KeyAndURLExtractor.API_TRANSACTION_DETAILS_URI;
        var pathParameters = new PathParameters("accountIdKey", accountIdKey, "transactionId", transactionId);
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
            throw new NetworkException("Transaction details could not be retrieved.", ex);
        }
    }

    public PortfolioResponse getAccountPortfolio(String accountIdKey, int count, boolean ascending, int pageNumber) throws NetworkException {
        var portfolioResponse = new PortfolioResponse();
        String requestURI = KeyAndURLExtractor.API_VIEW_PORTFOLIO_URI;
        String sortOrder = (ascending) ? "ASC" : "DESC";
        var pathParameters = new PathParameters("accountIdKey", accountIdKey);

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count + "");
        queryParameters.addParameter("sortOrder", sortOrder);
        queryParameters.addParameter("totalsRequired", "true");
        queryParameters.addParameter("lotsRequired", "true");
        queryParameters.addParameter("view", "COMPLETE");
        queryParameters.addParameter("pageNumber", pageNumber + "");

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, portfolioResponse);
            apiLogger.log("Account portfolio retrieved successfully.");
            apiLogger.log("Response", portfolioResponse.toString());

            return portfolioResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Account portfolio could not be retrieved.");
            throw new NetworkException("Account portfolio could not be retrieved.", ex);
        }
    }

    public PortfolioResponse getAccountPortfolio(String accountIdKey, int count, boolean ascending) throws NetworkException {
        return getAccountPortfolio(accountIdKey, count, ascending, 1);
    }

    public PortfolioResponse getAccountPortfolio(String accountIdKey) throws NetworkException {
        return getAccountPortfolio(accountIdKey, 50, true);
    }

    public AlertsResponse getStockAlerts(int count, boolean ascending) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_VIEW_ALERTS_URI;
        var alertsResponse = new AlertsResponse();
        String direction = (ascending) ? "ASC" : "DESC";

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count);
        queryParameters.addParameter("direction", direction);
        queryParameters.addParameter("category", "STOCK");

        try {
            sendAPIGetRequest(requestURI, queryParameters, alertsResponse);
            apiLogger.log("Stock alerts retrieved successfully.");
            apiLogger.log("Response", alertsResponse.toString());

            return alertsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Stock alerts could not be retrieved.");
            throw new NetworkException("Stock alerts could not be retrieved.", ex);
        }
    }

    public AlertsResponse getStockAlerts(int count) throws NetworkException {
        return getStockAlerts(count, true);
    }

    public AlertsResponse getStockAlerts() throws NetworkException {
        return getStockAlerts(25);
    }

    public AlertsResponse getAccountAlerts(int count, boolean ascending) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_VIEW_ALERTS_URI;
        var alertsResponse = new AlertsResponse();
        String direction = (ascending) ? "ASC" : "DESC";

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count);
        queryParameters.addParameter("direction", direction);
        queryParameters.addParameter("category", "ACCOUNT");

        try {
            sendAPIGetRequest(requestURI, queryParameters, alertsResponse);
            apiLogger.log("Account alerts retrieved successfully.");
            apiLogger.log("Response", alertsResponse.toString());

            return alertsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Account alerts could not be retrieved.");
            throw new NetworkException("Account alerts could not be retrieved.", ex);
        }
    }

    public AlertsResponse getAccountAlerts(int count) throws NetworkException {
        return getAccountAlerts(25, true);
    }

    public AlertsResponse getAccountAlerts() throws NetworkException {
        return getAccountAlerts(25);
    }

    public AlertsResponse getAlerts(int count, boolean ascending) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_VIEW_ALERTS_URI;
        var alertsResponse = new AlertsResponse();
        String direction = (ascending) ? "ASC" : "DESC";

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count);
        queryParameters.addParameter("direction", direction);

        try {
            sendAPIGetRequest(requestURI, queryParameters, alertsResponse);
            apiLogger.log("All alerts retrieved successfully.");
            apiLogger.log("Response", alertsResponse.toString());

            return alertsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("All alerts could not be retrieved.");
            throw new NetworkException("All alerts could not be retrieved.", ex);
        }
    }

    public AlertsResponse getAlerts(int count) throws NetworkException {
        return getAlerts(count, true);
    }

    public AlertsResponse getAlerts() throws NetworkException {
        return getAlerts(25, true);
    }

    public AlertDetailsResponse getAlertDetails(long alertId) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_ALERT_DETAILS_URI;
        var alertDetailsResponse = new AlertDetailsResponse();
        var pathParameters = new PathParameters("id", alertId);

        try {
            sendAPIGetRequest(requestURI, pathParameters, alertDetailsResponse);
            apiLogger.log("Alert details retrieved successfully");
            apiLogger.log("Response", alertDetailsResponse.toString());

            return alertDetailsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Alert details could not be retrieved");
            throw new NetworkException("Alert details could not be retrieved", ex);
        }
    }

    public DeleteAlertsResponse deleteAlerts(long... alertIds) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_DELETE_ALERTS_URI;
        var deleteAlertsResponse = new DeleteAlertsResponse();
        var pathParameters = new PathParameters();
        pathParameters.addParameter("idList", alertIds);

        try {
            sendAPIDeleteRequest(requestURI, pathParameters, deleteAlertsResponse);
            apiLogger.log("Alerts deleted successfully.");
            apiLogger.log("Response", deleteAlertsResponse.toString());

            return deleteAlertsResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Alerts could not be deleted.");
            throw new NetworkException("Alerts could not be deleted.", ex);
        }
    }

    public DeleteAlertsResponse deleteAlerts(ArrayList<? extends Alert> alerts) throws NetworkException {
        long[] alertIds = new long[alerts.size()];
        for (int i = 0; i < alerts.size(); i++)
            alertIds[i] = alerts.get(i).id;

        return deleteAlerts(alertIds);
    }

    /**
     * Option symbols must be in the following format: "underlier:year:month:day:optionType:strikePrice". Up to 50
     * symbols can be sent at once.
     * @param symbols
     * @return
     * @throws NetworkException
     */
    public QuoteResponse getQuotes(String... symbols) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_QUOTES_URI;
        var quoteResponse = new QuoteResponse();
        boolean overrideSymbolCount = symbols.length > 25;

        var pathParameters = new PathParameters("symbols", symbols);
        var queryParameters = new QueryParameters();
        if (overrideSymbolCount) queryParameters.addParameter("overrideSymbolCount", "true");
        queryParameters.addParameter("requireEarningsDate", "true");

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, quoteResponse);
            apiLogger.log("Quotes received successfully", java.util.Arrays.toString(symbols));
            apiLogger.log("Response", quoteResponse.toString());

            return quoteResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Quotes could not be retrieved.");
            throw new NetworkException("Quotes could not be retrieved.", ex);
        }
    }

    public LookupResponse lookUpProduct(String search) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_LOOK_UP_PRODUCT_URI;
        var lookupResponse = new LookupResponse();

        var pathParameters = new PathParameters("search", search);

        try {
            sendAPIGetRequest(requestURI, pathParameters, lookupResponse);
            apiLogger.log("Looked up product successfully.");
            apiLogger.log("Response", lookupResponse.toString());

            return lookupResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not look up product");
            throw new NetworkException("Could not look up product", ex);
        }
    }

    public OptionChainResponse getOptionChains(String symbol, boolean includeWeekly) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_OPTION_CHAINS_URI;
        var optionChainResponse = new OptionChainResponse();

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbol);
        queryParameters.addParameter("includeWeekly", includeWeekly);
        queryParameters.addParameter("priceType", "ALL");

        try {
            sendAPIGetRequest(requestURI, queryParameters, optionChainResponse);
            apiLogger.log("Retrieved option chain successfully.");
            apiLogger.log("Response", optionChainResponse.toString());

            return optionChainResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve option chain.");
            throw new NetworkException("Could not retrieve option chain.", ex);
        }
    }

    /**
     * The year is represented as the full year number (ex: 2023)
     * @param symbol
     * @param expiryYear
     * @return
     * @throws NetworkException
     */
    public OptionChainResponse getOptionChains(String symbol, int expiryYear) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_OPTION_CHAINS_URI;
        var optionChainResponse = new OptionChainResponse();

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbol);
        queryParameters.addParameter("includeWeekly", true);
        queryParameters.addParameter("priceType", "ALL");
        queryParameters.addParameter("expiryYear", expiryYear);

        try {
            sendAPIGetRequest(requestURI, queryParameters, optionChainResponse);
            apiLogger.log("Retrieved option chain successfully.");
            apiLogger.log("Response", optionChainResponse.toString());

            return optionChainResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve option chain.");
            throw new NetworkException("Could not retrieve option chain.", ex);
        }
    }

    public OptionChainResponse getOptionChains(String symbol, int expiryYear, int expiryMonth) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_OPTION_CHAINS_URI;
        var optionChainResponse = new OptionChainResponse();

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbol);
        queryParameters.addParameter("includeWeekly", true);
        queryParameters.addParameter("priceType", "ALL");
        queryParameters.addParameter("expiryYear", expiryYear);
        queryParameters.addParameter("expiryMonth", expiryMonth);

        try {
            sendAPIGetRequest(requestURI, queryParameters, optionChainResponse);
            apiLogger.log("Retrieved option chain successfully.");
            apiLogger.log("Response", optionChainResponse.toString());

            return optionChainResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve option chain.");
            throw new NetworkException("Could not retrieve option chain.", ex);
        }
    }

    public OptionChainResponse getOptionChains(String symbol, int expiryYear, int expiryMonth, int expiryDay) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_OPTION_CHAINS_URI;
        var optionChainResponse = new OptionChainResponse();

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbol);
        queryParameters.addParameter("includeWeekly", true);
        queryParameters.addParameter("priceType", "ALL");
        queryParameters.addParameter("expiryYear", expiryYear);
        queryParameters.addParameter("expiryMonth", expiryMonth);
        queryParameters.addParameter("expiryDay", expiryDay);

        try {
            sendAPIGetRequest(requestURI, queryParameters, optionChainResponse);
            apiLogger.log("Retrieved option chain successfully.");
            apiLogger.log("Response", optionChainResponse.toString());

            return optionChainResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve option chain.");
            throw new NetworkException("Could not retrieve option chain.", ex);
        }
    }

    public OptionChainResponse getOptionChains(String symbol) throws NetworkException {
        final boolean INCLUDE_WEEKLY = true;

        return getOptionChains(symbol, INCLUDE_WEEKLY);
    }

    /**
     * The expiryType can be any of the following strings: UNSPECIFIED, DAILY, WEEKLY, MONTHLY, QUARTERLY, VIX, ALL,
     * MONTHEND
     * @param symbol
     * @param expiryType
     * @return
     * @throws NetworkException
     */
    public OptionExpireDateResponse getOptionExpirationDates(String symbol, String expiryType) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_GET_OPTION_EXPIRE_DATE_URI;
        var optionExpireDateResponse = new OptionExpireDateResponse();

        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbol);
        queryParameters.addParameter("expiryType", expiryType);

        try {
            sendAPIGetRequest(requestURI, queryParameters, optionExpireDateResponse);
            apiLogger.log("Option expiration dates retrieved successfully.");
            apiLogger.log("Response", optionExpireDateResponse.toString());

            return optionExpireDateResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve option expiration dates.");
            throw new NetworkException("Could not retrieve option expiration dates.", ex);
        }
    }

    public OptionExpireDateResponse getOptionExpirationDates(String symbol) throws NetworkException {
        return getOptionExpirationDates(symbol, "ALL");
    }

    /**
     * The count parameters can be up to 100
     * @param accountIdKey
     * @param count
     * @return
     * @throws NetworkException
     */
    public OrdersResponse getOrders(String accountIdKey, int count) throws NetworkException {
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("count", count);

        return getOrders(accountIdKey, queryParameters);
    }

    public OrdersResponse getOrders(String accountIdKey, int count, long marker) throws NetworkException {
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("marker", marker);
        queryParameters.addParameter("count", count);

        return getOrders(accountIdKey, queryParameters);
    }

    /**
     * The status parameter can be the following options: OPEN, EXECUTED, CANCELLED, INDIVIDUAL_FILLS, CANCEL_REQUESTED,
     * EXPIRED, REJECTED
     * @param accountIdKey
     * @param status
     * @return
     * @throws NetworkException
     */
    public OrdersResponse getOrders(String accountIdKey, String status) throws NetworkException {
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("status", status);

        return getOrders(accountIdKey, queryParameters);
    }

    public OrdersResponse getOrders(String accountIdKey, String status, LocalDate fromDate, LocalDate toDate) throws NetworkException {
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("status", status);
        queryParameters.addParameter("fromDate", formatDateMMDDYYYY(fromDate));
        queryParameters.addParameter("toDate", formatDateMMDDYYYY(toDate));
        queryParameters.addParameter("count", 100);

        return getOrders(accountIdKey, queryParameters);
    }

    /**
     * Up to 25 symbols can be added
     * @param accountIdKey
     * @param fromDate
     * @param toDate
     * @param symbols
     * @return
     * @throws NetworkException
     */
    public OrdersResponse getOrders(String accountIdKey, LocalDate fromDate, LocalDate toDate, String... symbols) throws NetworkException {
        var queryParameters = new QueryParameters();
        queryParameters.addParameter("symbol", symbols);
        queryParameters.addParameter("fromDate", formatDateMMDDYYYY(fromDate));
        queryParameters.addParameter("toDate", formatDateMMDDYYYY(toDate));
        queryParameters.addParameter("count", 100);

        return getOrders(accountIdKey, queryParameters);
    }

    public OrdersResponse getOrders(String accountIdKey) throws NetworkException {
        return getOrders(accountIdKey, new QueryParameters());
    }

    public PreviewOrderResponse previewOrder(String accountIdKey, PreviewOrder previewOrder) throws NetworkException, ParserConfigurationException {
        String requestURI = KeyAndURLExtractor.API_PREVIEW_ORDER_URI;
        var previewOrderResponse = new PreviewOrderResponse();

        var pathParameters = new PathParameters("accountIdKey", accountIdKey);
        var bodyParameter = new BodyParameter("PreviewOrderRequest", previewOrder.getOrderRequest());

        try {
            sendAPIPostRequest(requestURI, pathParameters, bodyParameter, previewOrderResponse);
            apiLogger.log("Preview order request successful");
            apiLogger.log("Response", previewOrderResponse.toIndentedString());

            return previewOrderResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not send the preview order request.");
            throw new NetworkException("Could not send the preview order request.", ex);
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


    private OrdersResponse getOrders(String accountIdKey, QueryParameters queryParameters) throws NetworkException {
        String requestURI = KeyAndURLExtractor.API_LIST_ORDERS_URI;
        var ordersResponse = new OrdersResponse();

        var pathParameters = new PathParameters("accountIdKey", accountIdKey);

        try {
            sendAPIGetRequest(requestURI, pathParameters, queryParameters, ordersResponse);
            apiLogger.log("Orders fetched successfully.");
            apiLogger.log("Response", ordersResponse.toString());

            return ordersResponse;
        }
        catch (OauthException ex) {
            apiLogger.log("Could not retrieve orders.");
            throw new NetworkException("Could not retrieve orders.", ex);
        }
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

    private static String formatDateMMDDYYYY(LocalDate date) {
        var formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        return formatter.format(date);
    }
}
