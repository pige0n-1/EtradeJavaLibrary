
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;

/**
 * This is a child class of BrowserRequest to customize the experience of obtaining the verifier key in the Oauth
 * authentication flow. Create an instance with the no-arg constructor, and configure it using the methods in the
 * BrowserRequest class.
 */
public class EtradeBrowserRequest extends BrowserRequest {
    
    // Static data fields
    private static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    
    @Override
    protected EtradeVerifierKey getVerifierUserInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        System.out.print("Enter the verifier code: ");
        String verifierCode = scanner.next();
        
        try {
            return new EtradeVerifierKey(verifierCode);
        }
        catch (InvalidParameterException ex) {
            networkLogger.log("Invalid verifier code entered", verifierCode);
            
            return getVerifierUserInput();
        }
    }
}
