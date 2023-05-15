
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms.URLBuilder;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class represents a specialized type of request in the Oauth 1.0a 
 * authentication flow for that represents the user sign-on to the severice
 * provider. No authorization header is set; only a URL is called with the
 * base URL and query parameters. The request can be sent with the go()
 * methdod.
 */
public class BrowserRequest 
        implements Serializable {
    
    // Instance data fields
    /**
     * The URL of the detination of the request
     */
    private URI fullURI;
    
    /**
     * Represents if this has been configured. It can be configured with
     * the configure method. The request cannot be sent until it is configured.
     */
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
    
    /**
     * Constructs a BrowserRequest that is configured with a BaseURL and
     * an OauthKeySet.
     * @param baseURL
     * @param keys
     * @throws OauthException 
     */
    public BrowserRequest(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        configure(baseURL, keys);
    }
    
    /**
     * If the BrowserRequest is not yet configured, this method can be called to configure
     * it.
     * @param baseURL
     * @param keys
     * @throws OauthException 
     */
    public void configureBrowserRequest(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        configure(baseURL, keys);
    }
    
    /**
     * Sends the request in a browser window.
     * @return the verifier Key, from the Oauth model
     * @throws IOException
     * @throws OauthException 
     */
    public Key go() throws IOException, OauthException {
        if (!configured) throw new OauthException("BrowserRequest cannot be sent before configured.");

        try {
            Desktop.getDesktop().browse(fullURI);
        }
        catch (UnsupportedOperationException ex) {
            // if the OS is linux open a linux browser
            if (System.getProperty("os.name").toLowerCase().contains("nux")) openLinuxBrowser();
        }

        return getVerifierUserInput();
    }
    
    public URI getFullURI() {
        return fullURI;
    }
    
    public boolean isConfigured() {
        return configured;
    }
    
    
    // Private helper methods


    private void openLinuxBrowser() throws IOException {
        var runtime = Runtime.getRuntime();
        runtime.exec(new String[] {"xdg-open", fullURI.toString()});
    }
    
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
