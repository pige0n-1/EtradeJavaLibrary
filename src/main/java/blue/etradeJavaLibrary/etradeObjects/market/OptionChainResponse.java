package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class OptionChainResponse extends EtradeObject<OptionChainResponse> {

    // Instance data fields
    public ArrayList<OptionPair> optionPairs = new ArrayList<>();
    public Long timeStamp;
    public String quoteType;
    public Double nearPrice;
    public SelectedED selected;

    @Override
    public OptionChainResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new OptionPair());
        for (int i = 0; i < nodeList.getLength(); i++)
            optionPairs.add(extractObject(new OptionPair(), nodeList.item(i)));
        timeStamp = extractLong("timeStamp");
        quoteType = extract("quoteType");
        nearPrice = extractDouble("nearPrice");
        selected = extractObject(new SelectedED());

        return this;
    }
}
