
package blue.etradeJavaLibrary.core;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;

public class KeyAndURLExtractor {
    
    public static final String OAUTH_BASE_URL = "https://api.etrade.com";
    public static final String OAUTH_AUTHORIZATION_BASE_URL = "https://us.etrade.com/e/t/etws/authorize";
    public static final String OAUTH_ACCESS_TOKEN_URI = "/oauth/access_token";
    public static final String OAUTH_REQUEST_TOKEN_URI = "/oauth/request_token";
    public static final String OAUTH_RENEW_ACCESS_TOKEN_URI = "/oauth/renew_access_token";
    public static final String OAUTH_REVOKE_ACCESS_TOKEN_URI = "/oauth/revoke_access_token";
    public static final String API_BASE_URL = "https://api.etrade.com";
    public static final String API_ACCOUNT_LIST_URI = "/v1/accounts/list";
    public static final String API_BALANCE_URI = "/v1/accounts/";
    public static final String API_PORTFOLIO_URI = "/v1/accounts/";
    public static final String API_QUOTE_URI = "/v1/market/quote/";
    public static final String API_ACCOUNTS_URI = "/v1/accounts/";
    public static final String API_ORDER_URI = "/v1/accounts/";
    public static final String SANDBOX_BASE_URL = "https://apisb.etrade.com";
     
    public static Key getConsumerKey() {
        String keyString = extractSystemEnvironmentVariable("etradeConsumerKey");
        return new Key(keyString);
    }
    
    public static Key getConsumerSecret() {
        String keyString = extractSystemEnvironmentVariable("etradeConsumerSecret");
        return new Key(keyString);
    }
    
    public static Key getSandboxConsumerKey() {
        String keyString = extractSystemEnvironmentVariable("etradeSandboxConsumerKey");
        return new Key(keyString);
    }
    
    public static Key getSandboxConsumerSecret() {
        String keyString = extractSystemEnvironmentVariable("etradeSandboxConsumerSecret");
        return new Key(keyString);
    }
    
    public static String getEtradeUsername() {
        return extractSystemEnvironmentVariable("etradeUsername");
    }
    
    public static String getEtradePassword() {
        return extractSystemEnvironmentVariable("etradePassword");
    }
    
    
    // Private helper methods
    
    
    private static String extractSystemEnvironmentVariable(String identifier) {
        String value = System.getenv(identifier);
        
        if (value == null || value.equals(""))
            ProgramLogger.getNetworkLogger().log("The following environment variable is empty", identifier);
        
        return value;
    }
}
