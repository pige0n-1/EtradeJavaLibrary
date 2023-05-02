
package blue.etradeJavaLibrary.core.network;

/**
 * An exception that is thrown whenever there is a general connection problem to an API
 */
public class NetworkException extends Exception {

    /**
     * Constructs a default NetworkException
     */
    public NetworkException() {}

    /**
     * Constructs a NetworkException with a message
     * @param message
     */
    public NetworkException(String message) {
        super(message);
    }

    /**
     * Constructs a chained NetworkException with a message
     * @param message
     * @param ex
     */
    public NetworkException(String message, Exception ex) {
        super(message, ex);
    }
}
