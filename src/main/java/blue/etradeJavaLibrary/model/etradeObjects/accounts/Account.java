
package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.*;

import java.time.LocalDate;

/**
 * Represents an E*Trade account
 */
public class Account extends EtradeObject {
    
    // Instance data fields
    public Long accountId;
    public String accountIdKey;
    public String accountMode;
    public String accountDesc;
    public String accountName;
    public String accountType;
    public String institutionType;
    public String accountStatus;
    public LocalDate closedDate;
    public Boolean shareWorksAccount;
    public String shareWorksSource;
    
    /**
     * Creates an Account object that is not yet initialized.
     * To initialize the object, call the configureFromXMLDocument
     * with the XML response from Etrade.
     */
    public Account() {}
    
    @Override
    public Account configureFromXMLDocument(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
        
        accountId = extractLong("accountId");
        accountIdKey = extract("accountIdKey");;
        accountMode = extract("accountMode");;
        accountDesc = extract("accountDesc");;
        accountName = extract("accountName");;
        accountType = extract("accountType");;
        institutionType = extract("institutionType");;
        accountStatus = extract("accountStatus");;
        closedDate = extractDate("closedDate");
        shareWorksAccount = extractBoolean("shareWorksAccount");
        shareWorksSource = extract("shareWorksSource");
        
        return this;
    }
    
    @Override
    public Document getXMLDocument() {
        return xmlDocument;
    }
    
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject instanceof Account otherAccount) 
            return otherAccount.accountIdKey.equals(this.accountIdKey);
        
        else return false;
    }
    
    @Override
    public int hashCode() {
        return accountIdKey.hashCode();
    }
}
