
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.OauthParameterException;
import java.util.Base64;

public final class OauthParameters extends Parameters {
    // Http request types
    public static final String GET = "GET";
    public static final String POST = "POST";
    
    // Private data fields
    private boolean signatureSet = false;
    
    public OauthParameters(Key consumerKey) throws OauthParameterException {
        initializeParameters(consumerKey);
    }
    
    public OauthParameters(Key consumerKey, Key token) throws OauthParameterException {
        this(consumerKey);
        setToken(token);
    }
    
    private void initializeParameters(Key consumerKey) throws OauthParameterException {
        addParameter("oauth_consumer_key", consumerKey.getValue());
        addParameter("oauth_signature_method", "HMAC-SHA1");
        addParameter("oauth_nonce", generateNonce());
        addParameter("oauth_version", "1.0");
        addParameter("oauth_callback", "oob");
        createTimestamp();
    }
    
    private void createTimestamp() throws OauthParameterException {
        addParameter("oauth_timestamp", System.currentTimeMillis() / 1000 + "");
    }
    
    private String generateNonce() {
        byte[] rawRandomNumberString = ((int)(this.hashCode() * Math.random()) + "").getBytes();
        return Base64.getEncoder().encodeToString(rawRandomNumberString);
    }
    
    public void setSignature(String signature) throws OauthParameterException {
        if (signatureSet)
            throw new OauthParameterException("The signature cannot be"
                    + "set twice");

        addParameter("oauth_signature", signature);
        
        signatureSet = true;
    }
    
    public void setToken(Key token) throws OauthParameterException {
        addParameter("oauth_token", token.getValue());
    }
}
