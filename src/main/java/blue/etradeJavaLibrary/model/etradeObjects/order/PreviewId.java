package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class PreviewId extends EtradeObject<PreviewId> {

    // Instance data fields
    public Integer previewId;
    public String cashMargin;

    @Override
    public PreviewId configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        previewId = extractInteger("previewId");
        cashMargin = extract("cashMargin");

        return this;
    }
}
