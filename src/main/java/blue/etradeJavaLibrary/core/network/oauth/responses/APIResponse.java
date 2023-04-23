
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.OauthParameterException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;

public class APIResponse extends OauthResponse {
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    public Parameters parseResponse() throws OauthParameterException {
        return null;
    }
}
