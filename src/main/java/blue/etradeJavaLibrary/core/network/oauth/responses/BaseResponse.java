
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public abstract class BaseResponse<O> {
    
    // Instance data fields
    private InputStream connectionResponseStream;
    
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
     * Returns a clone of the connection response InputStream. 
     * @return 
     */
    public InputStream getConnectionResponseStream() {
        try {
            byte[] responseBytes = connectionResponseStream.readAllBytes();
            connectionResponseStream = new ByteArrayInputStream(responseBytes);
            
            return new ByteArrayInputStream(responseBytes);
        }
        catch (IOException ex) {
            throw new RuntimeException("Response stream could not be read.");
        }
    }
    
    /**
     * A general convenience method for converting the InputStream response to a String.
     * @return
     */
    public String convertToString() {
        try {
            byte[] responseBytes = getConnectionResponseStream().readAllBytes();
            String responseString = new String(responseBytes);
            logger.log("Raw response string", responseString);
            
            return responseString;
        }
        catch (Exception ex) {
            throw new RuntimeException("Response stream could not be read.", ex);
        }
    }
    
    @Override
    public String toString() {
        return convertToString();
    }
}
