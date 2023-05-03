
package blue.etradeJavaLibrary.core.network.oauth.model;


import blue.etradeJavaLibrary.core.logging.ProgramLogger;

/**
 * This child class of Parameters is used to represent parameters in the OAuth
 * 1.0a model that are located in the path section of a URL. The URLBuilder
 * class takes in an object of this class and a BaseURL object, as well as
 * QueryParameters, to construct a full URL object. The BaseURL object must have
 * all the names of the path parameters embedded in it, with each one being
 * surrounded with curly brackets 
 * (example: "https://etrade.com/accounts/{accountNumber}").
 * The corresponding PathParameters object would have a parameter with a key
 * called "accountNumber". These must match exactly, or an exception will be
 * thrown.
 */
public class PathParameters extends Parameters {
    
    /**
     * Constructs an empty PathParameters object
     */
    public PathParameters() {}
    
    /**
     * A convenient constructor for a PathParameters that starts
     * out with one parameter
     * @param key
     * @param value 
     */
    public PathParameters(String key, String value) {
        addParameter(key, value);
    }
    
    /**
     * A convenient constructor for a PathParameters that starts
     * with two parameters
     * @param key1
     * @param value1
     * @param key2
     * @param value2 
     */
    public PathParameters(String key1, String value1, String key2, String value2) {
        addParameter(key1, value1);
        addParameter(key2, value2);
    }
}
