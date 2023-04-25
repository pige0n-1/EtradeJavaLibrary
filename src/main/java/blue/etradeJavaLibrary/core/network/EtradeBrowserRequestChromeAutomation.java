
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.net.MalformedURLException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.Serializable;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

/**
 * Current work of progress. Not functional.
 * @author Hunter
 */
public class EtradeBrowserRequestChromeAutomation extends BrowserRequest implements Serializable {
    
    private transient WebDriver chromeBrowser;
    
    public EtradeBrowserRequestChromeAutomation() throws MalformedURLException, OauthException {}
    
    public EtradeBrowserRequestChromeAutomation(BaseURL baseURL, Key consumerKey, Key token) throws MalformedURLException, OauthException {
        super(baseURL, consumerKey, token);
    }
    
    /**
    @Override
    public Key go() throws OauthException {
        try {
            openBrowser();
            
            new WebDriverWait(chromeBrowser, Duration.ofSeconds(100)).until(ExpectedConditions.elementToBeClickable(By.name("submit"))).click();
            //waitForUserToLogin();
            return BrowserRequest.getVerifierUserInput();
            //clickAccept();
            //return getVerifierCode();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    **/
    
    
    // Private helper methods
    
    
    private void openBrowser() throws OauthException {
        try {
            chromeBrowser = new ChromeDriver();
        }
        catch (Exception ex) {
            throw new OauthException("There was a problem with selenium.");
        }
        
        String browserURL = getFullURI().toString();
        chromeBrowser.get(browserURL);
    }
    
    private void waitForUserToLogin() throws OauthException {
        final String LOGIN_TITLE = "Log On to E*TRADE | E*TRADE";
        
        do {
            sleep(1500);
            
        } while (chromeBrowser.getTitle().equals(LOGIN_TITLE));
    }
    
    private void clickAccept() throws OauthException {
        final String ACCEPT_BUTTON_NAME = "submit";
        
        WebElement acceptButton = chromeBrowser.findElement(By.name(ACCEPT_BUTTON_NAME));
        acceptButton.click();
    }
    
    private Key getVerifierCode() throws OauthException {
        sleep(1000);
        return null;
    }
    
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {}
    }
}
