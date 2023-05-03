package blue.etradeJavaLibrary.etradeXMLModel.listTransactions;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class TransactionListResponse extends EtradeObject<TransactionListResponse> {

    // Instance data fields
    public ArrayList<Transaction> transactions = new ArrayList<>();

    @Override
    public TransactionListResponse configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        var transactionsNodeList = getList("Transaction");

        // In the instance where there is only one transaction, Etrade does not label it with the <Transaction> tag
        if (transactionsNodeList.getLength() == 0) addTheOnlyTransaction();

        for (int i = 0; i < transactionsNodeList.getLength(); i++)
            transactions.add(extractObject(new Transaction()));

        return this;
    }


    // Private helper methods


    private void addTheOnlyTransaction() {
        transactions.add(extractObject(new Transaction(), "TransactionListResponse"));
    }
}
