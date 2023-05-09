package blue.etradeJavaLibrary.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class Lots extends EtradeObject<Lots> {

    // Instance data fields
    public ArrayList<Lot> lot = new ArrayList<>();

    @Override
    public Lots configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new Lot());
        for (int i = 0; i < nodeList.getLength(); i++)
            lot.add(extractObject(new Lot(), nodeList.item(i)));

        return this;
    }
}
