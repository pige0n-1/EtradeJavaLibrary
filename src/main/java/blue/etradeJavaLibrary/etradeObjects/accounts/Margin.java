package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

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
