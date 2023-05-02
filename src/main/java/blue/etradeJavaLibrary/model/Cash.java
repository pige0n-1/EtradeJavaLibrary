package blue.etradeJavaLibrary.model;

import blue.etradeJavaLibrary.core.network.oauth.responses.XMLDefinedObject;
import org.w3c.dom.Document;

import java.math.BigDecimal;

public class Cash extends EtradeObject<Cash> {

    // Instance data fields
    public BigDecimal fundsForOpenOrdersCash;
    public BigDecimal moneyMktBalance;
}
