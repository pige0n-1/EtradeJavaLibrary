
package blue.etradeJavaLibrary.core.network.oauth.model;


public class BaseURLSet {
    public final String oauthBaseURL;
    public final String apiBaseURL;
    public final String requestTokenURI;
    public final String accessTokenURI;
    public final String renewAccessTokenURI;
    public final String revokeAccessTokenURI;
    public final String authorizeAccountBaseURL;

    public BaseURLSet(String oauthBaseURL, String apiBaseURL, String requestTokenURI, String accessTokenURI, String renewAccessTokenURI, String revokeAccessTokenURI, String authorizeAccountBaseURL) {
        this.oauthBaseURL = oauthBaseURL;
        this.apiBaseURL = apiBaseURL;
        this.requestTokenURI = requestTokenURI;
        this.accessTokenURI = accessTokenURI;
        this.renewAccessTokenURI = renewAccessTokenURI;
        this.revokeAccessTokenURI = revokeAccessTokenURI;
        this.authorizeAccountBaseURL = authorizeAccountBaseURL;
    }
}
