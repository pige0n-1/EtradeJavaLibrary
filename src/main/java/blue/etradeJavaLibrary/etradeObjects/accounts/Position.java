package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;

public class Position extends EtradeObject<Position> {

    // Instance data fields
    public Long positionId;
    public Long accountId;
    public Product product;
    public String osiKey;
    public String symbolDescription;
    public Date dateAcquired;
    public Double pricePaid;
    public Double price;
    public Double commissions;
    public Double otherFees;
    public Double quantity;
    public String positionIndicator;
    public String positionType;
    public Double change;
    public Double changePct;
    public Double daysGain;
    public Double daysGainPct;
    public Double marketValue;
    public Double totalCost;
    public Double totalGain;
    public Double totalGainPct;
    public Double pctOfPortfolio;
    public Double costPerShare;
    public Double todayCommissions;
    public Double todayFees;
    public Double todayPricePaid;
    public Double todayQuantity;
    public String quotestatus;
    public Date dateTimeUTC;
    public Double adjPrevClose;
    public CompleteView complete;
    public String lotsDetails;
    public String quoteDetails;
    public ArrayList<PositionLot> positionLot = new ArrayList<>();

    @Override
    public Position configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        positionId = extractLong("positionId");
        accountId = extractLong("accountId");
        product = extractObject(new Product());
        osiKey = extract("osiKey");
        symbolDescription = extract("symbolDescription");
        dateAcquired = extractDate("dateAcquired");
        pricePaid = extractDouble("pricePaid");
        price = extractDouble("price");
        commissions = extractDouble("commissions");
        otherFees = extractDouble("otherFees");
        quantity = extractDouble("quantity");
        positionIndicator = extract("positionIndicator");
        positionType = extract("positionType");
        change = extractDouble("change");
        changePct = extractDouble("changePct");
        daysGain = extractDouble("daysGain");
        daysGainPct = extractDouble("daysGainPct");
        marketValue = extractDouble("marketValue");
        totalCost = extractDouble("totalCost");
        totalGain = extractDouble("totalGain");
        totalGainPct = extractDouble("totalGainPct");
        pctOfPortfolio = extractDouble("pctOfPortfolio");
        costPerShare = extractDouble("costPerShare");
        todayCommissions = extractDouble("todayCommissions");
        todayFees = extractDouble("todayFees");
        todayPricePaid = extractDouble("todayPricePaid");
        todayQuantity = extractDouble("todayQuantity");
        quotestatus = extract("quotestatus");
        dateTimeUTC = extractDate("dateTimeUTC");
        adjPrevClose = extractDouble("adjPrevClose");
        complete = extractObject(new CompleteView(), "Complete");
        lotsDetails = extract("lotsDetails");
        quoteDetails = extract("quoteDetails");
        var nodeList = getList(new PositionLot());
        for (int i = 0; i < nodeList.getLength(); i++)
            positionLot.add(extractObject(new PositionLot(), nodeList.item(i)));

        return this;
    }
}
