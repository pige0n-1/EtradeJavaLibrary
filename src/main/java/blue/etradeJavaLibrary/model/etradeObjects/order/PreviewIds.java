package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class PreviewIds extends EtradeObject<PreviewIds> {

    // Instance data fields
    public ArrayList<PreviewId> previewIds = new ArrayList<>();

    @Override
    public PreviewIds configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var nodeList = getList("previewId");
        for (int i = 0; i < nodeList.getLength(); i++)
            previewIds.add(extractObject(new PreviewId(), nodeList.item(i)));

        return this;
    }
}
