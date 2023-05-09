package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class SelectedED extends EtradeObject<SelectedED> {

    // Instance data fields
    public Integer month;
    public Integer year;
    public Integer day;

    @Override
    public SelectedED configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        month = extractInteger("month");
        year = extractInteger("year");
        day = extractInteger("day");

        return this;
    }
}
