package blue.etradeJavaLibrary.xml.transactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.xml.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Product extends EtradeObject<Product> {

    // Instance data fields
    public String symbol;
    public String securityType;
    public String securitySubType;
    public String callPut;
    public int expiryYear;
    public int expiryMonth;
    public int expiryDay;
    public BigDecimal strikePrice;
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
        strikePrice = extractNumber("strikePrice");
        expiryType = extract("expiryType");
        productId = extractObject(new ProductId());

        return this;
    }
}
