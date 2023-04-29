
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.APIResponse;
import java.io.IOException;
import java.io.Serializable;

public abstract class APIManager 
        implements Serializable, AutoCloseable {
    
    // Instance data fields
    private OauthKeySet keys;
    private BaseURLSet baseURLSet;
    private OauthFlowManager oauthFlow;
    private boolean configured = false;
    
    // Static data fields
    protected transient static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();
    protected transient static final ProgramLogger apiLogger = ProgramLogger.getAPILogger();
    
    protected APIManager() {}
    
    protected final void configure(BaseURLSet baseURLSet, OauthKeySet keys) throws OauthException {
        configure(baseURLSet, keys, new BrowserRequest());
    }
    
    protected final void configure(BaseURLSet baseURLSet, OauthKeySet keys, BrowserRequest browserRequestMethod) throws OauthException{
        this.keys = keys;
        this.baseURLSet = baseURLSet;
        oauthFlow = new OauthFlowManager(baseURLSet, keys);
        oauthFlow.setBrowserRequest(browserRequestMethod);
        configured = true;
        
        getNewAccessToken();
    }
    
    protected final void renewAccessToken() throws OauthException {
        ensureConfigured();
        oauthFlow.renewAccessToken();
    }
    
    protected final void getNewAccessToken() throws OauthException {
        ensureConfigured();
        oauthFlow.getAccessToken();
    }
    
    protected final String sendAPIRequest(String requestURI, PathParameters pathParameters, QueryParameters queryParameter, HttpMethod httpMethod) throws OauthException {
        ensureConfigured();
        renewAccessTokenIfNeeded();
        
        try {
            BaseURL requestBaseURL = new BaseURL(baseURLSet.apiBaseURL, requestURI);
            APIRequest request = new APIRequest(requestBaseURL, keys, httpMethod);
            APIResponse response = request.sendAndGetResponse();

            return response.parse();
        }
        catch (IOException ex) {
            networkLogger.log("API request failure.");
            throw new OauthException("API request could not be sent.", ex);
        }
    }
    
    protected final String sendAPIRequest(String requestURI, PathParameters pathParameters, HttpMethod httpMethod) throws OauthException {
        return sendAPIRequest(requestURI, pathParameters, new QueryParameters(), httpMethod);
    }
    
    protected final String sendAPIRequest(String requestURI, QueryParameters queryParameters, HttpMethod httpMethod) throws OauthException {
        return sendAPIRequest(requestURI, new PathParameters(), queryParameters, httpMethod);
    }
    
    protected final String sendAPIRequest(String requestURI, HttpMethod httpMethod) throws OauthException {
        return sendAPIRequest(requestURI, new PathParameters(), new QueryParameters(), httpMethod);
    }
    
    /**
     * Some service providers make access tokens inactivate or expire after a certain amount of time. This
     * method should check to see if the access token needs to be renewed, and if it does, it should renew it
     * by calling the renewAccessToken method or getNewAccessToken method, depending on if the token is
     * inactive or completely expired.
     */
    protected abstract void renewAccessTokenIfNeeded() throws OauthException;
    
    
    // Private helper methods
    
    
    private void ensureConfigured() {
        if (!configured)
            throw new RuntimeException("The API manager must be configured before sending a request.");
    }
}
