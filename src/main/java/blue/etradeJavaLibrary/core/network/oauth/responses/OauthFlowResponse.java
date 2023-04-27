
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;

public class OauthFlowResponse extends BaseResponse {
    
    public OauthFlowResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    public Parameters parseResponse() throws OauthException {
        String responseString = convertToString(connectionResponseStream);
        
        String[] parametersArray = responseString.split("&");
        Parameters parameters = new Parameters(false);
        
        for (String parameterString : parametersArray) {
            String[] parameterKeyAndValue = parameterString.split("=");
            String decodedKey = Rfc3986.decode(parameterKeyAndValue[0]);
            String decodedValue = Rfc3986.decode(parameterKeyAndValue[1]);
            
            parameters.addParameter(decodedKey, decodedValue);
        }
        
        return parameters;
    }
}
