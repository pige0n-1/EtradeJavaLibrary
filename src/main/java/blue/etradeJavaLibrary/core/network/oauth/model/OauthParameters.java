
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.util.Base64;

/**
 * This Parameters child class contains all of the OAuth authorization
 * parameters, i.e. any parameter starting with "oauth_" in the model.
 * Most of these parameters are added automatically upon instantiation, but
 * some cannot be added at that point. Use any of the setter methods to add 
 * additional needed parameters, and they will be added and automatically 
 * encoded. If needing to reuse the parameters, be sure to call the
 * resetNonceAndTimestamp method
 */
public final class OauthParameters extends Parameters {
    
    /**
     * Constructs an OauthParameters object without any keys. The
     * Parameters are initialized with parameters that are added
     * automatically.
     */
    public OauthParameters() {
        initializeParameters();
    }
    
    /**
     * Constructs an OauthParameters object with a consumer key. The
     * object is initialized with several other Oauth parameters that
     * can be automatically generated.
     * @param consumerKey 
     */
    public OauthParameters(Key consumerKey) {
        initializeParameters();
        setConsumerKey(consumerKey);
    }
    
    /**
     * Constructs an OauthParameters object with a consumer key and a token
     * @param consumerKey
     * @param token 
     */
    public OauthParameters(Key consumerKey, Key token) {
        this(consumerKey);
        setToken(token);
    }
    
    /**
     * Constructs an OauthParameters object with a consumer key, token, and verifier
     * @param consumerKey
     * @param token
     * @param verifier 
     */
    public OauthParameters(Key consumerKey, Key token, Key verifier) {
        this(consumerKey, token);
        addVerifier(verifier);
    }
    
    /**
     * Sets the oauth_signature parameter
     * @param signature 
     */
    public void setSignature(String signature) {
        addParameter("oauth_signature", signature);
    }
    
    /**
     * Sets the consumer key parameter
     * @param consumerKey 
     */
    public void setConsumerKey(Key consumerKey) {
        addParameter("oauth_consumer_key", consumerKey.getValue());
    }
    
    /**
     * Sets the oauth_token parameter
     * @param token 
     */
    public void setToken(Key token) {
        addParameter("oauth_token", token.getValue());
    }
    
    /**
     * Adds the optional oauth_verifier parameter
     * @param verifier 
     */
    public void addVerifier(Key verifier) {
        addParameter("oauth_verifier", verifier.getValue());
    }
    
    /**
     * If a request needs to be re-sent, the nonce needs to be set again.
     * This method renews the timestamp and changes the nonce.
     */
    public void resetNonceAndTimestamp() {
        createTimestamp();
        
        removeParameter("oauth_nonce");
        addParameter("oauth_nonce", generateNonce());
    }
    
    
    // Private helper methods
    
    
    private void initializeParameters() {
        addParameter("oauth_signature_method", "HMAC-SHA1");
        addParameter("oauth_nonce", generateNonce());
        addParameter("oauth_version", "1.0");
        addParameter("oauth_callback", "oob");
        createTimestamp();
    }
    
    private void createTimestamp() {
        removeParameter("oauth_timestamp");
        addParameter("oauth_timestamp", System.currentTimeMillis() / 1000 + "");
    }
    
    private String generateNonce() {
        byte[] rawRandomNumberString = ((int)(this.hashCode() * Math.random()) + "").getBytes();
        String base64String = Base64.getEncoder().encodeToString(rawRandomNumberString);
        return Rfc3986.encode(base64String);
    }
}
