package blue.etradeJavaLibrary.xml.transactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.xml.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction extends EtradeObject<Transaction> {

    // Instance data fields
    public long transactionId;
    public String accountId;
    public Date transactionDate;
    public Date postDate;
    public BigDecimal amount;
    public String description;
    public String transactionType;
    public boolean imageFlag;
    public String instType;
    public long storeId;
    public Category category;
    public Brokerage brokerage;

    @Override
    public Transaction configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        transactionId = extractLong("transactionId");
        accountId = extract("accountId");
        transactionDate = new Date(extractLong("transactionDate"));
        postDate = new Date(extractLong("postDate"));
        amount = extractNumber("amount");
        description = extract("description");
        transactionType = extract("transactionType");
        imageFlag = extractBoolean("imageFlag");
        instType = extract("instType");
        storeId = extractLong("storeId");
        category = extractObject(new Category());
        brokerage = extractObject(new Brokerage(), "brokerage");
        if (brokerage == null) // Etrade is inconsistent in their XML documents, so "brokerage" may be capitalized or not
            brokerage = extractObject(new Brokerage(), "Brokerage");

        return this;
    }
}
