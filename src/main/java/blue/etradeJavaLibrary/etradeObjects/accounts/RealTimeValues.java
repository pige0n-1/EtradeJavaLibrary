package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class RealTimeValues extends EtradeObject<RealTimeValues> {

    // Instance data fields
    public Double totalAccountValue;
    public Double netMv;
    public Double netMvLong;
    public Double netMvShort;
    public Double totalLongValue;

    @Override
    public RealTimeValues configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        totalAccountValue = extractDouble("totalAccountValue");
        netMv = extractDouble("netMv");
        netMvLong = extractDouble("netMvLong");
        netMvShort = extractDouble("netMvShort");
        totalLongValue = extractDouble("totalLongValue");

        return this;
    }
}
