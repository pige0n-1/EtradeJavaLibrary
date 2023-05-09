package blue.etradeJavaLibrary.model.etradeObjects.alerts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class AlertsResponse extends EtradeObject<AlertsResponse> {

    // Instance data fields
    public Long totalAlerts;
    public ArrayList<Alert> alerts = new ArrayList<>();

    @Override
    public AlertsResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {

        this.xmlDocument = xmlDocument;

        totalAlerts = extractLong("totalAlerts");
        var nodeList = getList(new Alert());

        if (nodeList == null) return this; // Etrade sends an empty string if there are no alerts

        for (int i = 0; i < nodeList.getLength(); i++)
            alerts.add(extractObject(new Alert(), nodeList.item(i)));

        return this;
    }
}
