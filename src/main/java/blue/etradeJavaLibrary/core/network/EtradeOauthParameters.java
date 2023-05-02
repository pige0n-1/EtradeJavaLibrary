package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthParameters;

/**
 * In a request to the E*Trade API, the oauth_callback parameter must be set to "oob". This class overrides
 * OauthParameters to implement the automatic addition of the callback parameter.
 */
public class EtradeOauthParameters extends OauthParameters {

    /**
     * Constructs a default EtradeOauthParameters object. Since it is used for E*Trade, the oauth_callback parameter
     * is always set to "oob" automatically.
     */
    public EtradeOauthParameters() {}

    /*public EtradeOauthParameters(Key consumerKey) {
        super(consumerKey);
    }

    public EtradeOauthParameters(Key consumerKey, Key token) {
        super(consumerKey, token);
    }

    public EtradeOauthParameters(Key consumerKey, Key token, Key verifier) {
        super(consumerKey, token, verifier);
    }*/

    @Override
    public void configure() {
        super.configure();
        addCallback("oob");
    }

    @Override
    public void configure(Key consumerKey) {
        configure();
        setConsumerKey(consumerKey);
    }

    @Override
    public void configure(Key consumerKey, Key token) {
        configure(consumerKey);
        addToken(token);
    }

    @Override
    public void configure(Key consumerKey, Key token, Key verifier) {
        configure(consumerKey, token);
        addVerifier(verifier);
    }
}
