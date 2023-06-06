
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is a general class to represent any kind of parameters in the OAuth 1.0a
 * model. There are three subclasses of this class to represent the three types
 * of parameters in the model: PathParameters, QueryParameters, and
 * OauthParameters. The data structure is mapped as a key-value pair. The 
 * boolean data field "rfc3986Encoded" is a public option to percent-encode
 * all the parameters. This is marked "true" by default to comply with the
 * standards, but there are times when it may be convenient to pass unencoded
 * parameters back to the user as a response from an HTTP request.
 */
public class Parameters implements Iterable<Parameters.Parameter> {
    
    // Instance data fields
    private final TreeMap<String, String> parameters = new TreeMap<>();
    private final boolean rfc3986Encoded;
    
    /**
     * Constructs an empty Parameters object that is RFC3986 encoded
     */
    public Parameters() {
        rfc3986Encoded = true;
    }
    
    /**
     * Constructs an empty Parameters object that can either be encoded
     * or unencoded.
     * @param rfc3986Encoded 
     */
    public Parameters(boolean rfc3986Encoded) {
        this.rfc3986Encoded = rfc3986Encoded;
    }
    
    /**
     * Adds the key and value pair to the collection
     * @param key
     * @param value 
     */
    public void addParameter(String key, String value) {
        if (rfc3986Encoded) parameters.put(encode(key), encode(value));
        
        else parameters.put(key, value);
    }
    
    /**
     * Removes the parameter with the specified key
     * @param key 
     */
    public void removeParameter(String key) {
        parameters.remove(key);
    }
    
    /**
     * Retrieves the value with the specified key
     * @param key
     * @return 
     */
    public String getValue(String key) {
        return parameters.get(key);
    }
    
    /**
     * Returns true if there are no parameters in the collection
     * @return 
     */
    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
    /**
     * Merges all parameters that are passed as arguments into one Parameters
     * object. If one Parameters object is encoded, it will stay encoded,
     * while the others will stay unencoded.
     * @param parametersCollection an arbitrary-length argument of Parameters objects
     * @return 
     */
    public static Parameters merge(Parameters... parametersCollection) {
        ensureNoParametersAreNull(parametersCollection);
        
        Parameters allParameters = new Parameters(false);
        
        for (Parameters currentParameters : parametersCollection)
            for (Parameter currentParameter : currentParameters)
                allParameters.addParameter(currentParameter.getKey(), currentParameter.getValue());
        
        return allParameters;
    }
    
    @Override
    public String toString() {
        StringBuilder parametersString = new StringBuilder("{");
        Iterator<Parameter> iterator = this.iterator();
        
        while (iterator.hasNext()) {
            Parameter parameter = iterator.next();
            
            parametersString.append(parameter.getKey())
                    .append("=")
                    .append(parameter.getValue());
            
            if (iterator.hasNext()) parametersString.append(", ");
        }
        parametersString.append("}");
        
        return parametersString.toString();
    }
    
    protected String encode(String value) {
        return Rfc3986.encode(value);
    }
    
    @Override
    /**
     * Returns an Iterator for this object
     */
    public Iterator<Parameter> iterator() {
        return new ParametersIterator();
    }
    
    /**
     * A simple extension of Iterator to iterate through a Parameters object
     */
    public class ParametersIterator implements Iterator<Parameter> {
        Iterator<Map.Entry<String, String>> treeMapIterator = parameters.entrySet().iterator();
        
        @Override
        public Parameter next() {
            Map.Entry<String, String> currentEntry = treeMapIterator.next();
            return new Parameter(currentEntry.getKey(), currentEntry.getValue());
        }
        
        @Override
        public boolean hasNext() {
            return treeMapIterator.hasNext();
        }
    }
    
    /**
     * Represents a single parameter in the Parameters class
     */
    public class Parameter {
        private final String key;
        private final String value;

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
    
    
    // Private helper methods
    
    
    private static void ensureNoParametersAreNull(Parameters... parametersCollection) {
        for (Parameters parameters : parametersCollection) {
            if (parameters == null)
                throw new InvalidParameterException("No Parameters object can be null in the merge process");
        }
    }
}
