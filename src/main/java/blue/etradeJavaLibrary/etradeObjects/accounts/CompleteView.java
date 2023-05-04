package blue.etradeJavaLibrary.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

import java.lang.Double;
import java.time.LocalDate;
import java.util.Date;

public class CompleteView extends EtradeObject<CompleteView> {

    // Instance data fields
    public Boolean priceAdjustedFlag;
    public Double price;
    public Double adjPrice;
    public Double change;
    public Double changePct;
    public Double prevClose;
    public Double adjPrevClose;
    public Double volume;
    public Double lastTrade;
    public LocalDate lastTradeTime;
    public Double adjLastTrade;
    public String symbolDescription;
    public Double perform1Month;
    public Double perform3Month;
    public Double perform6Month;
    public Double perform12Month;
    public Long prevDayVolume;
    public Long tenDayVolume;
    public Double beta;
    public Double sv10DaysAvg;
    public Double sv20DaysAvg;
    public Double sv1MonAvg;
    public Double sv2MonAvg;
    public Double sv3MonAvg;
    public Double sv4MonAvg;
    public Double sv6MonAvg;
    public Double week52High;
    public Double week52Low;
    public String week52Range;
    public Double marketCap;
    public String daysRange;
    public Double delta52WkHigh;
    public Double delta52WkLow;
    public String currency;
    public String exchange;
    public Boolean marginable;
    public Double bid;
    public Double ask;
    public Double bidAskSpread;
    public Long bidSize;
    public Long askSize;
    public Double open;
    public Double delta;
    public Double gamma;
    public Double ivPct;
    public Double rho;
    public Double theta;
    public Double vega;
    public Double premium;
    public Integer daysToExpiration;
    public Double intrinsicValue;
    public Double openInterest;
    public Boolean optionsAdjustedFlag;
    public String deliverablesStr;
    public Double optionMultiplier;
    public String baseSymbolAndPrice;
    public Double estEarnings;
    public Double eps;
    public Double peRatio;
    public Double annualDividend;
    public Double dividend;
    public Double divYield;
    public LocalDate divPayDate;
    public LocalDate exDividendDate;
    public String cusip;
    public String quoteStatus;

    @Override
    public CompleteView configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        priceAdjustedFlag = extractBoolean("priceAdjustedFlag");
        price = extractDouble("price");
        adjPrice = extractDouble("adjPrice");
        change = extractDouble("change");
        changePct = extractDouble("changePct");
        prevClose = extractDouble("prevClose");
        adjPrevClose = extractDouble("adjPrevClose");
        volume = extractDouble("volume");
        lastTrade = extractDouble("lastTrade");
        lastTradeTime = extractDate("lastTradeTime");
        adjLastTrade = extractDouble("adjLastTrade");
        symbolDescription = extract("symbolDescription");
        perform1Month = extractDouble("perform1Month");
        perform3Month = extractDouble("perform3Month");
        perform6Month = extractDouble("perform6Month");
        perform12Month = extractDouble("perform12Month");
        prevDayVolume = extractLong("prevDayVolume");
        tenDayVolume = extractLong("tenDayVolume");
        beta = extractDouble("beta");
        sv10DaysAvg = extractDouble("sv10DaysAvg");
        sv20DaysAvg = extractDouble("sv20DaysAvg");
        sv1MonAvg = extractDouble("sv1MonAvg");
        sv2MonAvg = extractDouble("sv2MonAvg");
        sv3MonAvg = extractDouble("sv3MonAvg");
        sv4MonAvg = extractDouble("sv4MonAvg");
        sv6MonAvg = extractDouble("sv6MonAvg");
        week52High = extractDouble("week52High");
        week52Low = extractDouble("week52Low");
        week52Range = extract("week52Range");
        marketCap = extractDouble("marketCap");
        daysRange = extract("daysRange");
        delta52WkHigh = extractDouble("delta52WkHigh");
        delta52WkLow = extractDouble("delta52WkLow");
        currency = extract("currency");
        exchange = extract("exchange");
        marginable = extractBoolean("marginable");
        bid = extractDouble("bid");
        ask = extractDouble("ask");
        bidAskSpread = extractDouble("bidAskSpread");
        bidSize = extractLong("bidSize");
        askSize = extractLong("askSize");
        open = extractDouble("open");
        delta = extractDouble("delta");
        gamma = extractDouble("gamma");
        ivPct = extractDouble("ivPct");
        rho = extractDouble("rho");
        theta = extractDouble("theta");
        vega = extractDouble("vega");
        premium = extractDouble("premium");
        daysToExpiration = extractInteger("daysToExpiration");
        intrinsicValue = extractDouble("intrinsicValue");
        openInterest = extractDouble("openInterest");
        optionsAdjustedFlag = extractBoolean("optionsAdjustedFlag");
        deliverablesStr = extract("deliverablesStr");
        optionMultiplier = extractDouble("optionMultiplier");
        baseSymbolAndPrice = extract("baseSymbolAndPrice");
        estEarnings = extractDouble("estEarnings");
        eps = extractDouble("eps");
        peRatio = extractDouble("peRatio");
        annualDividend = extractDouble("annualDividend");
        dividend = extractDouble("dividend");
        divYield = extractDouble("divYield");
        divPayDate = extractDate("divPayDate");
        exDividendDate = extractDate("exDividendDate");
        cusip = extract("cusip");
        quoteStatus = extract("quoteStatus");

        return this;
    }
}
