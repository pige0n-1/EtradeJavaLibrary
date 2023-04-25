
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.util.Base64;

public final class OauthParameters extends Parameters {
    
    private boolean signatureSet = false;
    
    public OauthParameters(Key consumerKey) throws OauthException {
        initializeParameters();
        setConsumerKey(consumerKey);
    }
    
    public OauthParameters(Key consumerKey, Key token) throws OauthException {
        this(consumerKey);
        setToken(token);
    }
    
    public OauthParameters(Key consumerKey, Key token, Key verifier) throws OauthException {
        this(consumerKey, token);
        addVerifier(verifier);
    }
    
    
    // Private helper methods
    
    
    private void initializeParameters() throws OauthException {
        addParameter("oauth_signature_method", "HMAC-SHA1");
        addParameter("oauth_nonce", generateNonce());
        addParameter("oauth_version", "1.0");
        addParameter("oauth_callback", "oob");
        createTimestamp();
    }
    
    private void createTimestamp() throws OauthException {
        addParameter("oauth_timestamp", System.currentTimeMillis() / 1000 + "");
    }
    
    private String generateNonce() {
        byte[] rawRandomNumberString = ((int)(this.hashCode() * Math.random()) + "").getBytes();
        return Base64.getEncoder().encodeToString(rawRandomNumberString);
    }
    
    public void setSignature(String signature) throws OauthException {
        if (signatureSet)
            throw new OauthException("The signature cannot be"
                    + "set twice");

        addParameter("oauth_signature", signature);
        
        signatureSet = true;
    }
    
    public void setConsumerKey(Key consumerKey) throws OauthException {
        addParameterWithoutEncoding("oauth_consumer_key", consumerKey.getValue());
    }
    
    public void setToken(Key token) throws OauthException {
        addParameterWithoutEncoding("oauth_token", token.getValue());
    }
    
    public void addVerifier(Key verifier) throws OauthException {
        addParameterWithoutEncoding("oauth_verifier", verifier.getValue());
    }
}
