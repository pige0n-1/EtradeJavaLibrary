
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Contains the functionality for an Hmac-SHA1 operation within the oauth 1.0a
 * model. The digest is encoded in base64.
 * @author Hunter
 */
public class HmacSha1 {
    private static final String ALGORITHM = "HmacSHA1";
    
    private HmacSha1() {}
    
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
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
