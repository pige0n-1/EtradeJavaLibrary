
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;

public class APIResponse extends BaseResponse {
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    public Parameters parseResponse() throws OauthException {
        return null;
    }
}
