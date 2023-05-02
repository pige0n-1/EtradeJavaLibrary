package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.*;

public class APIPutRequest extends APIRequest {

    public APIPutRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, pathParameters, queryParameters, keys, HttpMethod.PUT);
        setRequestBody(bodyParameter);
    }

    public APIPutRequest(BaseURL baseURL, PathParameters pathParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, pathParameters, keys, HttpMethod.PUT);
        setRequestBody(bodyParameter);
    }

    public APIPutRequest(BaseURL baseURL, QueryParameters queryParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, queryParameters, keys, HttpMethod.PUT);
        setRequestBody(bodyParameter);
    }

    public APIPutRequest(BaseURL baseURL, OauthKeySet keys, BodyParameter bodyParameter) {
        super(baseURL, keys, HttpMethod.PUT);
        setRequestBody(bodyParameter);
    }
}
