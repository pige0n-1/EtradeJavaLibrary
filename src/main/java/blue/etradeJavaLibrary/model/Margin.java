package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Margin extends EtradeObject<Margin> {

    // Instance data fields
    private Document xmlDocument;
    public BigDecimal dtCashOpenOrderReserve;
    public BigDecimal dtMarginOpenOrderReserve;
}
