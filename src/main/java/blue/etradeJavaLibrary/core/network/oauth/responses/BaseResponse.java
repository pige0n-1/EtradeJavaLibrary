
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;
import java.io.IOException;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;

public abstract class BaseResponse {
    protected InputStream connectionResponseStream;
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    public BaseResponse(InputStream connectionResponseStream) {
        this.connectionResponseStream = connectionResponseStream;
    }
    
    public abstract Parameters parseResponse() throws OauthException;
    
    protected static String convertToString(InputStream connectionResponseStream) throws OauthException {
        try {
            byte[] responseBytes = connectionResponseStream.readAllBytes();
            String responseString = new String(responseBytes);
            logger.log("Raw response string", responseString);
            return responseString;
        }
        catch (IOException ex) {
            throw new OauthException("Response stream could not be read.");
        }
    }
}
