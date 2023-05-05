package blue.etradeJavaLibrary.etradeObjects.alerts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDateTime;

public class Alert extends EtradeObject<Alert> {

    // Instance data fields
    public Long id;
    public LocalDateTime createTime;
    public String subject;
    public String status;

    @Override
    public Alert configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        id = extractLong("id");
        createTime = extractTime("createTime");
        subject = extract("subject");
        status = extract("status");

        return this;
    }
}
