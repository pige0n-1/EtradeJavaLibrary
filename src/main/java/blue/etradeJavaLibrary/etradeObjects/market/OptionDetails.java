package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OptionDetails extends EtradeObject<OptionDetails> {

    // Instance data fields
    public String optionCategory;
    public String optionRootSymbol;
    public Long timeStamp;
    public Boolean adjustedFlag;
    public String displaySymbol;
    public String optionType;
    public Double strikePrice;
    public String symbol;
    public Double bid;
    public Double ask;
    public Long bidSize;
    public Long askSize;
    public String inTheMoney;
    public Long volume;
    public Long openInterest;
    public Double netChange;
    public Double lastPrice;
    public String quoteDetail;
    public String osiKey;
    public OptionGreeks optionGreek;

    @Override
    public OptionDetails configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        optionCategory = extract("optionCategory");
        optionRootSymbol = extract("optionRootSymbol");
        timeStamp = extractLong("timeStamp");
        adjustedFlag = extractBoolean("adjustedFlag");
        displaySymbol = extract("displaySymbol");
        optionType = extract("optionType");
        strikePrice = extractDouble("strikePrice");
        symbol = extract("symbol");
        bid = extractDouble("bid");
        ask = extractDouble("ask");
        bidSize = extractLong("bidSize");
        askSize = extractLong("askSize");
        inTheMoney = extract("inTheMoney");
        volume = extractLong("volume");
        openInterest = extractLong("openInterest");
        netChange = extractDouble("netChange");
        lastPrice = extractDouble("lastPrice");
        quoteDetail = extract("quoteDetail");
        osiKey = extract("osiKey");
        optionGreek = extractObject(new OptionGreeks());

        return this;
    }
}
