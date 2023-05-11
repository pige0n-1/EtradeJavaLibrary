package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.model.InvalidParameterException;
import blue.etradeJavaLibrary.model.etradeObjects.order.PlaceOrderRequest;
import blue.etradeJavaLibrary.model.etradeObjects.order.PreviewOrderRequest;

public class OrderBuilder {

    // Instance data fields
    private OrderType orderType;
    private String clientOrderId;

    private String previewId;

    public OrderBuilder(OrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * The clientOrderId can be any value of 20 or less alphanumeric characters but must be unique within the account
     * @param orderType
     * @param clientOrderId
     */
    public OrderBuilder(OrderType orderType, String clientOrderId) {
        if (!clientOrderId.matches("\\w{1,20}"))
            throw new InvalidParameterException("clientOrderId must be 20 or less alphanumeric characters.");

        this.orderType = orderType;
        this.clientOrderId = clientOrderId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public PreviewOrder build() {
        if (previewId == null)
            return buildPreviewOrder();

        else return buildPlaceOrder();
    }

    public static void main(String[] args) {
        var orderBuilder = new OrderBuilder(OrderType.EQ, "ioy4it8hfnvkjdknsl");
        System.out.println(orderBuilder.clientOrderId);
    }

    public enum OrderType {
        EQ, OPTN, SPREADS,
        BUY_WRITES, BUTTERFLY, IRON_BUTTERFLY,
        CONDOR, IRON_CONDOR, MF, MMF
    }


    // Private helper methods


    private PreviewOrder buildPreviewOrder() {
        return null;
    }

    private Order buildPlaceOrder() {
        return null;
    }
}
