
package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.*;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.ArrayList;

public class AccountsList extends ArrayList<Account>
        implements XMLDefinedObject<AccountsList> {
    
    public AccountsList() {}
    
    @Override
    public AccountsList processXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        NodeList listOfAccounts = xmlDocument.getElementsByTagName("Account");
        
        if (listOfAccounts.getLength() == 0)
            throw new ObjectMismatchException();
        
        for (int i = 0; i < listOfAccounts.getLength(); i++) {
            Node accountNode = listOfAccounts.item(i);
            Document accountXMLDoc = convertToDocument(accountNode);
            
            Account currentAccount = new Account().processXMLDocument(accountXMLDoc);
            
            add(currentAccount);
        }
        
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Accounts list:\n");
        
        for (Account account : this) {
            stringBuilder.append(account.toString()).append("\n");
        }
        
        return stringBuilder.toString();
    }
    
    
    // Private helper methods
    
    
    private Document convertToDocument(Node accountNode) {
        final boolean RECURSIVELY_IMPORT_NODE = true;
        
        try {
            DocumentBuilder documentBuilder = XMLDefinedObject.documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Node importedAccountNode = document.importNode(accountNode, RECURSIVELY_IMPORT_NODE);
            document.appendChild(importedAccountNode);
            
            return document;
        }
        catch (ParserConfigurationException ex) {
            return null;
        }
    }
}