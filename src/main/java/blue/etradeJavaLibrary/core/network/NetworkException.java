
package blue.etradeJavaLibrary.core.network;


public class NetworkException extends Exception {
    
    public NetworkException() {}
    
    public NetworkException(String message) {
        super(message);
    }
    
    public NetworkException(String message, Exception ex) {
        super(message, ex);
    }
}
