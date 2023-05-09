package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.market.Messages;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class OrdersResponse extends EtradeObject<OrdersResponse> {

    // Instance data fields
    public Long marker;
    public String next;
    public ArrayList<Order> order = new ArrayList<>();
    public Messages messages;

    @Override
    public OrdersResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        marker = extractLong("marker");
        next = extract("next");
        var nodeList = getList(new Order());
        for (int i = 0; i < nodeList.getLength(); i++)
            order.add(extractObject(new Order(), nodeList.item(i)));
        messages = extractObject(new Messages());

        return this;
    }
}
