
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import blue.etradeJavaLibrary.core.network.oauth.responses.APIResponse;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.IOException;

public final class APIRequest extends BaseRequest {
    
    private PathParameters pathParameters;
    private QueryParameters queryParameters;
    
    public APIRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, Key consumerKey, Key consumerSecret, Key token, Key tokenSecret, Key verifier, HttpMethod httpMethod) throws OauthException {
        super(baseURL, consumerKey, consumerSecret, token, tokenSecret, verifier, httpMethod);
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
    }
    
    @Override
    public APIResponse sendAndGetResponse() throws MalformedURLException, OauthException, IOException {
        URL fullURL = buildFullURL(pathParameters, queryParameters);
        
        logger.log("Full URL", fullURL.toString());
        
        Parameters allParameters = Parameters.merge(pathParameters, queryParameters);
        HttpURLConnection connection = getConnection(fullURL, allParameters);
        
        InputStream connectionResponse = connection.getInputStream();
        
        return new APIResponse(connectionResponse);
    }
}
