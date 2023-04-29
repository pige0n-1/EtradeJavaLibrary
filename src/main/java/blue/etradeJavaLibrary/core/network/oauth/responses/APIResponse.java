
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class APIResponse extends BaseResponse<Document> {
    
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    protected Document parse() throws SAXException, IOException {
        return XMLDefinedObject.parseXML(connectionResponseStream);
    }
    
    public <X extends XMLDefinedObject> void parse(X xmlObject) throws OauthException {
        
        try {
            Document responseDocument = parse();
            xmlObject.processDocument(responseDocument);
        }
        catch (SAXException | IOException ex) {
            throw new OauthException("The response document could not be parsed.", ex);
        }
    }
}
