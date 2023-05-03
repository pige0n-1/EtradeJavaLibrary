package blue.etradeJavaLibrary.etradeObjects;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
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
        return getList(getClassName(emptyObject));
    }

    protected NodeList getList(String elementTagName) {
        try {
            return xmlDocument.getElementsByTagName(elementTagName);
        }
        catch (Exception ex) {
            return null;
        }
    }

    protected <T extends EtradeObject<T>> T extractObject(T emptyObject, String elementTagName) {
        try {
            var objectList = getList(elementTagName);
            var objectDocument = XMLDefinedObject.convertToDocument(objectList.item(0));

            return emptyObject.configureFromXMLDocument(objectDocument);
        }
        catch (Exception ex) {
            return null;
        }
    }

    protected static <T extends EtradeObject<T>> T extractObject(T emptyObject, Node node) throws ObjectMismatchException{
        try {
            var objectDocument = XMLDefinedObject.convertToDocument(node);

            return emptyObject.configureFromXMLDocument(objectDocument);
        }
        catch (ObjectMismatchException e) {
            return null;
        }
    }

    protected <T extends EtradeObject<T>> T extractObject(T emptyObject) throws ObjectMismatchException {
        return extractObject(emptyObject, getClassName(emptyObject));
    }

    @Override
    public Document toXMLDocument() {
        return xmlDocument;
    }

    @Override
    public String toString() {
        return toXMLString();
    }


    // Private helper methods


    private static String getClassName(Object object) {
        String classNameWithDotOperators = object.getClass().getName();
        int lastIndexOfDot = classNameWithDotOperators.lastIndexOf('.');

        return classNameWithDotOperators.substring(lastIndexOfDot + 1);
    }
}
