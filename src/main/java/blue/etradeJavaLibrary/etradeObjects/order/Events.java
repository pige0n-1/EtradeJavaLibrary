package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class Events extends EtradeObject<Events> {

    // Instance data fields
    public ArrayList<Event> event = new ArrayList<>();

    @Override
    public Events configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new Event());
        for (int i = 0; i < nodeList.getLength(); i++)
            event.add(extractObject(new Event(), nodeList.item(i)));

        return this;
    }
}
