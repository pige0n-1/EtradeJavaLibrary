package blue.etradeJavaLibrary.etradeXMLModel.listAccounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

public class AccountsListResponse extends EtradeObject<AccountsListResponse> {

    // Instance data fields
    public Accounts accounts;

    @Override
    public AccountsListResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        accounts = extractObject(new Accounts());

        return this;
    }
}
