
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

/**
 * A mutable class that holds all the possible keys needed for an HTTP request
 * in the Oauth 1.0a model
 */
public class OauthKeySet 
        implements Serializable {
    
    // Instance data fields
    /**
     * The consumer key that is needed in the Oauth model
     */
    public final Key consumerKey;
    
    /**
     * The consumer secret key that is needed in the Oauth model
     */
    public final Key consumerSecret;
    
    /**
     * The oauth token that is needed in the Oauth model
     */
    private Key token;
    
    /**
     * The token secret that is needed in the Oauth model
     */
    private Key tokenSecret;
    
    /**
     * Holds if the token or token secret have been set
     */
    private boolean retrievedAccessToken = false;
    
    /**
     * Constructs an OauthKeySet with a consumer key and consumer secret
     * @param consumerKey
     * @param consumerSecret 
     */
    public OauthKeySet(Key consumerKey, Key consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = new Key();
        this.tokenSecret = new Key();
    }
    
    /**
     * Constructs an OauthKeySet with all the keys needed
     * @param consumerKey
     * @param consumerSecret
     * @param token
     * @param tokenSecret 
     */
    public OauthKeySet(Key consumerKey, Key consumerSecret, Key token, Key tokenSecret) {
        this(consumerKey, consumerSecret);
        this.token = token;
        this.tokenSecret = tokenSecret;
        retrievedAccessToken = true;
    }
    
    /**
     * Sets the token and token secret keys
     * @param token
     * @param tokenSecret 
     */
    public void setToken(Key token, Key tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        retrievedAccessToken = true;
    }
    
    /**
     * Returns the token, if it has been set
     * @return 
     */
    public Key getToken() {
        return token;
    }
    
    /**
     * Returns the token secret, if it has been set
     * @return 
     */
    public Key getTokenSecret() {
        return tokenSecret;
    }
    
    /**
     * Returns true if the token or token secret have been set
     * @return 
     */
    public boolean hasRetrievedAToken() {
        return retrievedAccessToken;
    }
    
    /**
     * Removes the token and token secret from this object, and
     * changes retrievedAccessToken to false
     */
    public void removeToken() {
        this.token = new Key();
        this.tokenSecret = new Key();
        retrievedAccessToken = false;
    }
}
