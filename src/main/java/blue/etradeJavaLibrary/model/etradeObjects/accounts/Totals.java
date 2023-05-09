package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Totals extends EtradeObject<Totals> {

    // Instance data fields
    public Double todaysGainLoss;
    public Double todaysGainLossPct;
    public Double totalMarketValue;
    public Double totalGainLoss;
    public Double totalGainLossPct;
    public Double totalPricePaid;
    public Double cashBalance;

    @Override
    public Totals configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        todaysGainLoss = extractDouble("todaysGainLoss");
        todaysGainLossPct = extractDouble("todaysGainLossPct");
        totalMarketValue = extractDouble("totalMarketValue");
        totalGainLoss = extractDouble("totalGainLoss");
        totalGainLossPct = extractDouble("totalGainLossPct");
        totalPricePaid = extractDouble("totalPricePaid");
        cashBalance = extractDouble("cashBalance");

        return this;
    }
}
