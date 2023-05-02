package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class BalanceResponse extends EtradeObject<BalanceResponse> {

    // Instance data fields
    public String accountId;
    public String institutionType;
    public long asOfDate;
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
    public ComputedBalance computedBalance;

    @Override
    public BalanceResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        accountId = extract("accountId");
        institutionType = extract("institutionType");
        asOfDate = extractLong("asOfDate");
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
        computedBalance = extractObject(new ComputedBalance());

        return this;
    }
}
