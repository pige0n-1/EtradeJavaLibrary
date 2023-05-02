package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class RealTimeValues extends EtradeObject<RealTimeValues> {

    // Instance data fields
    private Document xmlDocument;
    public BigDecimal totalAccountValue;
    public BigDecimal netMv;
    public BigDecimal netMvLong;
    public BigDecimal netMvShort;
    public BigDecimal totalLongValue;
}
