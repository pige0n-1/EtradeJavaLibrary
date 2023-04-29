
package blue.etradeJavaLibrary.core.network.oauth.responses;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public interface XMLDefinedObject {
    
    /**
     * This is a simple method to convert an input stream from an HTTP respond
     * that is in the XML format to a Document object found in the java API. The
     * output of this method can be used in the processDocument method to configure 
     * the implementing class.
     * @param xmlInputStream the response from an APIRequest
     * @return Document object in the document object model
     * @throws SAXException
     * @throws IOException 
     */
    public static Document parseXML(InputStream xmlInputStream) throws SAXException, IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            return documentBuilder.parse(xmlInputStream);
        }
        catch (ParserConfigurationException ex) {
            return null;
        }
    }
    
    /**
     * Takes in an xml Document object to configure the implementing object.
     * In this model, this can be used to configure the instance after constructing the object.
     * @param xmlDocument a Document object from the java API, which is the output from the parseXML method
     */
    public void processDocument(Document xmlDocument);
}
