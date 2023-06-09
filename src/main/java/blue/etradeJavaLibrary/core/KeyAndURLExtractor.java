
package blue.etradeJavaLibrary.core;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;

/**
 * Utility class for the E*Trade Java Library. All keys that are needed for the Etrade Oauth authentication flow and
 * URLs that are needed can be extracted from this class. This class cannot be instantiation.
 */
public class KeyAndURLExtractor {
    
    public static final String LIVE_BASE_URL = "https://api.etrade.com";
    public static final String SANDBOX_BASE_URL = "https://apisb.etrade.com";
    public static final String OAUTH_VERIFIER_BASE_URL = "https://us.etrade.com/e/t/etws/authorize";
    public static final String OAUTH_ACCESS_TOKEN_URI = "/oauth/access_token";
    public static final String OAUTH_REQUEST_TOKEN_URI = "/oauth/request_token";
    public static final String OAUTH_RENEW_ACCESS_TOKEN_URI = "/oauth/renew_access_token";
    public static final String OAUTH_REVOKE_ACCESS_TOKEN_URI = "/oauth/revoke_access_token";
    public static final String API_ACCOUNT_LIST_URI = "/v1/accounts/list";
    public static final String API_ACCOUNT_BALANCE_URI = "/v1/accounts/{accountIdKey}/balance";
    public static final String API_LIST_TRANSACTIONS_URI = "/v1/accounts/{accountIdKey}/transactions";
    public static final String API_TRANSACTION_DETAILS_URI = "/v1/accounts/{accountIdKey}/transactions/{transactionId}";
    public static final String API_VIEW_PORTFOLIO_URI = "/v1/accounts/{accountIdKey}/portfolio";
    public static final String API_VIEW_ALERTS_URI = "/v1/user/alerts";
    public static final String API_ALERT_DETAILS_URI = "/v1/user/alerts/{id}";
    public static final String API_DELETE_ALERTS_URI = "/v1/user/alerts/{idList}";
    public static final String API_GET_QUOTES_URI = "/v1/market/quote/{symbols}";
    public static final String API_LOOK_UP_PRODUCT_URI = "/v1/market/lookup/{search}";
    public static final String API_GET_OPTION_CHAINS_URI = "/v1/market/optionchains";
    public static final String API_GET_OPTION_EXPIRE_DATE_URI = "/v1/market/optionexpiredate";
    public static final String API_LIST_ORDERS_URI = "/v1/accounts/{accountIdKey}/orders";
    public static final String API_PREVIEW_ORDER_URI = "/v1/accounts/{accountIdKey}/orders/preview";

    /* Prevent instantiation */
    private KeyAndURLExtractor() {}

    /**
     * Returns the consumer key that is stored in the user environment variable 'etradeConsumerKey'
     * @return
     */
    public static Key getConsumerKey() {
        String keyString = extractSystemEnvironmentVariable("etradeConsumerKey");

        if (keyString == null)
            throw new RuntimeException("No consumer key was found in an environment variable");

        return new Key(keyString);
    }

    /**
     * Returns the consumer secret that is stored in the user environment variable 'etradeConsumerSecret'
     * @return
     */
    public static Key getConsumerSecret() {
        String keyString = extractSystemEnvironmentVariable("etradeConsumerSecret");

        if (keyString == null)
            throw new RuntimeException("No consumer secret was found in an environment variable");

        return new Key(keyString);
    }

    /**
     * Returns the sandbox consumer key that is stored in the user environment variable 'etradeSandboxConsumerKey'
     * @return
     */
    public static Key getSandboxConsumerKey() {
        String keyString = extractSystemEnvironmentVariable("etradeSandboxConsumerKey");
        return new Key(keyString);
    }

    /**
     * Returns the sandbox consumer secret that is stored in the user environment variable 'etradeSandboxConsumerSecret'
     * @return
     */
    public static Key getSandboxConsumerSecret() {
        String keyString = extractSystemEnvironmentVariable("etradeSandboxConsumerSecret");
        return new Key(keyString);
    }
    
    
    // Private helper methods
    
    
    private static String extractSystemEnvironmentVariable(String identifier) {
        String value = System.getenv(identifier);
        
        if (value == null || value.equals(""))
            ProgramLogger.getNetworkLogger().log("The following environment variable is empty", identifier);
        
        return value;
    }
}
