package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDefinedObject;

import javax.xml.parsers.ParserConfigurationException;

/**
 * A BodyParameter is a traditional parameter in the Oauth model, but it is specific to the HTTP request body. The
 * body parameter represents the entire body of an HTTP request. A request only holds one body, so this class only holds
 * up to one parameter. This instance of Parameters is not RFC3986 percent-encoded.
 */
public class BodyParameter extends Parameters {

    // Static data fields
    private static final boolean RFC3986_ENCODED = false;

    /**
     * Constructs an empty BodyParameters object. No parameters can be added afterwards.
     */
    public BodyParameter() {
        super(RFC3986_ENCODED);
    }

    /**
     * Constructs a BodyParameters with one parameter. No parameters can be added afterwards.
     * @param key
     * @param body
     */
    public BodyParameter(String key, XMLBuildable body) throws ParserConfigurationException {
        super(RFC3986_ENCODED);
        var xmlDocument = body.buildXMLFromDataFields();

        addParameter(key, XMLDefinedObject.toString(xmlDocument));
    }

    /**
     * Returns the value of the body parameter as a string
     * @return
     */
    public String getValue() {
        var parametersIterator = iterator();

        return parametersIterator.next().getValue();
    }

    @Override
    public void addParameter(String key, String value) {}

    @Override
    public void removeParameter(String key) {}
}
