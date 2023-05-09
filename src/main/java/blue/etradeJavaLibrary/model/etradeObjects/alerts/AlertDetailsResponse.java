package blue.etradeJavaLibrary.model.etradeObjects.alerts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDateTime;

public class AlertDetailsResponse extends EtradeObject<AlertDetailsResponse> {

    // Instance data fields
    public Long id;
    public LocalDateTime createTime;
    public String subject;
    public String msgText;
    public LocalDateTime readTime;
    public LocalDateTime deleteTime;
    public String symbol;
    public String next;
    public String prev;

    @Override
    public AlertDetailsResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        id = extractLong("id");
        createTime = extractTime("createTime");
        subject = extract("subject");
        msgText = extract("msgText");
        readTime = extractTime("readTime");
        deleteTime = extractTime("deleteTime");
        symbol = extract("symbol");
        next = extract("next");
        prev = extract("prev");

        return this;
    }
}
