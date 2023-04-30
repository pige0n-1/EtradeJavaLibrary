
package blue.etradeJavaLibrary.core.network.oauth.responses;


/**
 * This exception is thrown whenever a org.w3c.dom.Document object is 
 * attempted to be parsed into an incompatible subclass of XMLDefinedObject.
 * For example, if a call to the Etrade API is made to retrieve an account,
 * and an Account XML document is returned, but the user tries to parse it
 * into a Stock object, this exception would be thrown. In this example,
 * both Stock and Account are subclasses of XMLDefinedObject, so they
 * implement the processXMLDocument, a method that throws this exception
 * @author Hunter
 */
public class ObjectMismatchException extends Exception {
    
    public ObjectMismatchException() {}
    
    public ObjectMismatchException(String message) {
        super(message);
    }
    
    public ObjectMismatchException(String message, Exception ex) {
        super(message, ex);
    }
    
    public ObjectMismatchException(Exception ex) {
        super(ex);
    }
}
