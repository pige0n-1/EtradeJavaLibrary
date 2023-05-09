package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import org.w3c.dom.Document;

public class ProductId extends EtradeObject<ProductId>
        implements XMLBuildable {

    // Instance data fields
    public String symbol;
    public String typeCode;

    @Override
    public ProductId configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        symbol = extract("symbol");
        typeCode = extract("typeCode");

        return this;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("symbol", symbol)
                .addStringField("typeCode", typeCode);
    }

    @Override
    public String getXMLClassName() {
        return "productId";
    }
}
