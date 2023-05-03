package blue.etradeJavaLibrary.etradeXMLModel.listTransactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

public class Category extends EtradeObject<Category> {

    // Instance data fields
    public String categoryId;
    public String parentId;
    public String categoryName;
    public String parentName;


    @Override
    public Category configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        categoryId = extract("categoryId");
        parentId = extract("parentId");
        categoryName = extract("categoryName");
        parentName = extract("parentName");

        return this;
    }
}
