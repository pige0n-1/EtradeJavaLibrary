
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.util.Base64;

/**
 * This Parameters child class contains all OAuth authorization parameters, i.e. any parameter starting with "oauth_" in
 * the model. Most of these parameters are added automatically upon instantiation, but some cannot be added at that
 * point. Use any of the setter methods to add additional needed parameters, and they will be added and automatically
 * encoded. If needing to resend a request with the same OauthParameters object, be sure to call the
 * resetNonceAndTimestamp() method, so the request will not be rejected by the service provider for re-using a nonce.
 * If using a no-arg constructor, no parameters will be added automatically. Call configure() method to initialize
 * all parameters that can be added automatically. The no-arg constructor would not be needed traditionally; however,
 * it becomes useful when creating subclasses of OauthParameters and passing an object of that class to a method that
 * configures any OauthParameters child object. Before using this class, be sure to consult your service provider
 * for specifically what Oauth parameters are needed in every request. The following parameters are added automatically
 * upon instantiation with an argument-constructor or with configure():
 * oauth_nonce
 * oauth_timestamp
 * oauth_version.
 * oauth_signature_method
 * The signature method can be changed by calling setSignatureMethod(), but it is set by default for convenience.
 */
public class OauthParameters extends Parameters {
    
    /**
     * Constructs an OauthParameters object without any keys or values. Call configure() to add all the
     * Oauth parameters that can be automatically added.
     */
    public OauthParameters() {}
    
    /**
     * Constructs an OauthParameters object with a consumer key. The
     * object is initialized with several other Oauth parameters that
     * can be automatically generated.
     * @param consumerKey 
     */
    public OauthParameters(Key consumerKey) {
        configure(consumerKey);
    }
    
    /**
     * Constructs an OauthParameters object with a consumer key and a token
     * @param consumerKey
     * @param token 
     */
    public OauthParameters(Key consumerKey, Key token) {
        this(consumerKey);
        addToken(token);
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
     * This method adds all Oauth parameters that can be added automatically. See the OauthParameters class javadoc for
     * details.
     */
    public void configure() {
        initializeParameters();
    }

    /**
     * This method adds all Oauth parameters that can be added automatically, with addition of oauth_consumer_key. See
     * the OauthParameters class javadoc for more details.
     * @param consumerKey
     */
    public void configure(Key consumerKey) {
        configure();
        setConsumerKey(consumerKey);
    }

    /**
     * This method adds all Oauth parameters that can be added automatically, with addition of oauth_consumer_key and
     * oauth_token. See the OauthParameters class javadoc for more details.
     * @param consumerKey
     * @param token
     */
    public void configure(Key consumerKey, Key token) {
        configure(consumerKey);
        addToken(token);
    }

    /**
     * Adds all Oauth parameters that can be added automatically, with addition of oauth_consumer_key, oauth_token, and
     * oauth_verifier. See the OauthParameters class javadoc for more details.
     * @param consumerKey
     * @param token
     * @param verifier
     */
    public void configure(Key consumerKey, Key token, Key verifier) {
        configure(consumerKey, token);
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
    public void addToken(Key token) {
        addParameter("oauth_token", token.getValue());
    }

    /**
     * Sets the oauth_signature_method parameter
     * @param signatureMethod
     */
    public void addSignatureMethod(String signatureMethod) {
        addParameter("oauth_signature_method", signatureMethod);
    }
    
    /**
     * Adds the optional oauth_verifier parameter
     * @param verifier 
     */
    public void addVerifier(Key verifier) {
        addParameter("oauth_verifier", verifier.getValue());
    }

    /**
     * Adds the oauth_callback parameter
     * @param callbackURL
     */
    public void addCallback(String callbackURL) {
        addParameter("oauth_callback", callbackURL);
    }

    @Override
    /**
     * Adds the parameter to the collection, but ensures that it has not already been added. The method first
     * removes any identical parameters, and then re-adds it.
     */
    public void addParameter(String key, String value) {
        key = ensureKeyStartsWithOauth_(key);

        removeParameter(key);
        super.addParameter(key, value);
    }
    
    /**
     * If a request needs to be re-sent, the nonce needs to be set again.
     * This method renews the timestamp and changes the nonce.
     */
    public void resetNonceAndTimestamp() {
        addTimestamp();
        addParameter("oauth_nonce", generateNonce());
    }
    
    
    // Private helper methods


    private void initializeParameters() {
        addParameter("oauth_signature_method", "HMAC-SHA1");
        addParameter("oauth_nonce", generateNonce());
        addParameter("oauth_version", "1.0");
        addTimestamp();
    }
    
    private void addTimestamp() {
        addParameter("oauth_timestamp", System.currentTimeMillis() / 1000 + "");
    }
    
    private String generateNonce() {
        byte[] rawRandomNumberString = ((int)(this.hashCode() * Math.random()) + "").getBytes();
        String base64String = Base64.getEncoder().encodeToString(rawRandomNumberString);
        return Rfc3986.encode(base64String);
    }

    private String ensureKeyStartsWithOauth_(String key) {
        if (key.startsWith("oauth_")) return key;

        else return "oauth_" + key;
    }
}
