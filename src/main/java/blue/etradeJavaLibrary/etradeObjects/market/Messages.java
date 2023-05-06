package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class Messages extends EtradeObject<Messages> {

    // Instance data fields
    public ArrayList<Message> message;

    @Override
    public Messages configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new Message());
        for (int i = 0; i < nodeList.getLength(); i++)
            message.add(extractObject(new Message(), nodeList.item(i)));

        return this;
    }
}
