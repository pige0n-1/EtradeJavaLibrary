
package blue.etradeJavaLibrary.core.network.oauth.model;


public class Key {
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
}
