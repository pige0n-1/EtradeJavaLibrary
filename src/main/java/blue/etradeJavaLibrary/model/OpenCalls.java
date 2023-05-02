package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class OpenCalls extends EtradeObject<OpenCalls> {

    // Instance data fields
    private Document xmlDocument;
    public BigDecimal minEquityCall;
    public BigDecimal fedCall;
    public BigDecimal cashCall;
    public BigDecimal houseCall;
}
