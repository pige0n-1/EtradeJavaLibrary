
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.responses.OauthFlowResponse;
import blue.etradeJavaLibrary.core.network.oauth.OauthParameterException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

public class OauthRequest extends BaseRequest {
    
    public OauthRequest(BaseURL baseURL, Key consumerKey, Key consumerSecret) throws OauthParameterException {   
        super(baseURL, consumerKey, consumerSecret, HttpMethod.GET);
    }
    
    @Override
    public OauthFlowResponse sendAndGetResponse() throws MalformedURLException, OauthParameterException, IOException {
        URL fullURL = buildFullURL();
     
        logger.log("Full URL", fullURL.toString());
        
        HttpURLConnection connection = getConnection(fullURL);
        
        InputStream connectionResponse = connection.getInputStream();
        
        return new OauthFlowResponse(connectionResponse);
    }
}
