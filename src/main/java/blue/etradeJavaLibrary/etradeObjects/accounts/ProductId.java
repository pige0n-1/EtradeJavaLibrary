package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class ProductId extends EtradeObject<ProductId> {

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
}