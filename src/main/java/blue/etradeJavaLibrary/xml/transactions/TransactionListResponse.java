package blue.etradeJavaLibrary.xml.transactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.xml.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class TransactionListResponse extends EtradeObject<TransactionListResponse> {

    // Instance data fields
    public ArrayList<Transaction> transactions = new ArrayList<>();

    @Override
    public TransactionListResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var transactionsNodeList = getList("Transaction");

        for (int i = 0; i < transactionsNodeList.getLength(); i++)
            transactions.add(extractObject(new Transaction(), transactionsNodeList.item(i)));

        return this;
    }
}
