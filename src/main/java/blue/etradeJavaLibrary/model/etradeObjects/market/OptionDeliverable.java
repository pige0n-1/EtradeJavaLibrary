package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OptionDeliverable extends EtradeObject<OptionDeliverable> {

    // Instance data fields
    public String rootSymbol;
    public String deliverableSymbol;
    public String deliverableTypeCode;
    public String deliverableExchangeCode;
    public Double deliverableStrikePercent;
    public Double deliverableCILShares;
    public Integer deliverableWholeShares;

    @Override
    public OptionDeliverable configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        rootSymbol = extract("rootSymbol");
        deliverableSymbol = extract("deliverableSymbol");
        deliverableTypeCode = extract("deliverableTypeCode");
        deliverableExchangeCode = extract("deliverableExchangeCode");
        deliverableStrikePercent = extractDouble("deliverableStrikePercent");
        deliverableCILShares = extractDouble("deliverableCILShares");
        deliverableWholeShares = extractInteger("deliverableWholeShares");

        return this;
    }
}
