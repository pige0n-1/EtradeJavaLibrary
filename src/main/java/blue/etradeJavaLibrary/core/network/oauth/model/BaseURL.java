
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

public class BaseURL implements Serializable {
    private String baseURL;
    
    public BaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    
    @Override
    public String toString() {
        return baseURL;
    }
}
