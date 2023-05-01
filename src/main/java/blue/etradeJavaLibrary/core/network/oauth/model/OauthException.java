
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.IOException;

/**
 * This exception represents a general problem in any part of the OAuth process.
 */
public class OauthException extends IOException {
    
    /**
     * Constructs an OauthException
     */
    public OauthException() {}
    
    /**
     * Constructs an OauthException with a message
     * @param message 
     */
    public OauthException(String message) {
        super(message);
    }
    
    /**
     * Constructs a chained OauthException
     * @param ex 
     */
    public OauthException(Exception ex) {
        super(ex);
    }
    
    /**
     * Constructs a chained OauthException with a message
     * @param message
     * @param ex 
     */
    public OauthException(String message, Exception ex) {
        super(message, ex);
    }
}
