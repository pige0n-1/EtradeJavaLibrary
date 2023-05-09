package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class CashBuyingPowerDetails extends EtradeObject<CashBuyingPowerDetails> {

    // Instance data fields
    public OrderBuyPowerEffect settled; // TODO: find out how these are labeled
    public OrderBuyPowerEffect settledUnsettled;

    @Override
    public CashBuyingPowerDetails configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        settled = extractObject(new OrderBuyPowerEffect(), "settled");
        settled = extractObject(new OrderBuyPowerEffect(), "settledUnsettled");

        return this;
    }
}
