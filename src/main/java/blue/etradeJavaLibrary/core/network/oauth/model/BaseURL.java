
package blue.etradeJavaLibrary.core.network.oauth.model;


public class BaseURL {
    private String baseURL;
    
    public BaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    
    @Override
    public String toString() {
        return baseURL;
    }
}
