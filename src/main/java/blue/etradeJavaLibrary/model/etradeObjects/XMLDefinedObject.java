
package blue.etradeJavaLibrary.model.etradeObjects;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
     * A simple utility method to convert an org.w3c.dom.Node object to an org.w3c.dom.Document object. A Document
     * object encapsulates an XML document, and the Node class is the super class in the java API. This method is
     * useful when the using the getElementsByTagName method in the Document class. That method returns a NodeList
     * object, which holds a list of Node objects. Each Node object can be converted into a Document object, which is
     * the type that is useful to instances of XMLDefinedObject, since they can be configured only from a Document
     * object.
     * @param xmlNode the Node object that represents an XML object
     * @return a standalone XML Document object that contains the same information as the Node object
     */
    static Document convertToDocument(Node xmlNode) {
        final boolean RECURSIVELY_IMPORT_NODE = true;

        try {
            DocumentBuilder documentBuilder = XMLDefinedObject.documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Node importedAccountNode = document.importNode(xmlNode, RECURSIVELY_IMPORT_NODE);
            document.appendChild(importedAccountNode);

            return document;
        }
        catch (ParserConfigurationException ex) {
            return null;
        }
    }

    /**
     * Takes in an XML Document object to configure the implementing object.
     * In this model, this can be used to configure the instance after constructing the object.
     * @param xmlDocument a Document object from the java API, which is the output from the parseXML method
     * @return the instance object, strictly for convenience
     * @throws ObjectMismatchException if the given XML document does not represent the same type of object as the instance
     */
    O configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException;
    
    /**
     * Converts this object into Document form. This method does not track any changes to instance variables after
     * instantiation, and it does not build an entirely new XML document. It only returns the XML document that was
     * set upon instantiation.
     * @return Document object, representing an XML document
     */
    Document toXMLDocument();

    /**
     * Converts the instance object to an XML string
     * @return the XML String representation
     */
    default String toXMLString() {
        try {
            var document = toXMLDocument();
            var transformerFactor = TransformerFactory.newInstance();
            var transformer = transformerFactor.newTransformer();
            var stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));

            return stringWriter.getBuffer().toString();
        }
        catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Converts a Document object to String
     * @param xmlDocument the Document object
     * @return the XML String object
     */
    static String toString(Document xmlDocument) {
        try {
            var transformerFactor = TransformerFactory.newInstance();
            var transformer = transformerFactor.newTransformer();
            var stringWriter = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

            return stringWriter.getBuffer().toString();
        }
        catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Converts a Document object to an indented String
     * @param xmlDocument the Document object
     * @return the XML String object
     */
    static String toIndentedString(Document xmlDocument) {
        try {
            var transformerFactory = TransformerFactory.newInstance();
            var transformer = transformerFactory.newTransformer();

            // Set output properties for proper indentation
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Set indentation to 4 spaces

            var stringWriter = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

            return stringWriter.getBuffer().toString();
        }
        catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }
}
