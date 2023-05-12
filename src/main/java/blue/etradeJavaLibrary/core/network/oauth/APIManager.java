
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import blue.etradeJavaLibrary.core.network.oauth.requests.*;
import blue.etradeJavaLibrary.core.network.oauth.responses.*;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDefinedObject;

import javax.management.Query;
import java.time.Instant;
import java.io.IOException;
import java.io.Serializable;

public abstract class APIManager 
        implements Serializable, AutoCloseable {
    
    // Instance data fields
    private OauthKeySet keys;
    private BaseURLSet baseURLSet;
    private OauthFlowManager oauthFlow;
    private boolean configured = false;
    protected Instant timeOfLastRequest = Instant.now();
    
    // Static data fields
    protected transient static final ProgramLogger networkLogger = ProgramLogger.getNetworkLogger();

    protected APIManager() {}

    protected final void configure(BaseURLSet baseURLSet, OauthKeySet keys) throws OauthException {
        configure(baseURLSet, keys, new OauthParameters(), new BrowserRequest());
    }

    protected final void configure(BaseURLSet baseURLSet, OauthKeySet keys, OauthParameters oauthParameters, BrowserRequest browserRequestMethod) throws OauthException {
        this.keys = keys;
        this.baseURLSet = baseURLSet;
        oauthFlow = new OauthFlowManager(baseURLSet, keys);
        oauthFlow.setOauthParameters(oauthParameters);
        oauthFlow.setBrowserRequest(browserRequestMethod);
        configured = true;
        
        if (keys.hasRetrievedAToken()) renewAccessToken();
        else getNewAccessToken();
    }
    
    protected final void renewAccessToken() throws OauthException {
        ensureConfigured();
        oauthFlow.renewAccessToken();
        updateTimeOfLastRequest();
    }
    
    protected final void getNewAccessToken() throws OauthException {
        ensureConfigured();
        oauthFlow.getAccessToken();
        updateTimeOfLastRequest();
    }
    
    protected final void sendAPIGetRequest(String requestURI, PathParameters pathParameters, QueryParameters queryParameters, XMLDefinedObject xmlObject) throws OauthException {
        ensureConfigured();
        renewAccessTokenIfNeeded();
        
        try {
            BaseURL requestBaseURL = new BaseURL(baseURLSet.apiBaseURL, requestURI);
            APIRequest request = new APIGetRequest(requestBaseURL, pathParameters, queryParameters, keys);
            request.setOauthParameters(oauthFlow.getOauthParameters());

            APIResponse response = request.sendAndGetResponse();
            updateTimeOfLastRequest();
                
            response.parseIntoXMLDefinedObject(xmlObject);
            networkLogger.log("API GET request success.");
        }
        catch (IOException | ObjectMismatchException ex) {
            networkLogger.log("API GET request failure.");
            throw new OauthException("API GET request could not be sent.", ex);
        }
    }
    
    protected final void sendAPIGetRequest(String requestURI, PathParameters pathParameters, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIGetRequest(requestURI, pathParameters, new QueryParameters(), xmlObject);
    }

    protected final void sendAPIGetRequest(String requestURI, QueryParameters queryParameters, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIGetRequest(requestURI, new PathParameters(), queryParameters, xmlObject);
    }

    protected final void sendAPIGetRequest(String requestURI, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIGetRequest(requestURI, new PathParameters(), new QueryParameters(), xmlObject);
    }

    protected final void sendAPIDeleteRequest(String requestURI, PathParameters pathParameters, QueryParameters queryParameters, XMLDefinedObject xmlObject) throws OauthException {
        ensureConfigured();
        renewAccessTokenIfNeeded();

        try {
            BaseURL requestBaseURL = new BaseURL(baseURLSet.apiBaseURL, requestURI);
            APIRequest request = new APIDeleteRequest(requestBaseURL, pathParameters, queryParameters, keys);
            request.setOauthParameters(oauthFlow.getOauthParameters());

            APIResponse response = request.sendAndGetResponse();
            updateTimeOfLastRequest();

            response.parseIntoXMLDefinedObject(xmlObject);
            networkLogger.log("API request success.");
        }
        catch (IOException | ObjectMismatchException ex) {
            networkLogger.log("API request failure.");
            throw new OauthException("API request could not be sent.", ex);
        }
    }

    protected final void sendAPIDeleteRequest(String requestURI, PathParameters pathParameters, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIDeleteRequest(requestURI, pathParameters, new QueryParameters(), xmlObject);
    }

    protected final void sendAPIDeleteRequest(String requestURI, QueryParameters queryParameters, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIDeleteRequest(requestURI, new PathParameters(), queryParameters, xmlObject);
    }

    protected final void sendAPIDeleteRequest(String requestURI, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIDeleteRequest(requestURI, new PathParameters(), new QueryParameters(), xmlObject);
    }

    protected final void sendAPIPostRequest(String requestURI, PathParameters pathParameters, QueryParameters queryParameters, BodyParameter bodyParameter, XMLDefinedObject xmlObject) throws OauthException {
        ensureConfigured();
        renewAccessToken();

        try {
            BaseURL requestBaseURL = new BaseURL(baseURLSet.apiBaseURL, requestURI);
            APIRequest request = new APIPostRequest(requestBaseURL, pathParameters, queryParameters, bodyParameter, keys);
            request.setOauthParameters(oauthFlow.getOauthParameters());

            APIResponse response = request.sendAndGetResponse();
            updateTimeOfLastRequest();

            response.parseIntoXMLDefinedObject(xmlObject);
            networkLogger.log("API POST request success.");
        }
        catch (IOException | ObjectMismatchException ex) {
            networkLogger.log("API POST request failure.");
            throw new OauthException("API POST request could not be sent.", ex);
        }
    }

    protected final void sendAPIPostRequest(String requestURI, PathParameters pathParameters, BodyParameter bodyParameter, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIPostRequest(requestURI, pathParameters, new QueryParameters(), bodyParameter, xmlObject);
    }

    protected final void sendAPIPostRequest(String requestURI, QueryParameters queryParameters, BodyParameter bodyParameter, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIPostRequest(requestURI, new PathParameters(), queryParameters, bodyParameter, xmlObject);
    }

    protected final void sendAPIPostRequest(String requestURI, BodyParameter bodyParameter, XMLDefinedObject xmlObject) throws OauthException {
        sendAPIPostRequest(requestURI, new PathParameters(), new QueryParameters(), bodyParameter, xmlObject);
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
            throw new InvalidParameterException("The API manager must be configured before sending a request.");
    }

    protected void updateTimeOfLastRequest() {
        timeOfLastRequest = Instant.now();
    }
}
