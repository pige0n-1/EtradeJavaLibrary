package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.order.PreviewOrderRequest;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class PreviewOrder {

    // Instance data fields
    private final PreviewOrderRequest previewOrderRequest;

    /* Prevent instantiation outside the package */
    PreviewOrder(PreviewOrderRequest previewOrderRequest) {
        this.previewOrderRequest = previewOrderRequest;
    }

    public PreviewOrderRequest getOrderRequest() {
        return previewOrderRequest;
    }
}
