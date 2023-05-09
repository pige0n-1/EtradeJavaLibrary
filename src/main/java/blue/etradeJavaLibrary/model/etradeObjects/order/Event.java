package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Event extends EtradeObject<Event> {

    // Instance data fields
    public String name;
    public LocalDateTime dateTime;
    public Integer orderNumber;
    public ArrayList<Instrument> instrument = new ArrayList<>();

    @Override
    public Event configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        name = extract("name");
        dateTime = extractTime("dateTime");
        orderNumber = extractInteger("orderNumber");
        var nodeList = getList(new Instrument());
        for (int i = 0; i < nodeList.getLength(); i++)
            instrument.add(extractObject(new Instrument(), nodeList.item(i)));

        return this;
    }
}
