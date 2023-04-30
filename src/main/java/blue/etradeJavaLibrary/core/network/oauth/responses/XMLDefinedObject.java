
package blue.etradeJavaLibrary.core.network.oauth.responses;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;

/**
 * An XMLDefinedObject, in this model, represents any type of object that can
 * be represented in the document object model (DOM) with XML. This is useful in the
 * oauth 1.0a model to easily convert XML in HTTP requests and responses to corresponding
 * Java objects.
 * @param <O> an optional type parameter for the instance type that implements XMLDefinedObject.
 * this is used for the configureFromXMLDocument method to return the same type.
 */
public interface XMLDefinedObject<O extends XMLDefinedObject> {
    
    // Static data fields
    public static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    public static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    
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
    static Document parseXML(InputStream xmlInputStream) throws SAXException, IOException {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(xmlInputStream);
            document.getDocumentElement().normalize();
            
            return document;
        }
        catch (ParserConfigurationException ex) {
            return null;
        }
    }
    
    /**
     * Takes in an xml Document object to configure the implementing object.
     * In this model, this can be used to configure the instance after constructing the object.
     * @param xmlDocument a Document object from the java API, which is the output from the parseXML method
     * @return the instance object, strictly for convenience
     * @throws ObjectMismatchException if the given XML document does not represent the same type of object as the instance
     */
    O configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException;
    
    /**
     * Converts this object into Document form.
     * @return Document object, representing an XML document
     */
    Document toXMLDocument();
}
