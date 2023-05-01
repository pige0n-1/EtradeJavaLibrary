
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Encapsulates a general response from the service provider
 * in the Oauth 1.0a model. It can be parsed into any object
 * that the child class decides. In the Oauth authentication flow,
 * it is parsed into a Parameters object in order to extract the request
 * and access tokens, and in an API response, it is parsed into an
 * object from the document object model (DOM).
 * @param <O> The type of object the response will be parsed into
 */
public abstract class BaseResponse<O> {
    
    // Instance data fields
    /**
     * The raw InputStream object that is returned from the HttpURLConnection
     * object.
     */
    private InputStream connectionResponseStream;
    
    // Static data fields
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    /**
     * Creates a BaseResponse object from the raw InputStream object.
     * @param connectionResponseStream 
     */
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
     * @return returns a clone of the raw InputStream data field
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
