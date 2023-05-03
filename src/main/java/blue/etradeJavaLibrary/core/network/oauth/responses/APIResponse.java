
package blue.etradeJavaLibrary.core.network.oauth.responses;

import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import java.io.IOException;
import java.io.InputStream;

import blue.etradeJavaLibrary.etradeObjects.XMLDefinedObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Encapsulates a response from an APIRequest. The response is parsed
 * into an Document object from the Java API. The parseIntoXMLDefinedObject
 * method can be used to directly configure an object that can be defined by
 * XML.
 */
public class APIResponse extends BaseResponse<Document> {
    
    // Static data fields
    private static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    
    /**
     * Constructs an APIResponse object from an InputStream, the unerlying
     * output from an APIRequest object.
     * @param connectionResponseStream 
     */
    public APIResponse(InputStream connectionResponseStream) {
        super(connectionResponseStream);
    }
    
    @Override
    /**
     * Parses the InputStream into a Document object in the Java API. The
     * Document object can be used to define an object in java.
     */
    public Document parse() throws OauthException {
        try {
            return XMLDefinedObject.parseXML(getConnectionResponseStream());
        }
        catch (SAXException | IOException ex) {
            throw new OauthException("The response document could not be parsed.", ex);
        }
    }
    
    /**
     * A convenience method to directly configure an object that implements
     * the XMLDefinedObject interface. The method calls the instance method
     * "configureFromXMLDocument" to configure the object that is passed
     * into this method.
     * @param xmlDefinedObject
     * @throws OauthException
     * @throws ObjectMismatchException 
     */
    public void parseIntoXMLDefinedObject(XMLDefinedObject xmlDefinedObject) throws OauthException, ObjectMismatchException {
        networkLogger.log("Raw response string", toString());
        xmlDefinedObject.configureFromXMLDocument(parse());
    }
}
