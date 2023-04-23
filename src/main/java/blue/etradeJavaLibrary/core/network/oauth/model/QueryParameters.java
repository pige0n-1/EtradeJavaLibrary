
package blue.etradeJavaLibrary.core.network.oauth.model;

public class QueryParameters extends Parameters {
    
    private boolean rfc3986Encoded;
    
    public QueryParameters() {
        this.rfc3986Encoded = true;
    }
    
    public QueryParameters(boolean rfc3986Encoded) {
        this.rfc3986Encoded = rfc3986Encoded;
    }
    
    @Override
    public void addParameter(String key, String value) throws OauthException {
        if (rfc3986Encoded)
            super.addParameter(key, value);
        else
            super.addParameterWithoutEncoding(key, value);
    }
    
    @Override
    public void replaceParameter(String key, String value) throws OauthException {
        if (rfc3986Encoded)
            super.replaceParameter(key, value);
        else {
            super.removeParameter(key);
            super.addParameterWithoutEncoding(key, value);
        }
    }
}
