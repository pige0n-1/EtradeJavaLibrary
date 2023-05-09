package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import org.w3c.dom.Document;

public class PreviewId extends EtradeObject<PreviewId>
        implements XMLBuildable {

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

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("previewId", previewId) // TODO: Ensure previewId isn't being written twice
                .addStringField("cashMargin", cashMargin);
    }

    @Override
    public String getXMLClassName() {
        return "previewId";
    }
}
