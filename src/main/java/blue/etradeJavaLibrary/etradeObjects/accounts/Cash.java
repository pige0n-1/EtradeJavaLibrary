package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Cash extends EtradeObject<Cash> {

    // Instance data fields
    public BigDecimal fundsForOpenOrdersCash;
    public BigDecimal moneyMktBalance;


    @Override
    public Cash configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        fundsForOpenOrdersCash = extractNumber("fundsForOpenOrdersCash");
        moneyMktBalance = extractNumber("moneyMktBalance");

        return this;
    }
}
