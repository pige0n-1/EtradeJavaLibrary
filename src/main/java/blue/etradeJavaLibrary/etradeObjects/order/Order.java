package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class Order extends EtradeObject<Order> {

    // Instance data fields
    public Long orderId;
    public String details;
    public String orderType;
    public Double totalOrderValue;
    public Double totalCommission;
    public ArrayList<OrderDetail> orderDetail = new ArrayList<>();
    public Events events;

    @Override
    public Order configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        orderId = extractLong("orderId");
        details = extract("details");
        orderType = extract("orderType");
        totalOrderValue = extractDouble("totalOrderValue");
        totalCommission = extractDouble("totalCommission");
        var nodeList = getList(new OrderDetail());
        for (int i = 0; i < nodeList.getLength(); i++)
            orderDetail.add(extractObject(new OrderDetail(), nodeList.item(i)));
        events = extractObject(new Events());

        return this;
    }
}
