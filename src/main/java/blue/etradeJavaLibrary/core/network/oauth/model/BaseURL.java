
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

/**
 * This immutable class is used to represent a base URL. It does not contain any query information. The base URL string
 * that is used in the constructor can contain variables that are later filled in with PathParameters objects. Simply
 * surround the variable name with curly brackets in the string, and any path parameters with the same key name will be
 * filled in automatically.
 */
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

    /**
     * Adds a second baseURL to the first and returns it. The instance BaseURL is not changed.
     * @param url
     * @return
     */
    public BaseURL append(String url) {
        return new BaseURL(baseURL + url);
    }
    
    @Override
    public String toString() {
        return baseURL;
    }
}
