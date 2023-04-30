
package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import org.w3c.dom.*;
import java.util.ArrayList;

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
        accountId = xmlDocument.getElementById("accountId").getTextContent();
        accountIdKey = xmlDocument.getElementById("accountIdKey").getTextContent();
        accountMode = xmlDocument.getElementById("accountMode").getTextContent();
        accountDesc = xmlDocument.getElementById("accountDesc").getTextContent();
        accountName = xmlDocument.getElementById("accountName").getTextContent();
        accountType = xmlDocument.getElementById("accountType").getTextContent();
        institutionType = xmlDocument.getElementById("institutionType").getTextContent();
        accountStatus = xmlDocument.getElementById("accountStatus").getTextContent();
        closedDate = Long.parseLong(xmlDocument.getElementById("closedDate").getTextContent());
        shareWorksAccount = Boolean.parseBoolean(xmlDocument.getElementById("shareWorksAccount").getTextContent());
        shareWorksSource = xmlDocument.getElementById("shareWorksSource").getTextContent();
        
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("E*Trade account:\n");
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
        stringBuilder.append("shareWorksSource: ").append(shareWorksSource).append("\n");
        
        return stringBuilder.toString();
    }
}
