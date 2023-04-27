
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.util.Base64;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;

public final class OauthParameters extends Parameters {
    
    public OauthParameters(Key consumerKey) {
        initializeParameters();
        setConsumerKey(consumerKey);
    }
    
    public OauthParameters(Key consumerKey, Key token) {
        this(consumerKey);
        setToken(token);
    }
    
    public OauthParameters(Key consumerKey, Key token, Key verifier) {
        this(consumerKey, token);
        addVerifier(verifier);
    }
    
    public void setSignature(String signature) {
        addParameter("oauth_signature", signature);
    }
    
    public void setConsumerKey(Key consumerKey) {
        addParameter("oauth_consumer_key", consumerKey.getValue());
    }
    
    public void setToken(Key token) {
        addParameter("oauth_token", token.getValue());
    }
    
    public void addVerifier(Key verifier) {
        addParameter("oauth_verifier", verifier.getValue());
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
        addParameter("oauth_timestamp", System.currentTimeMillis() / 1000 + "");
    }
    
    private String generateNonce() {
        byte[] rawRandomNumberString = ((int)(this.hashCode() * Math.random()) + "").getBytes();
        String base64String = Base64.getEncoder().encodeToString(rawRandomNumberString);
        return Rfc3986.encode(base64String);
    }
}
