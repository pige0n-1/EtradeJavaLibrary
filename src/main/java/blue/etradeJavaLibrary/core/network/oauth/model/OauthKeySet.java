
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

/**
 * A mutable class that holds all the possible keys needed for an HTTP request
 * in the Oauth 1.0a model.
 */
public class OauthKeySet 
        implements Serializable {
    
    // Instance data fields
    public Key consumerKey;
    public Key consumerSecret;
    public Key token;
    public Key tokenSecret;
    public Key verifier;
    
    public OauthKeySet(Key consumerKey, Key consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }
    
    public OauthKeySet(Key consumerKey, Key consumerSecret, Key token, Key tokenSecret) {
        this(consumerKey, consumerSecret);
        this.token = token;
        this.tokenSecret = tokenSecret;
    }
    
    public OauthKeySet(Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, Key verifier) {
        this(consumerKey, consumerSecret, token, tokenSecret);
        this.verifier = verifier;
    }
}
