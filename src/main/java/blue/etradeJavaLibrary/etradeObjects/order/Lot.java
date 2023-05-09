package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Lot extends EtradeObject<Lot> {

    // Instance data fields
    public Long id;
    public Double size;

    @Override
    public Lot configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        id = extractLong("id");
        size = extractDouble("size");

        return this;
    }
}
