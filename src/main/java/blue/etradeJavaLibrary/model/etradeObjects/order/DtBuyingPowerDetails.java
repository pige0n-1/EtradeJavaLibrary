package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class DtBuyingPowerDetails extends EtradeObject<DtBuyingPowerDetails> {

    // Instance data fields
    public OrderBuyPowerEffect nonMarginable;
    public OrderBuyPowerEffect marginable;

    @Override
    public DtBuyingPowerDetails configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        nonMarginable = extractObject(new OrderBuyPowerEffect(), "nonMarginable");
        marginable = extractObject(new OrderBuyPowerEffect(), "marginable");

        return this;
    }
}
