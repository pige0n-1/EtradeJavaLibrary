package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class LookupResponse extends EtradeObject<LookupResponse> {

    // Instance data fields
    public ArrayList<Data> data = new ArrayList<>();

    @Override
    public LookupResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new Data());
        for (int i = 0; i < nodeList.getLength(); i++)
            data.add(extractObject(new Data(), nodeList.item(i)));

        return this;
    }
}
