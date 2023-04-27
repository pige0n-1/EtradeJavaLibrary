
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;

public class Parameters implements Iterable<Parameters.Parameter> {
    
    // Data fields
    private final TreeMap<String, String> parameters = new TreeMap<>();
    private final boolean rfc3986Encoded;
    
    public Parameters() {
        rfc3986Encoded = true;
    }
    
    public Parameters(boolean rfc3986Encoded) {
        this.rfc3986Encoded = rfc3986Encoded;
    }
    
    public void addParameter(String key, String value) {
        if (rfc3986Encoded)
            parameters.put(encode(key), encode(value));
        else
            parameters.put(key, value);
    }
    
    public void removeParameter(String key) {
        parameters.remove(key);
    }
    
    public String getValue(String key) {
        return parameters.get(key);
    }
    
    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
    public static Parameters merge(Parameters... parametersCollection) {
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
            
            parametersString.append(parameter.getKey());
            parametersString.append("=");
            parametersString.append(parameter.getValue());
            
            if (iterator.hasNext())
                parametersString.append(", ");
        }
        parametersString.append("}");
        
        return parametersString.toString();
    }
    
    protected String encode(String value) {
        return Rfc3986.encode(value);
    }
    
    @Override
    public Iterator<Parameter> iterator() {
        return new ParametersIterator();
    }
    
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
}
