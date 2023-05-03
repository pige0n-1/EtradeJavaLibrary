package blue.etradeJavaLibrary.xml.account;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.xml.EtradeObject;
import org.w3c.dom.Document;

import java.util.Date;

public class BalanceResponse extends EtradeObject<BalanceResponse> {

    // Instance data fields
    public String accountId;
    public String institutionType;
    public Date asOfDate;
    public String accountType;
    public String optionLevel;
    public String accountDescription;
    public int quoteMode;
    public String dayTraderStatus;
    public String accountMode;
    public String accountDesc;
    public OpenCalls openCalls;
    public Cash cash;
    public Margin margin;
    public Lending lending;
    public Computed computed;

    @Override
    public BalanceResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        accountId = extract("accountId");
        institutionType = extract("institutionType");
        asOfDate = new Date(extractLong("asOfDate"));
        accountType = extract("accountType");
        optionLevel = extract("optionLevel");
        accountDescription = extract("accountDescription");
        quoteMode = extractInt("quoteMode");
        dayTraderStatus = extract("dayTraderStatus");
        accountMode = extract("accountMode");
        accountDesc = extract("accountDesc");
        openCalls = extractObject(new OpenCalls());
        cash = extractObject(new Cash());
        margin = extractObject(new Margin());
        lending = extractObject(new Lending());
        computed = extractObject(new Computed());

        return this;
    }
}
