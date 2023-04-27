
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.URLBuilder;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class represents a specialized type of request in the Oauth 1.0a 
 * authentication flow for that represents the user sign-on to the severice
 * provider. No authorization header is set; only a URL is called with the
 * base URL and query parameters
 */
public class BrowserRequest 
        implements Serializable {
    
    private static final boolean RFC3986_ENCODED = false;
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    private URI fullURI;
    private boolean configured = false;
    
    public BrowserRequest() {}
    
    public BrowserRequest(BaseURL baseURL, Key consumerKey, Key token) throws OauthException {
        configure(baseURL, consumerKey, token);
    }
    
    public void configureBrowserRequest(BaseURL baseURL, Key consumerKey, Key token) throws OauthException {
        configure(baseURL, consumerKey, token);
    }
    
    public Key go() throws IOException, OauthException {
        if (configured) {
            Desktop.getDesktop().browse(fullURI);
            return getVerifierUserInput();
        }
        else
            throw new OauthException("BrowserRequest cannot be sent before configured.");
    }
    
    public URI getFullURI() {
        return fullURI;
    }
    
    public boolean isConfigured() {
        return configured;
    }
    
    
    // Private helper methods
    
    
    private void configure(BaseURL baseURL, Key consumerKey, Key token) throws OauthException {
        QueryParameters queryParameters = new QueryParameters(RFC3986_ENCODED);
        queryParameters.addParameter("key", consumerKey.getValue());
        queryParameters.addParameter("token", token.getValue());
        
        try {
            URL fullURL = URLBuilder.buildURL(baseURL, queryParameters);
            
            fullURI = fullURL.toURI();
            logger.log("Browser authentication URI", fullURI.toString());
            configured = true;
        }
        catch (MalformedURLException | URISyntaxException ex) {
            logger.log("Browser request unsuccessful due to problem with URL", fullURI.toString());
            throw new OauthException("There was a problem with the URL");
        }
    }
    
    protected static Key getVerifierUserInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        System.out.print("Enter the verifier: ");
        
        String verifier = scanner.next();
        return new Key(verifier);
    }
}
