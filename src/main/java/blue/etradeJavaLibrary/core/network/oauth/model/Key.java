
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

public class Key implements Serializable {
    private String value;
    
    public Key(String value) {
        this.value = value;
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
