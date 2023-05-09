
package blue.etradeJavaLibrary.core.network.oauth.model;


/**
 * This class represents parameters in the OAuth 1.0a model that reside in the 
 * query portion of a URL. There is an optional data field to turn of RFC3986
 * encoding, but it is on by default.
 */
public class QueryParameters extends Parameters {
    
    /**
     * Constructs an empty QueryParameters object that is RFC3986 encoded
     */
    public QueryParameters() {}
    
    /**
     * Creates a new QueryParameters object, which parameters can either be
     * encoded or unencoded, according to the RFC3986 specification.
     * @param rfc3986Encoded 
     */
    public QueryParameters(boolean rfc3986Encoded) {
        super(rfc3986Encoded);
    }
    
    /**
     * Adds a key and value pair to the parameters collection
     * @param key
     * @param value 
     */
    public void addParameter(String key, Key value) {
        addParameter(key, value.getValue());
    }

    /**
     * Adds a key and value pair to the parameters collection
     * @param key
     * @param value
     */
    public void addParameter(String key, int value) {
        addParameter(key, value + "");
    }

    /**
     * Adds a key and value pair to the parameters collection. The value is either "true" or "false"
     * @param key
     * @param value
     */
    public void addParameter(String key, boolean value) {
        var valueString = (value) ? "true" : "false";
        addParameter(key, valueString);
    }
}
