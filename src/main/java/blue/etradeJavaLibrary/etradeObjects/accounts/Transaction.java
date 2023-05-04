package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Transaction extends EtradeObject<Transaction> {

    // Instance data fields
    public Long transactionId;
    public String accountId;
    public LocalDate transactionDate;
    public LocalDate postDate;
    public Double amount;
    public String description;
    public String transactionType;
    public Boolean imageFlag;
    public String instType;
    public Long storeId;
    public Category category;
    public Brokerage brokerage;

    @Override
    public Transaction configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        transactionId = extractLong("transactionId");
        accountId = extract("accountId");
        transactionDate = extractDate("transactionDate");
        postDate = extractDate("postDate");
        amount = extractDouble("amount");
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
