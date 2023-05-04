package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class OpenCalls extends EtradeObject<OpenCalls> {

    // Instance data fields
    public Double minEquityCall;
    public Double fedCall;
    public Double cashCall;
    public Double houseCall;

    @Override
    public OpenCalls configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        minEquityCall = extractDouble("minEquityCall");
        fedCall = extractDouble("fedCall");
        cashCall = extractDouble("cashCall");
        houseCall = extractDouble("houseCall");

        return this;
    }
}
