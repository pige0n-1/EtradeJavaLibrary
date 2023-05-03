package blue.etradeJavaLibrary.etradeXMLModel.listTransactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class TransactionDetailsResponse extends EtradeObject<TransactionDetailsResponse> {

    // Instance data fields
    public Transaction transaction;

    @Override
    public TransactionDetailsResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        transaction = extractObject(new Transaction(), "TransactionDetailsResponse");

        return this;
    }
}
