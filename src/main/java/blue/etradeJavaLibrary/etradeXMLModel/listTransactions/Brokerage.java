package blue.etradeJavaLibrary.etradeXMLModel.listTransactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.util.Date;

public class Brokerage extends EtradeObject<Brokerage> {

    // Instance data fields
    public Product product;
    public BigDecimal quantity;
    public BigDecimal price;
    public String settlementCurrency;
    public String paymentCurrency;
    public BigDecimal fee;
    public String memo;
    public String checkNo;
    public String orderNo;
    public Date settlementDate;

    @Override
    public Brokerage configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        product = extractObject(new Product());
        quantity = extractNumber("quantity");
        price = extractNumber("price");
        settlementCurrency = extract("settlementCurrency");
        paymentCurrency = extract("paymentCurrency");
        fee = extractNumber("fee");
        memo = extract("memo");
        checkNo = extract("checkNo");
        orderNo = extract("orderNo");
        settlementDate = new Date(extractLong("settlementDate"));

        return this;
    }
}
