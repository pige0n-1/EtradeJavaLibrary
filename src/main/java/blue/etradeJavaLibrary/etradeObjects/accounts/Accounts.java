
package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.*;
import java.util.ArrayList;

import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.*;

/**
 * Represents an E*Trade HTTP response of a list of accounts.
 * It is defined in an XML response.
 */
public class Accounts extends EtradeObject<Accounts> {
    
    // Instance data fields
    public ArrayList<Account> accounts = new ArrayList<>();
    
    @Override
    public Accounts configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var listOfAccounts = getList(new Account());
        if (listOfAccounts.getLength() == 0) throw new ObjectMismatchException();

        for (int i = 0; i < listOfAccounts.getLength(); i++)
            accounts.add(extractObject(new Account(), listOfAccounts.item(i)));
        
        return this;
    }
}
