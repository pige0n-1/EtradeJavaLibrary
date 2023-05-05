package blue.etradeJavaLibrary.etradeObjects;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public abstract class EtradeObject<E extends EtradeObject<E>>
        implements XMLDefinedObject<E> {

    // Instance data fields
    protected Document xmlDocument;

    public EtradeObject() {}

    protected String extract(String elementTagName) {
        try {
            return xmlDocument.getElementsByTagName(elementTagName).item(0).getTextContent();
        }
        catch (Throwable e) {
            return null;
        }
    }

    protected Long extractLong(String elementTagName) {
        try {
            return Long.parseLong(extract(elementTagName));
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected Integer extractInteger(String elementTagName) {
        try {
            return Integer.parseInt(extract(elementTagName));
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected Double extractDouble(String elementTagName) {
        try {
            return Double.parseDouble(extract(elementTagName));
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected Boolean extractBoolean(String elementTagName) {
        try {
            return Boolean.parseBoolean(extract(elementTagName));
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected LocalDate extractDate(String elementTagName) {
        try {
            return epochMillisecondToLocalDate(extractLong(elementTagName));
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected LocalDateTime extractTime(String elementTagName) {
        try {
            return epochMillisecondToLocalDateTime(extractLong(elementTagName));
        }
        catch (Throwable ex) {
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
        catch (Throwable ex) {
            return null;
        }
    }

    protected <T extends EtradeObject<T>> T extractObject(T emptyObject, String elementTagName) {
        try {
            var objectList = getList(elementTagName);
            var objectDocument = XMLDefinedObject.convertToDocument(objectList.item(0));

            return emptyObject.configureFromXMLDocument(objectDocument);
        }
        catch (Throwable ex) {
            return null;
        }
    }

    protected static <T extends EtradeObject<T>> T extractObject(T emptyObject, Node node) throws ObjectMismatchException{
        try {
            var objectDocument = XMLDefinedObject.convertToDocument(node);

            return emptyObject.configureFromXMLDocument(objectDocument);
        }
        catch (Throwable e) {
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

    private static LocalDate epochMillisecondToLocalDate(long epochMillisecond) {
        final long MILLISECONDS_TO_DAYS_CONVERSION_DIVISOR = 1000 * 60 * 60 * 24;

        return LocalDate.ofEpochDay(epochMillisecond / MILLISECONDS_TO_DAYS_CONVERSION_DIVISOR);
    }

    private static LocalDateTime epochMillisecondToLocalDateTime(long epochMillisecond) {
        return LocalDateTime.ofEpochSecond(epochMillisecond, 0, ZoneOffset.ofHours(0));
    }
}
