
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Current work of progress. Not functional.
 * @author Hunter
 */
public class EtradeBrowserRequestChromeAutomation extends BrowserRequest 
        implements Serializable {
    
    private transient WebDriver chromeBrowser;
    
    public EtradeBrowserRequestChromeAutomation() throws MalformedURLException, OauthException {}
    
    public EtradeBrowserRequestChromeAutomation(BaseURL baseURL, Key consumerKey, Key token) throws MalformedURLException, OauthException {
        super(baseURL, consumerKey, token);
    }
    
    @Override
    public Key go() throws OauthException, IOException {
        try {
            openBrowser();
            enterUsernameAndPassword();
            waitForUserToInputSecurityCode();
            clickAccept();
            return copyVerifierFromBrowser();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    
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
    
    private void enterUsernameAndPassword() {
        final String USERNAME_FIELD_NAME = "USER";
        final String PASSWORD_FIELD_NAME = "PASSWORD";
        final String USERNAME = KeyAndURLExtractor.getEtradeUsername();
        final String PASSWORD = KeyAndURLExtractor.getEtradePassword();
        
        WebElement usernameInputBox = chromeBrowser.findElement(By.name(USERNAME_FIELD_NAME));
        WebElement passwordInputBox = chromeBrowser.findElement(By.name(PASSWORD_FIELD_NAME));
        
        usernameInputBox.sendKeys(USERNAME);
        passwordInputBox.sendKeys(PASSWORD);
    }
    
    private void waitForUserToInputSecurityCode() {
        sleep(10000);
    }
    
    private void clickAccept() throws OauthException {
        final String ACCEPT_BUTTON_NAME = "submit";
        
        WebElement acceptButton = chromeBrowser.findElement(By.name(ACCEPT_BUTTON_NAME));
        acceptButton.click();
    }
    
    private Key copyVerifierFromBrowser() {
        return null;
    }
    
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {}
    }
}
