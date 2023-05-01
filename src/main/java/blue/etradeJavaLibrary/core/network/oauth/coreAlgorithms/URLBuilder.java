
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

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
    
    // Static data fields
    private static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    public static URL buildURL(BaseURL baseURL, PathParameters pathParameters, QueryParameters queryParameters) throws MalformedURLException {
        BaseURL baseURLWithVariables = fillInVariables(baseURL, pathParameters);
        
        if (baseURLWithVariables.hasUnfilledVariables()) {
            logger.log("There is a mismatch between BaseURL path variables and PathParameters.");
            throw new InvalidParameterException("There is a mismatch between base URL path variables and the PathParameters object");
        }
        
        URL fullURL = addQueryParameters(baseURLWithVariables, queryParameters);
        
        return fullURL;
    }
    
    public static URL buildURL(BaseURL baseURL, PathParameters pathParameters) throws MalformedURLException {
        return buildURL(baseURL, pathParameters, new QueryParameters());
    }
    
    public static URL buildURL(BaseURL baseURL, QueryParameters queryParameters) throws MalformedURLException {
        return buildURL(baseURL, new PathParameters(), queryParameters);
    }
    
    
    // Private helper methods
    
    
    private static BaseURL fillInVariables(BaseURL baseURL, PathParameters pathParameters) {
        String urlString = baseURL.toString();
        
        for (Parameters.Parameter pathVariable : pathParameters) {
            String replacementRegex = "\\{" + pathVariable.getKey() + "\\}";
            urlString = urlString.replaceAll(replacementRegex, pathVariable.getValue());
        }
        
        return new BaseURL(urlString);
    }
    
    private static URL addQueryParameters(BaseURL baseURL, QueryParameters queryParameters) throws MalformedURLException {
        StringBuilder finalURLString = new StringBuilder(baseURL.toString());
        
        Iterator<Parameters.Parameter> parametersIterator = queryParameters.iterator();
        if (parametersIterator.hasNext()) finalURLString.append("?");
        
        while(parametersIterator.hasNext()) {
            Parameters.Parameter currentParameter = parametersIterator.next();
            
            finalURLString.append(currentParameter.getKey());
            finalURLString.append("=");
            finalURLString.append(currentParameter.getValue());
            
            if (parametersIterator.hasNext()) finalURLString.append("&");
        }
        
        return new URL(finalURLString.toString());
    }
}
