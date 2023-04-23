
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.OauthException;

public abstract class OauthResponse {
    protected InputStream connectionResponseStream;
    protected ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    public OauthResponse(InputStream connectionResponseStream) {
        this.connectionResponseStream = connectionResponseStream;
    }
    
    public abstract Parameters parseResponse() throws OauthException ;
    
    protected static String convertToString(InputStream connectionResponseStream) {
        try {
            byte[] responseBytes = connectionResponseStream.readAllBytes();
            String responseString = new String(responseBytes, "UTF-8");
            return responseString;
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
