package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OptionPair extends EtradeObject<OptionPair> {

    // Instance data fields
    public OptionDetails call;
    public OptionDetails put;
    public String pairType;

    @Override
    public OptionPair configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        call = extractObject(new OptionDetails(), "Call");
        put = extractObject(new OptionDetails(), "Put");
        pairType = extract("pairType");

        return this;
    }
}
