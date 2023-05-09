
package blue.etradeJavaLibrary.core.network.oauth.model;

import java.io.Serializable;

/**
 * This class is used to encapsulate any kind of "key" used in the OAuth 1.0a
 * model. The key value is encapsulated in order to validate the key and
 * maintain rigidity of the program.
 */
public class Key implements Serializable {
    
    // Instance data fields
    /**
     * The underlying string value of the key
     */
    private final String value;
    
    /**
     * Constructs a Key with an underlying String
     * @param value 
     */
    public Key(String value) {
        if (value.contains(" "))
            throw new InvalidParameterException("The value of a Key object cannot contain spaces.");
        
        this.value = value;
    }
    
    /**
     * Constructs an empty key
     */
    public Key() {
        value = "";
    }
    
    /**
     * Returns the string value of the key
     * @return 
     */
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "Key: " + getValue();
    }

    public boolean isEmpty() {
        return value.equals("");
    }
}
