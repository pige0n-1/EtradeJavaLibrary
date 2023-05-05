package blue.etradeJavaLibrary.etradeObjects.alerts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class DeleteAlertsResponse extends EtradeObject<DeleteAlertsResponse> {

    // Instance data fields
    public String result;

    @Override
    public DeleteAlertsResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        result = extract("result");

        return this;
    }
}
