package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.model.InvalidParameterException;
import blue.etradeJavaLibrary.model.etradeObjects.order.OrderRequest;
import blue.etradeJavaLibrary.model.etradeObjects.order.PreviewId;
import blue.etradeJavaLibrary.model.etradeObjects.order.PreviewIds;

public class OrderBuilder {

    // Instance data fields
    private final OrderRequest orderRequest = new OrderRequest();

    /**
     * Constructs a new OrderBuilder
     * @param orderType the orderType value that E*Trade requires
     */
    public OrderBuilder(OrderType orderType) {
        this(orderType, ""); // TODO: make sure the empty string isn't processed in XML document
    }

    /**
     * Constructs a new OrderBuilder.
     * @param orderType the orderType value that E*Trade requires
     * @param clientOrderId a unique identifier from the client's records. The clientOrderId can be any value of 20 or
     *                      less alphanumeric characters but must be unique within the account.
     */
    public OrderBuilder(OrderType orderType, String clientOrderId) {
        if (!clientOrderId.matches("\\w{1,20}")) // Regex: between 1 and 20 alphanumeric characters
            throw new InvalidParameterException("clientOrderId must be 20 or less alphanumeric characters.");

        orderRequest.orderType = orderType.name();
        orderRequest.clientOrderId = clientOrderId;
    }

    public OrderBuilder addPreviewId(String previewIdString) {
        var previewIds = new PreviewIds();
        var previewId = new PreviewId();

        previewId.previewId = Integer.valueOf(previewIdString);
        previewIds.previewIds.add(previewId);

        orderRequest.previewIds = previewIds;

        return this;
    }

    public OrderRequest build() {
        return orderRequest;
    }

    public static void main(String[] args) {
        var orderBuilder = new OrderBuilder(OrderType.EQ, "ioy4it8hfnvkjdknsl");
        System.out.println(orderBuilder.build().clientOrderId);
    }

    public enum OrderType {
        EQ, OPTN, SPREADS,
        BUY_WRITES, BUTTERFLY, IRON_BUTTERFLY,
        CONDOR, IRON_CONDOR, MF, MMF;
    }
}
