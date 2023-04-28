
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.IOException;
import java.io.InputStream;

public abstract class BaseResponse {
    
    // Instance data fields
    protected InputStream connectionResponseStream;
    
    // Static data fields
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    public BaseResponse(InputStream connectionResponseStream) {
        this.connectionResponseStream = connectionResponseStream;
    }
    
    public abstract Object parseResponse() throws OauthException;
    
    protected static String convertToString(InputStream connectionResponseStream) throws OauthException {
        try {
            byte[] responseBytes = connectionResponseStream.readAllBytes();
            String responseString = new String(responseBytes);
            logger.log("Raw response string", responseString);
            return responseString;
        }
        catch (IOException ex) {
            throw new OauthException("Response stream could not be read.", ex);
        }
    }
}
