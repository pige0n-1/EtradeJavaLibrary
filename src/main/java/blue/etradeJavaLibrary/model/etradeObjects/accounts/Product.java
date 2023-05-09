package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import org.w3c.dom.Document;

public class Product extends EtradeObject<Product>
        implements XMLBuildable {

    // Instance data fields
    public String symbol;
    public String securityType;
    public String securitySubType;
    public String callPut;
    public Integer expiryYear;
    public Integer expiryMonth;
    public Integer expiryDay;
    public Double strikePrice;
    public String expiryType;
    public ProductId productId;

    @Override
    public Product configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        symbol = extract("symbol");
        securityType = extract("securityType");
        securitySubType = extract("securitySubType");
        callPut = extract("callPut");
        expiryYear = extractInteger("expiryYear");
        expiryMonth = extractInteger("expiryMonth");
        expiryDay = extractInteger("expiryDay");
        strikePrice = extractDouble("strikePrice");
        expiryType = extract("expiryType");
        productId = extractObject(new ProductId(), "productId");

        return this;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("symbol", symbol)
                .addStringField("securityType", securityType)
                .addStringField("callPut", callPut)
                .addStringField("expiryYear", expiryYear)
                .addStringField("expiryMonth", expiryMonth)
                .addStringField("expiryDay", expiryDay)
                .addStringField("strikePrice", strikePrice)
                .addStringField("expiryType", expiryType)
                .addXMLObjectField("productId", productId);
    }

    @Override
    public String getXMLClassName() {
        return "Product";
    }
}
