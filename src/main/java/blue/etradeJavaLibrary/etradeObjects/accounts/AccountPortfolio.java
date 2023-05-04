package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class AccountPortfolio extends EtradeObject<AccountPortfolio> {

    // Instance data fields
    public String accountId;
    public String next;
    public Integer totalNoOfPages;
    public String nextPageNo;
    public ArrayList<Position> position;

    @Override
    public AccountPortfolio configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        accountId = extract("accountId");
        next = extract("next");
        totalNoOfPages = extractInteger("totalNoOfPages");
        nextPageNo = extract("nextPageNo");
        var positionNodeList = getList(new Position());
        for (int i = 0; i < positionNodeList.getLength(); i++)
            position.add(extractObject(new Position(), positionNodeList.item(i)));

        return this;
    }
}
