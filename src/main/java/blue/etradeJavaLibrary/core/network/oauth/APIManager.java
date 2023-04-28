
package blue.etradeJavaLibrary.core.network.oauth;

import java.io.Serializable;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.*;

public abstract class APIManager 
        implements Serializable, AutoCloseable {
    
    // Instance data fields
    protected OauthKeySet keys;
    protected OauthFlowManager oauthFlow;
    protected String oauthBaseURL;
    
    // Static data fields
    protected transient static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    protected transient static final ProgramLogger apiLogger = ProgramLogger.getAPILogger();
    
    protected APIManager() throws NetworkException {
        setKeys();
        setBaseURL();
        //performOauthFlow();
    }
    
    protected abstract void setKeys();
    
    protected abstract void setBaseURL();
    
}
