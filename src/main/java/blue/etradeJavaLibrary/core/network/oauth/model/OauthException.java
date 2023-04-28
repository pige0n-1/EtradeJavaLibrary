
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.IOException;

/**
 * This exception represents a general problem in any part of the OAuth process.
 * @author Hunter
 */
public class OauthException extends IOException {
    
    public OauthException() {}
    
    public OauthException(String message) {
        super(message);
    }
}
