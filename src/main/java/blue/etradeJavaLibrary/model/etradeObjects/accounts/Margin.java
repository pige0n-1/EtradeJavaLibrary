package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Margin extends EtradeObject<Margin> {

    // Instance data fields
    public Double dtCashOpenOrderReserve;
    public Double dtMarginOpenOrderReserve;

    @Override
    public Margin configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        dtCashOpenOrderReserve = extractDouble("dtCashOpenOrderReserve");
        dtMarginOpenOrderReserve = extractDouble("dtMarginOpenOrderReserve");

        return this;
    }
}
