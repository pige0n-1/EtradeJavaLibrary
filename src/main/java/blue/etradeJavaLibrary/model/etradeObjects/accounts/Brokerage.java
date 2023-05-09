package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDate;

public class Brokerage extends EtradeObject<Brokerage> {

    // Instance data fields
    public Product product;
    public Double quantity;
    public Double price;
    public String settlementCurrency;
    public String paymentCurrency;
    public Double fee;
    public String memo;
    public String checkNo;
    public String orderNo;
    public LocalDate settlementDate;

    @Override
    public Brokerage configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        product = extractObject(new Product());
        quantity = extractDouble("quantity");
        price = extractDouble("price");
        settlementCurrency = extract("settlementCurrency");
        paymentCurrency = extract("paymentCurrency");
        fee = extractDouble("fee");
        memo = extract("memo");
        checkNo = extract("checkNo");
        orderNo = extract("orderNo");
        settlementDate = extractDate("settlementDate");

        return this;
    }
}