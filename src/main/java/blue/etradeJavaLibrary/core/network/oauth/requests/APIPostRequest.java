package blue.etradeJavaLibrary.core.network.oauth.requests;

import blue.etradeJavaLibrary.core.network.oauth.model.*;

public class APIPostRequest extends APIRequest {

    public APIPostRequest(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, pathParameters, queryParameters, keys, HttpMethod.POST);
        setRequestBody(bodyParameter);
    }

    public APIPostRequest(BaseURL baseURL, PathParameters pathParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, pathParameters, keys, HttpMethod.POST);
        setRequestBody(bodyParameter);
    }

    public APIPostRequest(BaseURL baseURL, QueryParameters queryParameters, BodyParameter bodyParameter, OauthKeySet keys) {
        super(baseURL, queryParameters, keys, HttpMethod.POST);
        setRequestBody(bodyParameter);
    }

    public APIPostRequest(BaseURL baseURL, OauthKeySet keys, BodyParameter bodyParameter) {
        super(baseURL, keys, HttpMethod.POST);
        setRequestBody(bodyParameter);
    }
}
