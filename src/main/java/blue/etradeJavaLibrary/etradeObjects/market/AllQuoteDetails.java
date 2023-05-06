package blue.etradeJavaLibrary.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AllQuoteDetails extends EtradeObject<AllQuoteDetails> {

    // Instance data fields
    public Boolean adjustedFlag;
    public Double ask;
    public Long askSize;
    public String askTime;
    public Double bid;
    public String bidExchange;
    public Long bidSize;
    public String bidTime;
    public Double changeClose;
    public Double changeClosePercentage;
    public String companyName;
    public Long daysToExpiration;
    public String dirLast;
    public Double dividend;
    public Double eps;
    public Double estEarnings;
    public LocalDate exDividendDate;
    public Double high;
    public Double high52;
    public Double lastTrade;
    public Double low;
    public Double low52;
    public Double open;
    public Long openInterest;
    public String optionStyle;
    public String optionUnderlier;
    public String optionUnderlierExchange;
    public Double previousClose;
    public Long previousDayVolume;
    public String primaryExchange;
    public String symbolDescription;
    public Long totalVolume;
    public Long upc;
    public ArrayList<OptionDeliverable> optionDeliverableList;
    public Double cashDeliverable;
    public Double marketCap;
    public Double sharesOutstanding;
    public String nextEarningDate;
    public Double beta;
    public Double yield;
    public Double declaredDividend;
    public Long dividendPayableDate;
    public Double pe;
    public LocalDate week52LowDate;
    public LocalDate week52HiDate;
    public Double intrinsicValue;
    public Double timePremium;
    public Double optionMultiplier;
    public Double contractSize;
    public LocalDate expirationDate;
    public ExtendedHourQuoteDetail ehQuote;
    public Double optionPreviousBidPrice;
    public Double optionPreviousAskPrice;
    public String osiKey;
    public LocalDateTime timeOfLastTrade;
    public Long averageVolume;

    @Override
    public AllQuoteDetails configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        adjustedFlag = extractBoolean("adjustedFlag");
        ask = extractDouble("ask");
        askSize = extractLong("askSize");
        askTime = extract("askTime");
        bid = extractDouble("bid");
        bidExchange = extract("bidExchange");
        bidSize = extractLong("bidSize");
        bidTime = extract("bidTime");
        changeClose = extractDouble("changeClose");
        changeClosePercentage = extractDouble("changeClosePercentage");
        companyName = extract("companyName");
        daysToExpiration = extractLong("daysToExpiration");
        dirLast = extract("dirLast");
        dividend = extractDouble("dividend");
        eps = extractDouble("eps");
        estEarnings = extractDouble("estEarnings");
        exDividendDate = extractDate("exDividendDate");
        high = extractDouble("high");
        high52 = extractDouble("high52");
        lastTrade = extractDouble("lastTrade");
        low = extractDouble("low");
        low52 = extractDouble("low52");
        open = extractDouble("open");
        openInterest = extractLong("openInterest");
        optionStyle = extract("optionStyle");
        optionUnderlier = extract("optionUnderlier");
        optionUnderlierExchange = extract("optionUnderlierExchange");
        previousClose = extractDouble("previousClose");
        previousDayVolume = extractLong("previousDayVolume");
        primaryExchange = extract("primaryExchange");
        symbolDescription = extract("symbolDescription");
        totalVolume = extractLong("totalVolume");
        upc = extractLong("upc");
        var nodeList = getList(new OptionDeliverable());
        for (int i = 0; i < nodeList.getLength(); i++)
            optionDeliverableList.add(extractObject(new OptionDeliverable(), nodeList.item(i)));
        cashDeliverable = extractDouble("cashDeliverable");
        marketCap = extractDouble("marketCap");
        sharesOutstanding = extractDouble("sharesOutstanding");
        nextEarningDate = extract("nextEarningDate");
        beta = extractDouble("beta");
        yield = extractDouble("yield");
        declaredDividend = extractDouble("declaredDividend");
        dividendPayableDate = extractLong("dividendPayableDate");
        pe = extractDouble("pe");
        week52LowDate = extractDate("week52LowDate");
        week52HiDate = extractDate("week52HiDate");
        intrinsicValue = extractDouble("intrinsicValue");
        timePremium = extractDouble("timePremium");
        optionMultiplier = extractDouble("optionMultiplier");
        contractSize = extractDouble("contractSize");
        expirationDate = extractDate("expirationDate");
        ehQuote = extractObject(new ExtendedHourQuoteDetail());
        optionPreviousBidPrice = extractDouble("optionPreviousBidPrice");
        optionPreviousAskPrice = extractDouble("optionPreviousAskPrice");
        osiKey = extract("osiKey");
        timeOfLastTrade = extractTime("timeOfLastTrade");
        averageVolume = extractLong("averageVolume");

        return this;
    }
}
