package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;

public abstract class EtradeObject<E extends EtradeObject<E>>
        implements XMLDefinedObject<E> {

    // Instance data fields
    protected Document xmlDocument;

    public EtradeObject() {}

    protected String extract(String elementTagName) {
        try {
            return xmlDocument.getElementsByTagName(elementTagName).item(0).getTextContent();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    protected Long extractLong(String elementTagName) {
        try {
            return Long.parseLong(extract(elementTagName));
        }
        catch (Exception ex) {
            return 0L;
        }
    }

    protected Integer extractInt(String elementTagName) {
        try {
            return Integer.parseInt(extract(elementTagName));
        }
        catch (Exception ex) {
            return 0;
        }
    }

    protected BigDecimal extractNumber(String elementTagName) {
        try {
            return new BigDecimal(extract(elementTagName));
        }
        catch (Exception ex) {
            return new BigDecimal("0");
        }
    }

    protected Boolean extractBoolean(String elementTagName) {
        try {
            return Boolean.parseBoolean(extract(elementTagName));
        }
        catch (Exception ex) {
            return null;
        }
    }

    protected NodeList getList(EtradeObject emptyObject) {
        try {
            return xmlDocument.getElementsByTagName(emptyObject.getClass().getName());
        }
        catch (Exception ex) {
            return null;
        }
    }

    protected <T extends EtradeObject<T>> T extractObject(T emptyObject, Node node) throws ObjectMismatchException{
        var objectDocument = XMLDefinedObject.convertToDocument(node);

        return emptyObject.configureFromXMLDocument(objectDocument);
    }

    protected <T extends EtradeObject<T>> T extractObject(T emptyObject) throws ObjectMismatchException {
        var objectList = getList(emptyObject);
        var objectDocument = XMLDefinedObject.convertToDocument(objectList.item(0));

        return emptyObject.configureFromXMLDocument(objectDocument);
    }

    @Override
    public Document toXMLDocument() {
        return xmlDocument;
    }

    @Override
    public String toString() {
        return toXMLString();
    }
}
