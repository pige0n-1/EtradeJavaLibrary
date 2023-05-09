package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OptionQuoteDetails extends EtradeObject<OptionQuoteDetails> {

    // Instance data fields
    public Double ask;
    public Long askSize;
    public Double bid;
    public Long bidSize;
    public String companyName;
    public Long daysToExpiration;
    public Double lastTrade;
    public Long openInterest;
    public Double optionPreviousBidPrice;
    public Double optionPreviousAskPrice;
    public String osiKey;
    public Double intrinsicValue;
    public Double timePremium;
    public Double optionMultiplier;
    public Double contractSize;
    public String symbolDescription;
    public OptionGreeks optionGreeks;

    @Override
    public OptionQuoteDetails configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        ask = extractDouble("ask");
        askSize = extractLong("askSize");
        bid = extractDouble("bid");
        bidSize = extractLong("bidSize");
        companyName = extract("companyName");
        daysToExpiration = extractLong("daysToExpiration");
        lastTrade = extractDouble("lastTrade");
        openInterest = extractLong("openInterest");
        optionPreviousBidPrice = extractDouble("optionPreviousBidPrice");
        optionPreviousAskPrice = extractDouble("optionPreviousAskPrice");
        osiKey = extract("osiKey");
        intrinsicValue = extractDouble("intrinsicValue");
        timePremium = extractDouble("timePremium");
        optionMultiplier = extractDouble("optionMultiplier");
        contractSize = extractDouble("contractSize");
        symbolDescription = extract("symbolDescription");
        optionGreeks = extractObject(new OptionGreeks());

        return this;
    }
}
