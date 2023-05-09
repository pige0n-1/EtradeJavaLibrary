package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class PortfolioResponse extends EtradeObject<PortfolioResponse> {

    // Instance data fields
    public Totals totals;
    public ArrayList<AccountPortfolio> accountPortfolio = new ArrayList<>();

    @Override
    public PortfolioResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        totals = extractObject(new Totals());
        var nodeList = getList(new AccountPortfolio());
        for (int i = 0; i < nodeList.getLength(); i++)
            accountPortfolio.add(extractObject(new AccountPortfolio(), nodeList.item(i)));

        return this;
    }
}
