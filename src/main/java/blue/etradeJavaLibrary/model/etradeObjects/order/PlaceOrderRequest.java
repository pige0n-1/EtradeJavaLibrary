package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;

public class PlaceOrderRequest extends PreviewOrderRequest {

    // Instance data fields
    public PreviewIds previewIds;

    @Override
    public XMLDataFields getDataFields() {
        return super.getDataFields().addXMLObjectField("PreviewIds", previewIds);
    }

    @Override
    public String getXMLClassName() {
        return "PlaceOrderRequest";
    }
}
