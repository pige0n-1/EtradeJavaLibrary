
package blue.etradeJavaLibrary.core.network.oauth.model;


/**
 * This class represents parameters in the OAuth 1.0a model that reside in the 
 * query portion of a URL. There is an optional data field to turn of RFC3986
 * encoding, but it is on be default.
 */
public class QueryParameters extends Parameters {
    
    public QueryParameters() {}
    
    /**
     * Creates a new QueryParameters object, which parameters can either be
     * encoded or unencoded, according to the RFC3986 specification.
     * @param rfc3986Encoded 
     */
    public QueryParameters(boolean rfc3986Encoded) {
        super(rfc3986Encoded);
    }
    
    public void addParameter(String key, Key value) {
        addParameter(key, value.getValue());
    }
}
