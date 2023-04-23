
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.SimpleParameters;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class OauthFlowResponse extends BaseResponse {
    
    public OauthFlowResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    public Parameters parseResponse() throws OauthException {
        String responseString = convertToString(connectionResponseStream);
        
        logger.log("Raw response string", responseString);
        
        String[] parametersArray = responseString.split("&");
        SimpleParameters parameters = new SimpleParameters();
        
        for (String parameterString : parametersArray) {
            String[] parameterKeyAndValue = parameterString.split("=");
            parameters.addParameterWithoutEncoding(parameterKeyAndValue[0], parameterKeyAndValue[1]);
        }
        
        return parameters;
    }
}
