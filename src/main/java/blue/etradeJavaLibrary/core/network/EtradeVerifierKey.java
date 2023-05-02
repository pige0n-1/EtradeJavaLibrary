
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.network.oauth.model.*;

/**
 * Used to model the verifier key given by E*Trade. This class is a subclass of Key that is used to validate
 * user input. An E*Trade verifier key must be 5 characters long and uppercase.
 */
public class EtradeVerifierKey extends Key {
    
    // Static data fields
    private static final String KEY_REGEX_PATTERN = "(\\w){5}";

    /**
     * Constructs an EtradeVerifierKey object with a specified value. The string must be 5 characters long, and it can
     * only contain letters and numbers. If it does not match the pattern, a RuntimeException is thrown. The value is
     * automatically converted to uppercase for convenience.
     * @param value
     */
    public EtradeVerifierKey(String value) {
        super(value.toUpperCase());
        
        if (!value.matches(KEY_REGEX_PATTERN))
            throw new InvalidParameterException("Bad verifier code passed.");
    }
}
