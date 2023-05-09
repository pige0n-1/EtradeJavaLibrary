package blue.etradeJavaLibrary.model.etradeObjects.order;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import org.w3c.dom.Document;

public class Disclosure extends EtradeObject<Disclosure> {

    // Instance data fields
    public Boolean ehDisclosureFlag;
    public Boolean ahDisclosureFlag;
    public Boolean conditionalDisclosureFlag;
    public Boolean aoDisclosureFlag;
    public Boolean mfFLConsent;
    public Boolean mfEOConsent;

    @Override
    public Disclosure configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        ehDisclosureFlag = extractBoolean("ehDisclosureFlag");
        ahDisclosureFlag = extractBoolean("ahDisclosureFlag");
        conditionalDisclosureFlag = extractBoolean("conditionalDisclosureFlag");
        aoDisclosureFlag = extractBoolean("aoDisclosureFlag");
        mfFLConsent = extractBoolean("mfFLConsent");
        mfEOConsent = extractBoolean("mfEOConsent");

        return this;
    }
}
