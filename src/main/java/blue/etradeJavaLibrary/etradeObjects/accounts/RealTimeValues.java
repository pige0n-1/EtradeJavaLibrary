package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class RealTimeValues extends EtradeObject<RealTimeValues> {

    // Instance data fields
    public BigDecimal totalAccountValue;
    public BigDecimal netMv;
    public BigDecimal netMvLong;
    public BigDecimal netMvShort;
    public BigDecimal totalLongValue;

    @Override
    public RealTimeValues configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        totalAccountValue = extractNumber("totalAccountValue");
        netMv = extractNumber("netMv");
        netMvLong = extractNumber("netMvLong");
        netMvShort = extractNumber("netMvShort");
        totalLongValue = extractNumber("totalLongValue");

        return this;
    }
}
