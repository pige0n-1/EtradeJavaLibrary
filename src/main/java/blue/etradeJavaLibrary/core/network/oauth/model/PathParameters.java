
package blue.etradeJavaLibrary.core.network.oauth.model;


public class PathParameters extends Parameters {
    
    public PathParameters(String key, String value) {
        addParameter(key, value);
    }
    
    public PathParameters(String key1, String value1, String key2, String value2) {
        addParameter(key1, value1);
        addParameter(key2, value2);
    }
}
