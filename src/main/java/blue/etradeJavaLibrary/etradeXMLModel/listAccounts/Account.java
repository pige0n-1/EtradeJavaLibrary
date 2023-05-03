
package blue.etradeJavaLibrary.etradeXMLModel.listAccounts;

import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.*;

/**
 * Represents an E*Trade account
 */
public class Account extends EtradeObject {
    
    // Instance data fields
    public String accountId;
    public String accountIdKey;
    public String accountMode;
    public String accountDesc;
    public String accountName;
    public String accountType;
    public String institutionType;
    public String accountStatus;
    public long closedDate;
    public boolean shareWorksAccount;
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
        
        accountId = extract("accountId");
        accountIdKey = extract("accountIdKey");;
        accountMode = extract("accountMode");;
        accountDesc = extract("accountDesc");;
        accountName = extract("accountName");;
        accountType = extract("accountType");;
        institutionType = extract("institutionType");;
        accountStatus = extract("accountStatus");;
        closedDate = extractLong("closedDate");
        shareWorksAccount = extractBoolean("shareWorksAccount");
        shareWorksSource = extract("shareWorksSource");
        
        return this;
    }
    
    @Override
    public Document toXMLDocument() {
        return xmlDocument;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Account:\n");
        stringBuilder.append("accountId: ").append(accountId).append("\n");
        stringBuilder.append("accountIdKey: ").append(accountIdKey).append("\n");
        stringBuilder.append("accountMode: ").append(accountMode).append("\n");
        stringBuilder.append("accountDesc: ").append(accountDesc).append("\n");
        stringBuilder.append("accountName: ").append(accountName).append("\n");
        stringBuilder.append("accountType: ").append(accountType).append("\n");
        stringBuilder.append("institutionType: ").append(institutionType).append("\n");
        stringBuilder.append("accountStatus: ").append(accountStatus).append("\n");
        stringBuilder.append("closedDate: ").append(closedDate).append("\n");
        stringBuilder.append("shareWorksAccount: ").append(shareWorksAccount).append("\n");
        stringBuilder.append("shareWorksSource: ").append(shareWorksSource);
        
        return stringBuilder.toString();
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
