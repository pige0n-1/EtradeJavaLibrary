package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;

import java.util.ArrayList;

/**
 * Represents either a PreviewOrderRequest or a PlaceOrderRequest in the E*Trade object model. Once the previewIds field
 * is set, the object becomes a PlaceOrderRequest in the XML document.
 */
public class OrderRequest implements XMLBuildable {

    // Instance data fields
    public String orderType;
    public ArrayList<OrderDetail> order = new ArrayList<>();
    public String clientOrderId;
    public PreviewIds previewIds;

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("orderType", orderType)
                .addArrayListField("order", order)
                .addStringField("clientOrderId", clientOrderId)
                .addXMLObjectField("PreviewIds", previewIds);
    }

    @Override
    public String getXMLClassName() {
        if (previewIds == null)
            return "PreviewOrderRequest";

        else return "PlaceOrderRequest";
    }
}
