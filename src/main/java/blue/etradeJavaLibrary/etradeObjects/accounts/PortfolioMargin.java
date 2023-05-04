package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class PortfolioMargin extends EtradeObject<PortfolioMargin> {

    // Instance data fields
    public Double dtCashOpenOrderReserve;
    public Double dtMarginOpenOrderReserve;
    public Double liquidatingEquity;
    public Double houseExcessEquity;
    public Double totalHouseRequirement;
    public Double excessEquityMinusRequirement;
    public Double totalMarginRqmts;
    public Double availExcessEquity;
    public Double excessEquity;
    public Double openOrderReserve;
    public Double fundsOnHold;

    @Override
    public PortfolioMargin configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        dtCashOpenOrderReserve = extractDouble("dtCashOpenOrderReserve");
        dtMarginOpenOrderReserve = extractDouble("dtMarginOpenOrderReserve");
        liquidatingEquity = extractDouble("liquidatingEquity");
        houseExcessEquity = extractDouble("houseExcessEquity");
        totalHouseRequirement = extractDouble("totalHouseRequirement");
        excessEquityMinusRequirement = extractDouble("excessEquityMinusRequirement");
        totalMarginRqmts = extractDouble("totalMarginRqmts");
        availExcessEquity = extractDouble("availExcessEquity");
        excessEquity = extractDouble("excessEquity");
        openOrderReserve = extractDouble("openOrderReserve");
        fundsOnHold = extractDouble("fundsOnHold");

        return this;
    }
}
