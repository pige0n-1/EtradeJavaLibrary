package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Lending extends EtradeObject<Lending> {

    // Instance data fields
    private Document xmlDocument;
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
}
