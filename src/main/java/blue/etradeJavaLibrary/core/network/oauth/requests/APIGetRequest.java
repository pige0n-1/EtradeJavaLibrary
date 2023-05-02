
package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthException;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthKeySet;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;

/**
 * Represents a simple HTTP get request in the oauth 1.0a model.
 * This class extends APIRequest and does not add any extra functionality,
 * but it is added for functionality.
 */
public final class APIGetRequest extends APIRequest {

    public APIGetRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, OauthKeySet keys) throws OauthException {
        super(baseURL, pathParameters, queryParameters, keys, HttpMethod.GET);
    }

    public APIGetRequest(BaseURL baseURL, PathParameters pathParameters, OauthKeySet keys) throws OauthException {
        super(baseURL, pathParameters, keys, HttpMethod.GET);
    }

    public APIGetRequest(BaseURL baseURL, QueryParameters queryParameters, OauthKeySet keys) throws OauthException {
        super(baseURL, queryParameters, keys, HttpMethod.GET);
    }
    
    public APIGetRequest(BaseURL baseURL, OauthKeySet keys) throws OauthException {
        super(baseURL, keys, HttpMethod.GET);
    }
}
