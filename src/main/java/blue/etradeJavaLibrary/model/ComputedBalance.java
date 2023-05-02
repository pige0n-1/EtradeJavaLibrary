package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class ComputedBalance extends EtradeObject<ComputedBalance> {

    // Instance data fields
    private Document xmlDocument;
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
}
