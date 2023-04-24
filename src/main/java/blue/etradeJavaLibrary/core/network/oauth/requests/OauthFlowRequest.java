
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.responses.OauthFlowResponse;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

public class OauthFlowRequest extends BaseRequest {
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret) throws OauthException {   
        super(baseURL, consumerKey, consumerSecret, HttpMethod.GET);
    }
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, HttpMethod.GET);
    }
    
    public OauthFlowRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, Key verifier) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, verifier, HttpMethod.GET);
    }
    
    @Override
    public OauthFlowResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException {
        URL fullURL = buildFullURL();
     
        logger.log("Full URL", fullURL.toString());
        
        HttpURLConnection connection = getConnection(fullURL);
        
        InputStream connectionResponse = connection.getInputStream();
        
        return new OauthFlowResponse(connectionResponse);
    }
}
