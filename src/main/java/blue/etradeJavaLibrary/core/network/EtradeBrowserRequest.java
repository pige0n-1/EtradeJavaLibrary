
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.KeyAndURLExtractor;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

/**
 * This is a child class of BrowserRequest to customize the experience of obtaining the verifier key in the Oauth
 * authentication flow. Create an instance with the no-arg constructor, and configure it using the methods in the
 * BrowserRequest class.
 */
public class EtradeBrowserRequest extends BrowserRequest {

    // Instance data fields
    private ChromeDriver chromeBrowser;
    
    // Static data fields
    private static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();

    @Override
    public Key go() throws IOException {
        if (!isConfigured()) throw new OauthException("BrowserRequest cannot be sent before configured.");

        try {
            var securityCode = getSecurityCodeFromUser();
            openBrowser();
            enterCredentials(securityCode);
            clickAccept();
            return copyVerifierFromBrowser();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }


    // Private helper methods


    private String getSecurityCodeFromUser() {
        var scanner = new java.util.Scanner(System.in);
        System.out.print("Enter the security code: ");

        String value = scanner.next();
        scanner.close();
        return value;
    }

    private void openBrowser() {
        var chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("--disable-blink-features");
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--user-agent=Chrome/74.0.3729.169");
        chromeBrowser = new ChromeDriver(chromeOptions);
        chromeBrowser.get(getFullURI().toString());
    }

    private void clickAccept() {
        WebElement acceptButton = chromeBrowser.findElement(By.name("submit"));
        acceptButton.click();
    }

    private Key copyVerifierFromBrowser() {
        WebElement inputBox = chromeBrowser.findElement(By.cssSelector("div[style='text-align:center'] > input[type='text']"));
        String value = inputBox.getAttribute("value");

        return new EtradeVerifierKey(value);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {}
    }

    private void enterCredentials(String securityCode) {
        final String USERNAME = KeyAndURLExtractor.getEtradeUsername();
        final String PASSWORD = KeyAndURLExtractor.getEtradePassword();

        WebDriverWait wait = new WebDriverWait(chromeBrowser, Duration.ofMillis(1314));

        WebElement usernameInputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("USER")));

        Actions actions = new Actions(chromeBrowser);
        actions.moveToElement(usernameInputBox).click().sendKeys(USERNAME).build().perform();

        WebElement passwordInputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("PASSWORD")));

        actions = new Actions(chromeBrowser);
        actions.moveToElement(passwordInputBox).click().sendKeys(PASSWORD).build().perform();

        WebElement securityCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='security_code_chk']")));
        securityCheckbox.click();

        sleep(790);

        WebElement securityCodeInputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("SECURITY_CODE")));
        actions = new Actions(chromeBrowser);
        actions.moveToElement(securityCodeInputBox).click().sendKeys(securityCode).build().perform();

        sleep(81);

        wait = new WebDriverWait(chromeBrowser, Duration.ofMillis(1111));

        WebElement logOnButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logon_button")));
        actions = new Actions(chromeBrowser);
        actions.moveToElement(logOnButton).click().build().perform();
    }

    /*private void enterCredentials(String securityCode) {
        final String USERNAME = KeyAndURLExtractor.getEtradeUsername();
        final String PASSWORD = KeyAndURLExtractor.getEtradePassword();

        WebElement usernameInputBox = chromeBrowser.findElement(By.name("USER"));
        WebElement passwordInputBox = chromeBrowser.findElement(By.name("PASSWORD"));
        WebElement securityCheckbox = chromeBrowser.findElement(By.cssSelector("label[for='security_code_chk']"));

        usernameInputBox.sendKeys(USERNAME);
        passwordInputBox.sendKeys(PASSWORD);
        securityCheckbox.click();

        WebElement securityCodeInputBox = chromeBrowser.findElement(By.name("SECURITY_CODE"));
        securityCodeInputBox.sendKeys(securityCode);

        WebElement logOnButton = chromeBrowser.findElement(By.id("logon_button"));
        logOnButton.click();
    }*/
}
