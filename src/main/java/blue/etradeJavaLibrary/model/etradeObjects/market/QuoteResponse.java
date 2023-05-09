package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class QuoteResponse extends EtradeObject<QuoteResponse> {

    // Instance data fields
    public ArrayList<QuoteData> quoteData = new ArrayList<>();
    public Messages messages;

    @Override
    public QuoteResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new QuoteData());
        for (int i = 0; i < nodeList.getLength(); i++)
            quoteData.add(extractObject(new QuoteData(), nodeList.item(i)));
        messages = extractObject(new Messages());

        return this;
    }
}
