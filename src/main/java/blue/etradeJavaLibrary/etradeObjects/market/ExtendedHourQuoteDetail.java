package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDateTime;

public class ExtendedHourQuoteDetail extends EtradeObject<ExtendedHourQuoteDetail> {

    // Instance data fields
    public Double lastPrice;
    public Double change;
    public Double percentChange;
    public Double bid;
    public Long bidSize;
    public Double ask;
    public Long askSize;
    public Long volume;
    public LocalDateTime timeOfLastTrade;
    public String timeZone;
    public String quoteStatus;

    @Override
    public ExtendedHourQuoteDetail configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        lastPrice = extractDouble("lastPrice");
        change = extractDouble("change");
        percentChange = extractDouble("percentChange");
        bid = extractDouble("bid");
        bidSize = extractLong("bidSize");
        ask = extractDouble("ask");
        askSize = extractLong("askSize");
        volume = extractLong("volume");
        timeOfLastTrade = extractTime("timeOfLastTrade");
        timeZone = extract("timeZone");
        quoteStatus = extract("quoteStatus");

        return this;
    }
}
