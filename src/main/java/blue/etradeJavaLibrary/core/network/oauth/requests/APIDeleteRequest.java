package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.OauthKeySet;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;

public class APIDeleteRequest extends APIRequest {
    public APIDeleteRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, OauthKeySet keys) {
        super(baseURL, pathParameters, queryParameters, keys, HttpMethod.DELETE);
    }

    public APIDeleteRequest(BaseURL baseURL, PathParameters pathParameters, OauthKeySet keys, HttpMethod httpMethod) {
        super(baseURL, pathParameters, keys, HttpMethod.DELETE);
    }

    public APIDeleteRequest(BaseURL baseURL, QueryParameters queryParameters, OauthKeySet keys, HttpMethod httpMethod) {
        super(baseURL, queryParameters, keys, HttpMethod.DELETE);
    }

    public APIDeleteRequest(BaseURL baseURL, OauthKeySet keys, HttpMethod httpMethod) {
        super(baseURL, keys, HttpMethod.DELETE);
    }
}
