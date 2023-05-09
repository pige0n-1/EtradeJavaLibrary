package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.accounts.PortfolioMargin;
import blue.etradeJavaLibrary.model.etradeObjects.market.Messages;
import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PreviewOrderResponse extends EtradeObject<PreviewOrderResponse> {

    // Instance data fields
    public String orderType;
    public Messages messageList;
    public Double totalOrderValue;
    public Double totalCommission;
    public ArrayList<OrderDetail> order = new ArrayList<>();
    public PreviewIds previewIds;
    public LocalDateTime previewTime;
    public Boolean dstFlag;
    public Long accountId;
    public Integer optionLevelCd;
    public String marginLevelCd;
    public PortfolioMargin portfolioMargin;
    public Boolean isEmployee;
    public String commissionMessage;
    public Disclosure disclosure;
    public String clientOrderId;
    public MarginBuyingPowerDetails marginBpDetails;
    public CashBuyingPowerDetails cashBpDetails;
    public DtBuyingPowerDetails dtBpDetails;

    @Override
    public PreviewOrderResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        orderType = extract("orderType");
        messageList = extractObject(new Messages());
        totalOrderValue = extractDouble("totalOrderValue");
        totalCommission = extractDouble("totalCommission");

        var nodeList = getList(new OrderDetail());
        for (int i = 0; i < nodeList.getLength(); i++)
            order.add(extractObject(new OrderDetail(), nodeList.item(i)));

        previewIds = extractObject(new PreviewIds());
        previewTime = extractTime("previewTime");
        dstFlag = extractBoolean("dstFlag");
        accountId = extractLong("accountId");
        optionLevelCd = extractInteger("optionLevelCd");
        marginLevelCd = extract("marginLevelCd");
        portfolioMargin = extractObject(new PortfolioMargin());
        isEmployee = extractBoolean("isEmployee");
        commissionMessage = extract("commissionMessage");
        disclosure = extractObject(new Disclosure());
        clientOrderId = extract("clientOrderId");
        marginBpDetails = extractObject(new MarginBuyingPowerDetails());
        cashBpDetails = extractObject(new CashBuyingPowerDetails());
        dtBpDetails = extractObject(new DtBuyingPowerDetails());

        return this;
    }
}
