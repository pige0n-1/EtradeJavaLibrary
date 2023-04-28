
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.InputStream;

public class APIResponse extends BaseResponse {
    
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    public String parse() throws OauthException {
        return convertToString(connectionResponseStream);
    }
}
