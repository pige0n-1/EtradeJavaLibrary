package blue.etradeJavaLibrary.model.etradeObjects.market;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class OptionGreeks extends EtradeObject<OptionGreeks> {

    // Instance data fields
    public Double rho;
    public Double vega;
    public Double theta;
    public Double delta;
    public Double gamma;
    public Double iv;
    public Double currentValue;

    @Override
    public OptionGreeks configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        rho = extractDouble("rho");
        vega = extractDouble("vega");
        theta = extractDouble("theta");
        delta = extractDouble("delta");
        gamma = extractDouble("gamma");
        iv = extractDouble("iv");
        currentValue = extractDouble("currentValue");

        return this;
    }
}
