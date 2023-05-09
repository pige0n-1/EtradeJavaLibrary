package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import blue.etradeJavaLibrary.model.etradeObjects.accounts.Product;
import org.w3c.dom.Document;

public class Instrument extends EtradeObject<Instrument>
        implements XMLBuildable {

    // Instance data fields
    public Product product;
    public String symbolDescription;
    public String orderAction;
    public String quantityType;
    public Double quantity;
    public Double cancelQuantity;
    public Double orderedQuantity;
    public Double filledQuantity;
    public Double averageExecutionPrice;
    public Double estimatedCommission;
    public Double estimatedFees;
    public Double bid;
    public Double ask;
    public Double lastprice;
    public String currency;
    public Lots lots;
    public MFQuantity mfQuantity;
    public String osiKey;
    public String mfTransaction;
    public Boolean reserveOrder;
    public Double reserveQuantity;

    @Override
    public Instrument configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        product = extractObject(new Product());
        symbolDescription = extract("symbolDescription");
        orderAction = extract("orderAction");
        quantityType = extract("quantityType");
        quantity = extractDouble("quantity");
        cancelQuantity = extractDouble("cancelQuantity");
        orderedQuantity = extractDouble("orderedQuantity");
        filledQuantity = extractDouble("filledQuantity");
        averageExecutionPrice = extractDouble("averageExecutionPrice");
        estimatedCommission = extractDouble("estimatedCommission");
        estimatedFees = extractDouble("estimatedFees");
        bid = extractDouble("bid");
        ask = extractDouble("ask");
        lastprice = extractDouble("lastprice");
        currency = extract("currency");
        lots = extractObject(new Lots());
        mfQuantity = extractObject(new MFQuantity());
        osiKey = extract("osiKey");
        mfTransaction = extract("mfTransaction");
        reserveOrder = extractBoolean("reserveOrder");
        reserveQuantity = extractDouble("reserveQuantity");

        return this;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addXMLObjectField("Product", product)
                .addStringField("orderAction", orderAction)
                .addStringField("quantityType", quantityType)
                .addStringField("quantity", quantity)
                .addStringField("cancelQuantity", cancelQuantity)
                .addStringField("currency", currency)
                .addStringField("osiKey", osiKey)
                .addStringField("reserveOrder", reserveOrder)
                .addStringField("reserveQuantity", reserveQuantity);
    }

    @Override
    public String getXMLClassName() {
        return "Instrument";
    }
}
