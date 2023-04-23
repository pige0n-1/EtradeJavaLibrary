
package blue.etradeJavaLibrary.core.network.oauth.model;

public final class SimpleParameters extends Parameters {

    @Override
    public void replaceParameter(String key, String value) throws OauthException {
        super.replaceParameter(key, value);
    }
    
    @Override
    public void addParameter(String key, String value) throws OauthException {
        super.addParameter(key, value);
    }
    
    @Override
    public void addParameterWithoutEncoding(String key, String value) {
        super.addParameterWithoutEncoding(key, value);
    }
}
