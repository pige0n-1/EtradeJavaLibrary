
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import java.io.Serializable;

public class Key implements Serializable {
    private String value;
    
    public Key(String value) {
        this.value = Rfc3986.encode(value);
    }
    
    public Key() {
        value = "";
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "Key: " + getValue();
    }
}
