package blue.etradeJavaLibrary.etradeXMLModel.accountBalances;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class OpenCalls extends EtradeObject<OpenCalls> {

    // Instance data fields
    public BigDecimal minEquityCall;
    public BigDecimal fedCall;
    public BigDecimal cashCall;
    public BigDecimal houseCall;

    @Override
    public OpenCalls configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        minEquityCall = extractNumber("minEquityCall");
        fedCall = extractNumber("fedCall");
        cashCall = extractNumber("cashCall");
        houseCall = extractNumber("houseCall");

        return this;
    }
}
