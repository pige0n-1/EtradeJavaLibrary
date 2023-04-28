
package blue.etradeJavaLibrary.core.network.oauth.model;


/**
 * This exception should be thrown in the Oauth model whenever a general
 * parameter is passed to a method that does not meet proper specifications.
 */
public class InvalidParameterException extends RuntimeException {
    
    public InvalidParameterException() {}
    
    public InvalidParameterException(String message) {
        super(message);
    }
}
