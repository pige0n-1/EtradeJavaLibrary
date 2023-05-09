package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.model.etradeObjects.order.PlaceOrderRequest;

public class Order extends PreviewOrder {

    /* Prevent instantiation outside the package */
    Order(PlaceOrderRequest placeOrderRequest) {
        super(placeOrderRequest);
    }
}
