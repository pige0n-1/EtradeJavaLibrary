package blue.etradeJavaLibrary.model.etradeObjects.accounts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Computed extends EtradeObject<Computed> {

    // Instance data fields
    public Double cashAvailableForInvestment;
    public Double cashAvailableForWithdrawal;
    public Double totalAvailableForWithdrawal;
    public Double netCash;
    public Double cashBalance;
    public Double settledCashForInvestment;
    public Double unSettledCashForInvestment;
    public Double fundsWithheldFromPurchasePower;
    public Double fundsWithheldFromWithdrawal;
    public Double marginBuyingPower;
    public Double cashBuyingPower;
    public Double dtMarginBuyingPower;
    public Double dtCashBuyingPower;
    public Double marginBalance;
    public Double shortAdjustBalance;
    public Double regtEquity;
    public Double regtEquityPercent;
    public Double accountBalance;
    public OpenCalls openCalls;
    public RealTimeValues realTimeValues;
    public PortfolioMargin portfolioMargin;

    @Override
    public Computed configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        cashAvailableForInvestment = extractDouble("cashAvailableForInvestment");
        cashAvailableForWithdrawal = extractDouble("cashAvailableForWithdrawal");
        totalAvailableForWithdrawal = extractDouble("totalAvailableForWithdrawal");
        netCash = extractDouble("netCash");
        cashBalance = extractDouble("cashBalance");
        settledCashForInvestment = extractDouble("settledCashForInvestment");
        unSettledCashForInvestment = extractDouble("unSettledCashForInvestment");
        fundsWithheldFromPurchasePower = extractDouble("fundsWithheldFromPurchasePower");
        fundsWithheldFromWithdrawal = extractDouble("fundsWithheldFromWithdrawal");
        marginBuyingPower = extractDouble("marginBuyingPower");
        cashBuyingPower = extractDouble("cashBuyingPower");
        dtMarginBuyingPower = extractDouble("dtMarginBuyingPower");
        dtCashBuyingPower = extractDouble("dtCashBuyingPower");
        marginBalance = extractDouble("marginBalance");
        shortAdjustBalance = extractDouble("shortAdjustBalance");
        regtEquity = extractDouble("regtEquity");
        regtEquityPercent = extractDouble("regtEquityPercent");
        accountBalance = extractDouble("accountBalance");
        openCalls = extractObject(new OpenCalls());
        realTimeValues = extractObject(new RealTimeValues());
        portfolioMargin = extractObject(new PortfolioMargin());

        return this;
    }
}
