package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.etradeObjects.market.Messages;
import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderDetail extends EtradeObject<OrderDetail> {

    // Instance data fields
    public Integer orderNumber;
    public Long accountId;
    public LocalDateTime previewTime;
    public LocalDateTime placedTime;
    public LocalDateTime executedTime;
    public Double orderValue;
    public String status;
    public String orderType;
    public String orderTerm;
    public String priceType;
    public String priceValue;
    public Double limitPrice;
    public Double stopPrice;
    public Double stopLimitPrice;
    public String offsetType;
    public Double offsetValue;
    public String marketSession;
    public String routingDestination;
    public Double bracketedLimitPrice;
    public Double initialStopPrice;
    public Double trailPrice;
    public Double triggerPrice;
    public Double conditionPrice;
    public String conditionSymbol;
    public String conditionType;
    public String conditionFollowPrice;
    public String conditionSecurityType;
    public Integer replacedByOrderId;
    public Integer replacesOrderId;
    public Boolean allOrNone;
    public Long previewId;
    public ArrayList<Instrument> instrument = new ArrayList<>();
    public Messages messages;
    public Double investmentAmount;
    public String positionQuantity;
    public Boolean aipFlag;
    public String egQual;
    public String reInvestOption;
    public Double estimatedCommission;
    public Double estimatedFees;
    public Double estimatedTotalAmount;
    public Double netPrice;
    public Double netBid;
    public Double netAsk;
    public Integer gcd;
    public String ratio;
    public String mfpriceType;

    @Override
    public OrderDetail configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        orderNumber = extractInteger("orderNumber");
        accountId = extractLong("accountId");
        previewTime = extractTime("previewTime");
        placedTime = extractTime("placedTime");
        executedTime = extractTime("executedTime");
        orderValue = extractDouble("orderValue");
        status = extract("status");
        orderType = extract("orderType");
        orderTerm = extract("orderTerm");
        priceType = extract("priceType");
        priceValue = extract("priceValue");
        limitPrice = extractDouble("limitPrice");
        stopPrice = extractDouble("stopPrice");
        stopLimitPrice = extractDouble("stopLimitPrice");
        offsetType = extract("offsetType");
        offsetValue = extractDouble("offsetValue");
        marketSession = extract("marketSession");
        routingDestination = extract("routingDestination");
        bracketedLimitPrice = extractDouble("bracketedLimitPrice");
        initialStopPrice = extractDouble("initialStopPrice");
        trailPrice = extractDouble("trailPrice");
        triggerPrice = extractDouble("triggerPrice");
        conditionPrice = extractDouble("conditionPrice");
        conditionSymbol = extract("conditionSymbol");
        conditionType = extract("conditionType");
        conditionFollowPrice = extract("conditionFollowPrice");
        conditionSecurityType = extract("conditionSecurityType");
        replacedByOrderId = extractInteger("replacedByOrderId");
        replacesOrderId = extractInteger("replacesOrderId");
        allOrNone = extractBoolean("allOrNone");
        previewId = extractLong("previewId");

        var nodeList = getList(new Instrument());
        for (int i = 0; i < nodeList.getLength(); i++)
            instrument.add(extractObject(new Instrument(), nodeList.item(i)));

        messages = extractObject(new Messages());
        investmentAmount = extractDouble("investmentAmount");
        positionQuantity = extract("positionQuantity");
        aipFlag = extractBoolean("aipFlag");
        egQual = extract("egQual");
        reInvestOption = extract("reInvestOption");
        estimatedCommission = extractDouble("estimatedCommission");
        estimatedFees = extractDouble("estimatedFees");
        estimatedTotalAmount = extractDouble("estimatedTotalAmount");
        netPrice = extractDouble("netPrice");
        netBid = extractDouble("netBid");
        netAsk = extractDouble("netAsk");
        gcd = extractInteger("gcd");
        ratio = extract("ratio");
        mfpriceType = extract("mfpriceType");

        return this;
    }
}
