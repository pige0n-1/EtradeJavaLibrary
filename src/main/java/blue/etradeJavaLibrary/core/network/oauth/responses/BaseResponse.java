
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public abstract class BaseResponse<O> {
    
    // Instance data fields
    protected InputStream connectionResponseStream;
    
    // Static data fields
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    public BaseResponse(InputStream connectionResponseStream) {
        this.connectionResponseStream = connectionResponseStream;
    }
    
    /**
     * Returns the parsed form of the InputStream response. It can be parsed
     * into different objects depending on the implementatino.
     * @return
     * @throws OauthException 
     */
    public abstract O parse() throws OauthException;
    
    /**
     * A general convenience method for converting the InputStream response to a String.
     * @return
     * @throws OauthException 
     */
    public String convertToString() throws OauthException {
        try {
            byte[] responseBytes = connectionResponseStream.readAllBytes();
            String responseString = new String(responseBytes);
            logger.log("Raw response string", responseString);
            
            connectionResponseStream = new ByteArrayInputStream(responseBytes); // replenish the InputStream
            
            return responseString;
        }
        catch (Exception ex) {
            throw new OauthException("Response stream could not be read.", ex);
        }
    }
    
    @Override
    public String toString() {
        try {
            return convertToString();
        }
        catch (Exception ex) {
            return "";
        }
    }
}
