package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Data extends EtradeObject<Data> {

    // Instance data fields
    public String symbol;
    public String description;
    public String type;

    @Override
    public Data configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        symbol = extract("symbol");
        description = extract("description");
        type = extract("type");

        return this;
    }
}
