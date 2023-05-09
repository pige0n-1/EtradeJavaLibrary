package blue.etradeJavaLibrary.etradeObjects;

import org.w3c.dom.Document;

/**
 * Represents an object that can be converted to an XML document from its instance variables, or data fields. This
 * interface consists of one abstract method: buildXMLFromDataFields. Once all the data fields of the implementing class
 * have been set, call this method to return a Document object from the org.w3c.dom package. This Document object
 * encapsulates an XML document.
 */
public interface XMLBuildable {

    /**
     * Builds an org.w3c.dom.Document object from all the instance variables of the object. The document is in XML
     * format. Once all the instance variables are set, call this method to build the variables into an XML document.
     * @return an XML document representing this object, built from all the data fields
     */
    Document buildXMLFromDataFields();
}
