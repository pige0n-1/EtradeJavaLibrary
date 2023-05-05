
package blue.etradeJavaLibrary.core.network.oauth.model;


import java.util.Arrays;
import java.util.Iterator;

/**
 * This child class of Parameters is used to represent parameters in the OAuth
 * 1.0a model that are located in the path section of a URL. The URLBuilder
 * class takes in an object of this class and a BaseURL object, as well as
 * QueryParameters, to construct a full URL object. The BaseURL object must have
 * all the names of the path parameters embedded in it, with each one being
 * surrounded with curly brackets The corresponding PathParameters object would
 * have a parameter with a key with the same name. These must match exactly,
 * or an exception will be thrown.
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
     * Creates a PathParameters with one parameter
     * @param key
     * @param value
     */
    public PathParameters(String key, long value) {
        addParameter(key, value + "");
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

    public PathParameters(String key1, long value1, String key2, long value2) {
        addParameter(key1, value1);
        addParameter(key2, value2);
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
     * Adds the specified key and value
     * @param key
     * @param value
     */
    public void addParameter(String key, long value) {
        addParameter(key, value + "");
    }

    /**
     * Adds the key and the string value of the object passed
     * @param key
     * @param value
     */
    public void addParameter(String key, Object value) {
        addParameter(key, value.toString());
    }
}
