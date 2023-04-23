
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.URLBuilder;
import blue.etradeJavaLibrary.core.network.oauth.OauthException;
import java.net.URL;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.io.IOException;
import java.awt.Desktop;

/**
 * This class represents a specialized type of request in the Oauth 1.0a 
 * authentication flow for certain applications, such as Etrade. In this clase
 * A browser window is opened according to a URL. There is no authentication
 * header in the request. The URL contains the base url and query parameters.
 * @author Hunter
 */
public class BrowserRequest {
    private URI fullURI;
    
    public BrowserRequest(BaseURL baseURL, QueryParameters queryParameters) throws MalformedURLException, OauthException{
        URL fullURL = URLBuilder.buildURL(baseURL, queryParameters);
        
        try {
            fullURI = fullURL.toURI();
        }
        catch (URISyntaxException ex) {}
    }
    
    public void go() throws IOException {
        Desktop.getDesktop().browse(fullURI);
    }
}
