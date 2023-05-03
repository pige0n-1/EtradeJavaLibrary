package blue.etradeJavaLibrary.etradeXMLModel.accountBalances;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Lending extends EtradeObject<Lending> {

    // Instance data fields
    public BigDecimal currentBalance;
    public BigDecimal creditLine;
    public BigDecimal outstandingBalance;
    public BigDecimal minPaymentDue;
    public BigDecimal amountPastDue;
    public BigDecimal availableCredit;
    public BigDecimal ytdInterestPaid;
    public BigDecimal lastYtdInterestPaid;
    public long paymentDueDate;
    public long lastPaymentReceivedDate;
    public BigDecimal paymentReceivedMtd;

    @Override
    public Lending configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        currentBalance = extractNumber("currentBalance");
        creditLine = extractNumber("creditLine");
        outstandingBalance = extractNumber("outstandingBalance");
        minPaymentDue = extractNumber("minPaymentDue");
        amountPastDue = extractNumber("amountPastDue");
        availableCredit = extractNumber("availableCredit");
        ytdInterestPaid = extractNumber("ytdInterestPaid");
        lastYtdInterestPaid = extractNumber("lastYtdInterestPaid");
        paymentDueDate = extractLong("paymentDueDate");
        lastPaymentReceivedDate = extractLong("lastPaymentReceivedDate");
        paymentReceivedMtd = extractNumber("paymentReceivedMtd");

        return this;
    }
}
