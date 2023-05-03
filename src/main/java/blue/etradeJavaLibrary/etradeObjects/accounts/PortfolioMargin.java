package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class PortfolioMargin extends EtradeObject<PortfolioMargin> {

    // Instance data fields
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

    @Override
    public PortfolioMargin configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        dtCashOpenOrderReserve = extractNumber("dtCashOpenOrderReserve");
        dtMarginOpenOrderReserve = extractNumber("dtMarginOpenOrderReserve");
        liquidatingEquity = extractNumber("liquidatingEquity");
        houseExcessEquity = extractNumber("houseExcessEquity");
        totalHouseRequirement = extractNumber("totalHouseRequirement");
        excessEquityMinusRequirement = extractNumber("excessEquityMinusRequirement");
        totalMarginRqmts = extractNumber("totalMarginRqmts");
        availExcessEquity = extractNumber("availExcessEquity");
        excessEquity = extractNumber("excessEquity");
        openOrderReserve = extractNumber("openOrderReserve");
        fundsOnHold = extractNumber("fundsOnHold");

        return this;
    }
}
