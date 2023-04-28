
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

/**
 * A mutable class that holds all the possible keys needed for an HTTP request
 * in the Oauth 1.0a model.
 */
public class OauthKeySet 
        implements Serializable {
    
    // Instance data fields
    public final Key consumerKey;
    public final Key consumerSecret;
    private Key token;
    private Key tokenSecret;
    private Key verifier;
    private boolean retrievedAccessToken = false;
    
    public OauthKeySet(Key consumerKey, Key consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = new Key();
        this.tokenSecret = new Key();
        this.verifier = new Key();
    }
    
    public OauthKeySet(Key consumerKey, Key consumerSecret, Key token, Key tokenSecret) {
        this(consumerKey, consumerSecret);
        this.token = token;
        this.tokenSecret = tokenSecret;
        retrievedAccessToken = true;
    }
    
    public void setToken(Key token, Key tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        retrievedAccessToken = true;
    }
    
    public void setVerifier(Key verifier) {
        this.verifier = verifier;
    }
    
    public Key getToken() {
        return token;
    }
    
    public Key getTokenSecret() {
        return tokenSecret;
    }
    
    public Key getVerifier() {
        return verifier;
    }
    
    public boolean hasRetrievedAToken() {
        return retrievedAccessToken;
    }
}
