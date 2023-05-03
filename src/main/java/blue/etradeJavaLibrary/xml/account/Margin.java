package blue.etradeJavaLibrary.xml.account;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.xml.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Margin extends EtradeObject<Margin> {

    // Instance data fields
    public BigDecimal dtCashOpenOrderReserve;
    public BigDecimal dtMarginOpenOrderReserve;

    @Override
    public Margin configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        dtCashOpenOrderReserve = extractNumber("dtCashOpenOrderReserve");
        dtMarginOpenOrderReserve = extractNumber("dtMarginOpenOrderReserve");

        return this;
    }
}
