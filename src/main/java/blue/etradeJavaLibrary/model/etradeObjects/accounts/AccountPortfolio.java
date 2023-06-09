package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class AccountPortfolio extends EtradeObject<AccountPortfolio> {

    // Instance data fields
    public Long accountId;
    public String next;
    public Integer totalNoOfPages;
    public String nextPageNo;
    public ArrayList<Position> position = new ArrayList<>();

    @Override
    public AccountPortfolio configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        accountId = extractLong("accountId");
        next = extract("next");
        totalNoOfPages = extractInteger("totalPages");
        nextPageNo = extract("nextPageNo");
        var positionNodeList = getList(new Position());
        for (int i = 0; i < positionNodeList.getLength(); i++)
            position.add(extractObject(new Position(), positionNodeList.item(i)));

        return this;
    }
}
