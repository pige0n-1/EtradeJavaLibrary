
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.network.oauth.model.Key;

public class EtradeClientWrapper {
    
    private Key consumerKey = KeyAndURLExtractor.getConsumerKey();
    private Key consumerSecret = KeyAndURLExtractor.getConsumerSecret();
    private Key token;
    private Key tokenSecret;
    
    private final EnvironmentType environmentType;
    
    public EtradeClientWrapper(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }
    
    public enum EnvironmentType {
        LIVE,
        SANDBOX
    }
}
