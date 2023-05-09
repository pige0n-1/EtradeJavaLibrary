package blue.etradeJavaLibrary.model.etradeObjects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an object that can be converted to an XML document from its instance variables, or data fields. This
 * interface consists of one abstract method: buildXMLFromDataFields. Once all the data fields of the implementing class
 * have been set, call this method to return a Document object from the org.w3c.dom package. This Document object
 * encapsulates an XML document.
 */
public interface XMLBuildable {

    /**
     * Returns a Document object that represents the instance object in XML. All child objects will have this method
     * called recursively.
     * @return
     * @throws ParserConfigurationException
     */
    default Document buildXMLFromDataFields() throws ParserConfigurationException {
        XMLDataFields dataFields = getDataFields();

        var documentBuilder = XMLDefinedObject.documentBuilderFactory.newDocumentBuilder();
        Document xmlDocument = documentBuilder.newDocument();
        xmlDocument.setXmlStandalone(true);

        Element parentElement = xmlDocument.createElement(getXMLClassName());
        xmlDocument.appendChild(parentElement);

        for (Map.Entry<String, ?> dataFieldEntry : dataFields)
            addDataFieldToXMLDocument(xmlDocument, parentElement, dataFieldEntry);

        return xmlDocument;
    }

    /**
     * Generates an XMLDataFields object that represents all the instance variables of the instance object
     * @return
     */
    XMLDataFields getDataFields();

    /**
     * Generates the name of the instance class, without dot operators (ex: "Alert"). The format of this name will
     * show up exactly in the XML representation of the instance object.
     * @return
     */
    String getXMLClassName();


    // Private helper methods


    private static void appendObjectToElement(Document parentDocument, Element parentElement, XMLBuildable xmlObject) throws ParserConfigurationException {
        Document childDocument = xmlObject.buildXMLFromDataFields();
        Node importedNode = parentDocument.importNode(childDocument.getDocumentElement(), true);
        parentElement.appendChild(importedNode);
    }

    private static void appendStringToElement(Document parentDocument, Element parentElement, Map.Entry<String, String> dataFieldEntry) {
        var element = parentDocument.createElement(dataFieldEntry.getKey());
        var elementText = parentDocument.createTextNode(dataFieldEntry.getValue());

        element.appendChild(elementText);
        parentElement.appendChild(element);
    }

    private static void addDataFieldToXMLDocument(Document xmlDocument, Element parentElement, Map.Entry<String, ?> dataFieldEntry) throws ParserConfigurationException {
        if (dataFieldEntry.getValue() instanceof XMLBuildable value)
            appendObjectToElement(xmlDocument, parentElement, value);

        else if (dataFieldEntry.getValue() instanceof String)
            appendStringToElement(xmlDocument, parentElement, (Map.Entry<String, String>) dataFieldEntry);

        else if (dataFieldEntry.getValue() instanceof ArrayList value)
            for (XMLBuildable object : (ArrayList<XMLBuildable>) value) appendObjectToElement(xmlDocument, parentElement, object);
    }
}
