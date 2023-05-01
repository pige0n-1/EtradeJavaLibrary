
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.Rfc3986;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;

/**
 * Encapsulates a response from an HTTP request in the Oauth flow.
 * It can be parsed into a Parameters object to obtain the request
 * or access tokens.
 * @author Hunter
 */
public class OauthFlowResponse extends BaseResponse<Parameters> {
    
    /**
     * Creates an OauthFlowResponse with a given InputStream object.
     * @param connectionResponseStream the response stream
     */
    public OauthFlowResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    /**
     * Parses the OauthFlowResponse into a Parameters object,
     * where any tokens can be extracted.
     */
    public Parameters parse() throws OauthException {
        String responseString = convertToString();
        
        String[] parametersArray = responseString.split("&");
        var parameters = new Parameters(false);
        
        for (String parameterString : parametersArray) {
            String[] parameterKeyAndValue = parameterString.split("=");
            
            String decodedKey = Rfc3986.decode(parameterKeyAndValue[0]);
            String decodedValue = Rfc3986.decode(parameterKeyAndValue[1]);
            
            parameters.addParameter(decodedKey, decodedValue);
        }
        
        return parameters;
    }
}
