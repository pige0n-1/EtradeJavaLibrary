
package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.*;

public class Account 
        implements XMLDefinedObject<Account> {
    
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
    
    public Account() {}
    
    @Override
    public Account processXMLDocument(Document xmlDocument) {
        accountId = xmlDocument.getElementsByTagName("accountId").item(0).getTextContent();
        accountIdKey = xmlDocument.getElementsByTagName("accountIdKey").item(0).getTextContent();
        accountMode = xmlDocument.getElementsByTagName("accountMode").item(0).getTextContent();
        accountDesc = xmlDocument.getElementsByTagName("accountDesc").item(0).getTextContent();
        accountName = xmlDocument.getElementsByTagName("accountName").item(0).getTextContent();
        accountType = xmlDocument.getElementsByTagName("accountType").item(0).getTextContent();
        institutionType = xmlDocument.getElementsByTagName("institutionType").item(0).getTextContent();
        accountStatus = xmlDocument.getElementsByTagName("accountStatus").item(0).getTextContent();
        closedDate = Long.parseLong(xmlDocument.getElementsByTagName("closedDate").item(0).getTextContent());
        shareWorksAccount = Boolean.parseBoolean(xmlDocument.getElementsByTagName("shareWorksAccount").item(0).getTextContent());
        
        if (shareWorksAccount)
            shareWorksSource = xmlDocument.getElementsByTagName("shareWorksSource").item(0).getTextContent();
        
        return this;
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
}
