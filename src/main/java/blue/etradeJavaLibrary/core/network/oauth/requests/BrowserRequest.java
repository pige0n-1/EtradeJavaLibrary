
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
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
 * authentication flow for that represents the user sign-on to the severice
 * provider. No authorization header is set; only a URL is called with the
 * base URL and query parameters
 */
public class BrowserRequest {
    private static final boolean RFC3986_ENCODED = false;
    private static ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    private URI fullURI;
    
    public BrowserRequest(BaseURL baseURL, QueryParameters queryParameters) throws MalformedURLException, OauthException{
        URL fullURL = URLBuilder.buildURL(baseURL, queryParameters);
        
        try {
            fullURI = fullURL.toURI();
            logger.log("Browser authentication URI", fullURI.toString());
        }
        catch (URISyntaxException ex) {}
    }
    
    public BrowserRequest(BaseURL baseURL, Key consumerKey, Key token) throws MalformedURLException, OauthException {
        QueryParameters queryParameters = new QueryParameters(RFC3986_ENCODED);
        queryParameters.addParameter("key", consumerKey.getValue());
        queryParameters.addParameter("token", token.getValue());
        
        URL fullURL = URLBuilder.buildURL(baseURL, queryParameters);
        
        try {
            fullURI = fullURL.toURI();
            logger.log("Browser authentication URI", fullURI.toString());
        }
        catch (URISyntaxException ex) {}
    }
    
    public void go() throws IOException {
        Desktop.getDesktop().browse(fullURI);
    }
}
