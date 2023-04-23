
package blue.etradeJavaLibrary.core.network.oauth;

import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import blue.etradeJavaLibrary.core.network.oauth.model.QueryParameters;
import blue.etradeJavaLibrary.core.network.oauth.model.BaseURL;
import blue.etradeJavaLibrary.core.network.oauth.model.PathParameters;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import blue.etradeJavaLibrary.core.logging.ProgramLogger;

/**
 * URLs generally contain three main components in this model. The base URL
 * contains the protocol and the path of the resource. The BaseURL class in the
 * blue.etradeJavaAPI.core.network.oauth.model package encapsulates a String
 * containing the baseURL and any variables that can be added in later. These
 * variables are surrounded by brackets 
 * (ex: "https://api.etrade.com/accounts/{accountNumber}"). Supplying a 
 * PathParameters object from the blue.etradeJavaAPI.core.network.oauth.model
 * package to the buildURL method would replace any path variables with the 
 * values of any PathParameters. The last component of the URL is QueryParameters,
 * which is found in the same package as PathParameters. QueryParameters is
 * parameters that are added on the end of the URL as a query.
 * @author Hunter
 */
public class URLBuilder {
    private static ProgramLogger logger = ProgramLogger.getProgramLogger();
    
    public static URL buildURL(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters)  throws MalformedURLException {
        String baseURLWithVariables = fillInVariables(baseURL.toString(), pathParameters);
        String fullURLString = addQueryParameters(baseURLWithVariables, queryParameters);
        
        return new URL(fullURLString);
    }
    
    public static URL buildURL(BaseURL baseURL, PathParameters pathParameters) throws MalformedURLException {
        return buildURL(baseURL, pathParameters, null);
    }
    
    public static URL buildURL(BaseURL baseURL, QueryParameters queryParameters) throws MalformedURLException {
        return buildURL(baseURL, null, queryParameters);
    }
    
    
    // Private helper methods
    
    
    private static String fillInVariables(String urlString, PathParameters pathParameters) {
        if (pathParameters == null || pathParameters.isEmpty())
            return urlString;
        
        String newURLString = urlString;
        for (Parameters.Parameter pathVariable : pathParameters) {
            String replacementRegex = "\\{" + pathVariable.getKey() + "\\}";
            newURLString = newURLString.replaceAll(replacementRegex, pathVariable.getValue());
        }
        return newURLString;
    }
    
    private static String addQueryParameters(String baseURL, QueryParameters queryParameters) throws MalformedURLException {
        if (queryParameters == null || queryParameters.isEmpty())
            return baseURL;
        
        StringBuilder finalURLString = new StringBuilder(baseURL);
        Iterator<Parameters.Parameter> parametersIterator = queryParameters.iterator();
        finalURLString.append("?");
        
        while(parametersIterator.hasNext()) {
            Parameters.Parameter currentParameter = parametersIterator.next();
            
            finalURLString.append(currentParameter.getKey());
            finalURLString.append("=");
            finalURLString.append(currentParameter.getValue());
            
            if (parametersIterator.hasNext())
                finalURLString.append("&");
        }
        
        return finalURLString.toString();
    }
}
