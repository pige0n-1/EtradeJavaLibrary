package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class OptionExpireDateResponse extends EtradeObject<OptionExpireDateResponse> {

    // Instance data fields
    public ArrayList<ExpirationDate> expirationDates = new ArrayList<>();

    @Override
    public OptionExpireDateResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList(new ExpirationDate());
        for (int i = 0; i < nodeList.getLength(); i++)
            expirationDates.add(extractObject(new ExpirationDate(), nodeList.item(i)));

        return this;
    }
}
