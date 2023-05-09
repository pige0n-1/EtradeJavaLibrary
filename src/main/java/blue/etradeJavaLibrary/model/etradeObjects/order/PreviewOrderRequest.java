package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;

import java.util.ArrayList;

public class PreviewOrderRequest implements XMLBuildable {

    // Instance data fields
    public String orderType;
    public ArrayList<OrderDetail> order = new ArrayList<>();
    public String clientOrderId; // TODO: ensure this field is created in OrderBuilder

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("orderType", orderType)
                .addArrayListField("order", order)
                .addStringField("clientOrderId", clientOrderId);
    }

    @Override
    public String getXMLClassName() {
        return "PreviewOrderRequest";
    }
}
