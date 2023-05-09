package blue.etradeJavaLibrary.core.network.oauth.model;

import blue.etradeJavaLibrary.model.etradeObjects.XMLDefinedObject;

/**
 * Holds one parameter: the request body
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
    public BodyParameter(String key, XMLDefinedObject body) {
        super(RFC3986_ENCODED);
        body.toXMLDocument().setXmlStandalone(true);
        addParameter(key, body.toXMLString());
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
