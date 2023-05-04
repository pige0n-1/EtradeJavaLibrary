package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.lang.Double;
import java.util.Date;

public class PositionLot extends EtradeObject<PositionLot> {

    // Instance data fields
    public Long positionId;
    public Long positionLotId;
    public Double price;
    public Integer termCode;
    public Double daysGain;
    public Double daysGainPct;
    public Double marketValue;
    public Double totalCost;
    public Double totalCostForGainPct;
    public Double totalGain;
    public Integer lotSourceCode;
    public Double originalQty;
    public Double remainingQty;
    public Double availableQty;
    public Long orderNo;
    public Integer legNo;
    public Date acquiredDate;
    public Integer locationCode;
    public Double exchangeRate;
    public String settlementCurrency;
    public String paymentCurrency;
    public Double adjPrice;
    public Double commPerShare;
    public Double feesPerShare;
    public Double premiumAdj;
    public Integer shortType;

    @Override
    public PositionLot configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        positionId = extractLong("positionId");
        positionLotId = extractLong("positionLotId");
        price = extractDouble("price");
        termCode = extractInteger("termCode");
        daysGain = extractDouble("daysGain");
        daysGainPct = extractDouble("daysGainPct");
        marketValue = extractDouble("marketValue");
        totalCost = extractDouble("totalCost");
        totalCostForGainPct = extractDouble("totalCostForGainPct");
        totalGain = extractDouble("totalGain");
        lotSourceCode = extractInteger("lotSourceCode");
        originalQty = extractDouble("originalQty");
        remainingQty = extractDouble("remainingQty");
        availableQty = extractDouble("availableQty");
        orderNo = extractLong("orderNo");
        legNo = extractInteger("legNo");
        acquiredDate = new Date(extractLong("acquiredDate"));
        locationCode = extractInteger("locationCode");
        exchangeRate = extractDouble("exchangeRate");
        settlementCurrency = extract("settlementCurrency");
        paymentCurrency = extract("paymentCurrency");
        adjPrice = extractDouble("adjPrice");
        commPerShare = extractDouble("commPerShare");
        feesPerShare = extractDouble("feesPerShare");
        premiumAdj = extractDouble("premiumAdj");
        shortType = extractInteger("shortType");

        return this;
    }
}
