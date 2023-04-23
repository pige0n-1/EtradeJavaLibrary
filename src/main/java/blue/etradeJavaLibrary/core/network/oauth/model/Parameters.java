
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;

public class Parameters implements Iterable<Parameters.Parameter> {
    
    // Data fields
    private final TreeMap<String, String> parameters = new TreeMap<>();
    
    public void replaceParameter(String key, String value) throws 
            OauthException {
        parameters.replace(encode(key), encode(value));
    }
    
    public void addParameter(String key, String value) throws 
            OauthException {
        parameters.put(encode(key), encode(value));
    }
    
    public Parameter getDecodedParameter(String key) throws OauthException {
        String value = parameters.get(key);
        
        if (value == null)
            throw new OauthException("No such parameter");
        
        value = Rfc3986.decode(value);
        
        return new Parameter(key, value);
    }
    
    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
    public static Parameters merge(Parameters... parametersCollection) {
        Parameters allParameters = new Parameters();
        
        for (Parameters currentParameters : parametersCollection) {
            for (Parameter currentParameter : currentParameters) {
                allParameters.addParameterWithoutEncoding(currentParameter.getKey(), currentParameter.getValue());
            }
        }
        
        return allParameters;
    }
    
    protected String addParameterWithoutEncoding(String key, String value) {
        return parameters.put(key, value);
    }
    
    @Override
    public String toString() {
        StringBuilder parametersString = new StringBuilder("{");
        Iterator<Parameter> iterator = this.iterator();
        
        while (iterator.hasNext()) {
            Parameter parameter = iterator.next();
            
            parametersString.append(parameter.getDecodedKey());
            parametersString.append("=");
            parametersString.append(parameter.getDecodedValue());
            
            if (iterator.hasNext())
                parametersString.append(", ");
        }
        parametersString.append("}");
        
        return parametersString.toString();
    }
    
    private String encode(String value) {
        return Rfc3986.encode(value);
    }
    
    @Override
    public Iterator<Parameter> iterator() {
        return new ParametersIterator();
    }
    
    private class ParametersIterator implements Iterator<Parameter> {
        Iterator<Map.Entry<String, String>> treeMapIterator = 
                parameters.entrySet().iterator();
        
        @Override
        public Parameter next() {
            Map.Entry<String, String> currentEntry = treeMapIterator.next();
            return new Parameter(currentEntry.getKey(), 
                    currentEntry.getValue());
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

        public String getDecodedKey() {
            return Rfc3986.decode(key);
        }

        public String getValue() {
            return value;
        }

        public String getDecodedValue() {
            return Rfc3986.decode(value);
        }
    }
}
