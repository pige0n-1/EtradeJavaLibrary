package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Message extends EtradeObject<Message> {

    // Instance data fields
    public String description;
    public Integer code;
    public String type;

    @Override
    public Message configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        description = extract("hasMiniOptions");
        code = extractInteger("code");
        type = extract("type");

        return this;
    }
}
