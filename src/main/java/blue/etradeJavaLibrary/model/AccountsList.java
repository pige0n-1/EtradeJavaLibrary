
package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.*;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Represents an E*Trade HTTP response of a list of accounts.
 * It is defined in an XML response.
 */
public class AccountsList extends ArrayList<Account>
        implements XMLDefinedObject<AccountsList> {
    
    // Instance data fields
    private Document xmlDocument;
    
    /**
     * Creates an AccountsList object that is not yet initialized.
     * To initialize, call the configureFromXMLDocument method
     * with the response document from Etrade.
     */
    public AccountsList() {}
    
    @Override
    public AccountsList configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;
        
        NodeList listOfAccounts = xmlDocument.getElementsByTagName("Account");
        
        if (listOfAccounts.getLength() == 0) throw new ObjectMismatchException();
        
        for (int i = 0; i < listOfAccounts.getLength(); i++) {
            Node accountNode = listOfAccounts.item(i);
            Document accountXMLDoc = convertToDocument(accountNode);
            
            Account currentAccount = new Account().configureFromXMLDocument(accountXMLDoc);
            
            add(currentAccount);
        }
        
        return this;
    }
    
    @Override
    public Document toXMLDocument() {
        return xmlDocument;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Accounts list:\n");
        
        for (Account account : this)
            stringBuilder.append(account.toString()).append("\n");
        
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