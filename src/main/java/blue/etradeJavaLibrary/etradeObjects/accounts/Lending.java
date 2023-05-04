package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.util.Date;

public class Lending extends EtradeObject<Lending> {

    // Instance data fields
    public Double currentBalance;
    public Double creditLine;
    public Double outstandingBalance;
    public Double minPaymentDue;
    public Double amountPastDue;
    public Double availableCredit;
    public Double ytdInterestPaid;
    public Double lastYtdInterestPaid;
    public Date paymentDueDate;
    public Date lastPaymentReceivedDate;
    public Double paymentReceivedMtd;

    @Override
    public Lending configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        currentBalance = extractDouble("currentBalance");
        creditLine = extractDouble("creditLine");
        outstandingBalance = extractDouble("outstandingBalance");
        minPaymentDue = extractDouble("minPaymentDue");
        amountPastDue = extractDouble("amountPastDue");
        availableCredit = extractDouble("availableCredit");
        ytdInterestPaid = extractDouble("ytdInterestPaid");
        lastYtdInterestPaid = extractDouble("lastYtdInterestPaid");
        paymentDueDate = extractDate("paymentDueDate");
        lastPaymentReceivedDate = extractDate("lastPaymentReceivedDate");
        paymentReceivedMtd = extractDouble("paymentReceivedMtd");

        return this;
    }
}
