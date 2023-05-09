package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class ExpirationDate extends EtradeObject<ExpirationDate> {

    // Instance data fields
    public Integer year;
    public Integer month;
    public Integer day;
    public String expiryType;

    @Override
    public ExpirationDate configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        year = extractInteger("year");
        month = extractInteger("month");
        day = extractInteger("day");
        expiryType = extract("expiryType");

        return this;
    }
}
