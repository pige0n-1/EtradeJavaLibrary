
package blue.etradeJavaLibrary.core.network.oauth.model;


import java.util.Arrays;
import java.util.Iterator;

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
    public void addParameter(String key, long value) {
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

    /**
     * Adds the specified key and a list of values. The values are represented as follows:
     * "value1,value2,value3,...,valuex" where is x is the length of the values array.
     * @param key
     * @param values
     */
    public void addParameter(String key, long... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++)
            stringValues[i] = values[i] + "";

        addParameter(key, stringValues);
    }

    /**
     * Adds the specified key and a list of values. The values are represented as follows:
     * "value1,value2,value3,...,valuex" where is x is the length of the values array.
     * @param key
     * @param values
     */
    public void addParameter(String key, String... values) {
        StringBuilder valueString = new StringBuilder();

        Iterator<String> valuesIterator = Arrays.stream(values).iterator();
        while (valuesIterator.hasNext()) {
            valueString.append(valuesIterator.next());
            if (valuesIterator.hasNext()) valueString.append(",");
        }

        addParameter(key, valueString.toString());
    }
}
