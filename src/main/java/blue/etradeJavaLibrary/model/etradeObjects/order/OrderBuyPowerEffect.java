package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OrderBuyPowerEffect extends EtradeObject<OrderBuyPowerEffect> {

    // Instance data fields
    public Double currentBp;
    public Double currentOor;
    public Double currentNetBp;
    public Double currentOrderImpact;
    public Double netBp;

    @Override
    public OrderBuyPowerEffect configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        currentBp = extractDouble("currentBp");
        currentOor = extractDouble("currentOor");
        currentNetBp = extractDouble("currentNetBp");
        currentOrderImpact = extractDouble("currentOrderImpact");
        netBp = extractDouble("netBp");

        return this;
    }
}
