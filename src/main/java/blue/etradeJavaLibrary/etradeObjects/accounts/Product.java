package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Product extends EtradeObject<Product> {

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
        expiryYear = extractInt("expiryYear");
        expiryMonth = extractInt("expiryMonth");
        expiryDay = extractInt("expiryDay");
        strikePrice = extractDouble("strikePrice");
        expiryType = extract("expiryType");
        productId = extractObject(new ProductId());

        return this;
    }
}
