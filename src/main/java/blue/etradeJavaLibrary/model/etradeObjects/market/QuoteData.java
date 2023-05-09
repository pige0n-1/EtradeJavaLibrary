package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.accounts.Product;
import org.w3c.dom.Document;

import java.time.LocalDateTime;

public class QuoteData extends EtradeObject<QuoteData> {

    // Instance data fields
    public AllQuoteDetails all;
    public String dateTime;
    public LocalDateTime dateTimeUTC;
    public String quoteStatus;
    public String ahFlag;
    public String errorMessage;
    public OptionQuoteDetails option;
    public Product product;
    public String timeZone;
    public Boolean dstFlag;
    public Boolean hasMiniOptions;

    @Override
    public QuoteData configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        all = extractObject(new AllQuoteDetails(), "All");
        dateTime = extract("dateTime");
        dateTimeUTC = extractTime("dateTimeUTC");
        quoteStatus = extract("quoteStatus");
        ahFlag = extract("ahFlag");
        errorMessage = extract("errorMessage");
        option = extractObject(new OptionQuoteDetails());
        product = extractObject(new Product());
        timeZone = extract("timeZone");
        dstFlag = extractBoolean("dstFlag");
        hasMiniOptions = extractBoolean("hasMiniOptions");

        return this;
    }
}
