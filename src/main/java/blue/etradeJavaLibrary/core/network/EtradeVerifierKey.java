
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.network.oauth.model.*;

public class EtradeVerifierKey extends Key {
    
    // Static data fields
    private static final String KEY_REGEX_PATTERN = "(\\w){5}";
    
    public EtradeVerifierKey(String value) throws InvalidParameterException {
        super(value.toUpperCase());
        
        if (!value.matches(KEY_REGEX_PATTERN))
            throw new InvalidParameterException("Bad verifier code passed.");
    }
}
