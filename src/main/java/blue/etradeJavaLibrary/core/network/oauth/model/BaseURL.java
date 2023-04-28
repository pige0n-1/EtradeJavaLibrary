
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

public class BaseURL 
        implements Serializable {
    
    // Instance data fields
    private final String baseURL;
    
    public BaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    
    /**
     * Creates a new BaseURL by concatenating the two string parameters. This
     * is provided for pure convenience.
     * @param part1
     * @param part2 
     */
    public BaseURL(String part1, String part2) {
        this.baseURL = part1 + part2;
    }
    
    public boolean hasUnfilledVariables() {
        final String UNFILLED_VARIABLES_REGEX = ".+\\{.+\\}.*";
        
        return baseURL.matches(UNFILLED_VARIABLES_REGEX);
    }
    
    public BaseURL append(String url) {
        return new BaseURL(baseURL + url);
    }
    
    @Override
    public String toString() {
        return baseURL;
    }
}
