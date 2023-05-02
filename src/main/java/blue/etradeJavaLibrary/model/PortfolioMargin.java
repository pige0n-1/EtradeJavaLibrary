package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class PortfolioMargin extends EtradeObject<PortfolioMargin> {

    // Instance data fields
    private Document xmlDocument;
    public BigDecimal dtCashOpenOrderReserve;
    public BigDecimal dtMarginOpenOrderReserve;
    public BigDecimal liquidatingEquity;
    public BigDecimal houseExcessEquity;
    public BigDecimal totalHouseRequirement;
    public BigDecimal excessEquityMinusRequirement;
    public BigDecimal totalMarginRqmts;
    public BigDecimal availExcessEquity;
    public BigDecimal excessEquity;
    public BigDecimal openOrderReserve;
    public BigDecimal fundsOnHold;
}
