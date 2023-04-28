
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
    
    // Instance data fields
    private URI fullURI;
    private boolean configured = false;
    
    // Static data fields
    private static final boolean RFC3986_ENCODED = false;
    protected static ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    /**
     * This no-arg constructor is provided purely for convenience so that the
     * data fields can be configured at a later time. This is useful when an 
     * oauth manager allows the passage of a child object of this class into a
     * method without being configured yet.
     */
    public BrowserRequest() {}
    
    public BrowserRequest(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        configure(baseURL, keys);
    }
    
    public void configureBrowserRequest(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        configure(baseURL, keys);
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
    
    
    private void configure(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        if (!keys.hasRetrievedAToken())
            throw new InvalidParameterException("You must have an access token to configure a BrowserRequest.");
        
        QueryParameters queryParameters = new QueryParameters(RFC3986_ENCODED);
        queryParameters.addParameter("key", keys.consumerKey);
        queryParameters.addParameter("token", keys.getToken());
        
        try {
            URL fullURL = URLBuilder.buildURL(baseURL, queryParameters);
            
            fullURI = fullURL.toURI();
            logger.log("Browser authentication URI", fullURI.toString());
            configured = true;
        }
        catch (MalformedURLException | URISyntaxException ex) {
            logger.log("Browser request unsuccessful due to problem with URL", fullURI.toString());
            throw new OauthException("There was a problem with the URL", ex);
        }
    }
    
    protected Key getVerifierUserInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        System.out.print("Enter the verifier: ");
        
        String verifier = scanner.next();
        return new Key(verifier);
    }
}
