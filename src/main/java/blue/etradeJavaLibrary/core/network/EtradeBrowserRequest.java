
package blue.etradeJavaLibrary.core.network;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.BrowserRequest;

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
