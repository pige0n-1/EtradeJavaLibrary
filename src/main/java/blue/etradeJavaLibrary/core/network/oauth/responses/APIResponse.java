
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class APIResponse extends BaseResponse<Document> {
    
    // Static data fields
    private static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    public Document parse() throws OauthException {
        try {
            return XMLDefinedObject.parseXML(getConnectionResponseStream());
        }
        catch (SAXException | IOException ex) {
            throw new OauthException("The response document could not be parsed.", ex);
        }
    }
    
    public void parseIntoXMLDefinedObject(XMLDefinedObject xmlDefinedObject) throws OauthException, ObjectMismatchException {
        xmlDefinedObject.configureFromXMLDocument(parse());
    }
}
