package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class MFQuantity extends EtradeObject<MFQuantity> {

    // Instance data fields
    public Double cash;
    public Double margin;
    public String cusip;

    @Override
    public MFQuantity configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        cash = extractDouble("cash");
        margin = extractDouble("margin");
        cusip = extract("cusip");

        return this;
    }
}
