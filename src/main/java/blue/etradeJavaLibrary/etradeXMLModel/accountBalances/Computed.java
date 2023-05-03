package blue.etradeJavaLibrary.etradeXMLModel.accountBalances;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.etradeXMLModel.EtradeObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Computed extends EtradeObject<Computed> {

    // Instance data fields
    public BigDecimal cashAvailableForInvestment;
    public BigDecimal cashAvailableForWithdrawal;
    public BigDecimal totalAvailableForWithdrawal;
    public BigDecimal netCash;
    public BigDecimal cashBalance;
    public BigDecimal settledCashForInvestment;
    public BigDecimal unSettledCashForInvestment;
    public BigDecimal fundsWithheldFromPurchasePower;
    public BigDecimal fundsWithheldFromWithdrawal;
    public BigDecimal marginBuyingPower;
    public BigDecimal cashBuyingPower;
    public BigDecimal dtMarginBuyingPower;
    public BigDecimal dtCashBuyingPower;
    public BigDecimal marginBalance;
    public BigDecimal shortAdjustBalance;
    public BigDecimal regtEquity;
    public BigDecimal regtEquityPercent;
    public BigDecimal accountBalance;
    public OpenCalls openCalls;
    public RealTimeValues realTimeValues;
    public PortfolioMargin portfolioMargin;

    @Override
    public Computed configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        cashAvailableForInvestment = extractNumber("cashAvailableForInvestment");
        cashAvailableForWithdrawal = extractNumber("cashAvailableForWithdrawal");
        totalAvailableForWithdrawal = extractNumber("totalAvailableForWithdrawal");
        netCash = extractNumber("netCash");
        cashBalance = extractNumber("cashBalance");
        settledCashForInvestment = extractNumber("settledCashForInvestment");
        unSettledCashForInvestment = extractNumber("unSettledCashForInvestment");
        fundsWithheldFromPurchasePower = extractNumber("fundsWithheldFromPurchasePower");
        fundsWithheldFromWithdrawal = extractNumber("fundsWithheldFromWithdrawal");
        marginBuyingPower = extractNumber("marginBuyingPower");
        cashBuyingPower = extractNumber("cashBuyingPower");
        dtMarginBuyingPower = extractNumber("dtMarginBuyingPower");
        dtCashBuyingPower = extractNumber("dtCashBuyingPower");
        marginBalance = extractNumber("marginBalance");
        shortAdjustBalance = extractNumber("shortAdjustBalance");
        regtEquity = extractNumber("regtEquity");
        regtEquityPercent = extractNumber("regtEquityPercent");
        accountBalance = extractNumber("accountBalance");
        openCalls = extractObject(new OpenCalls());
        realTimeValues = extractObject(new RealTimeValues());
        portfolioMargin = extractObject(new PortfolioMargin());

        return this;
    }
}
