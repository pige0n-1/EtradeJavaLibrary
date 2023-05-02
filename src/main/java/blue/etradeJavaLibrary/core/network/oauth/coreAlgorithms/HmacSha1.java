
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import blue.etradeJavaLibrary.core.network.oauth.model.InvalidParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Contains the functionality for an Hmac-SHA1 operation within the oauth 1.0a
 * model. The digest is encoded in base64.
 * @author Hunter
 */
public class HmacSha1 {
    
    // Static data fields
    private static final String ALGORITHM = "HmacSHA1";

    /* Prevent instantiation */
    private HmacSha1() {}

    /**
     * Returns the digest of an HMAC-SHA1 operation
     * @param key
     * @param message
     * @return
     */
    public static String doHmacSha1Base64(String key, String message) {
        try {
            byte[] keyBytes = key.getBytes();
            Key keyObject = new SecretKeySpec(keyBytes, ALGORITHM);
            
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(keyObject);
            byte[] digest = mac.doFinal(message.getBytes());
            
            String base64Digest = Base64.getEncoder().encodeToString(digest);
            return base64Digest;
        }
        catch (InvalidKeyException ex) {
            throw new InvalidParameterException("Invalid key for producting HMAC-SHA1 digest.");
        }
        catch (IllegalStateException | NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
