
package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;

public class Key {
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
}
