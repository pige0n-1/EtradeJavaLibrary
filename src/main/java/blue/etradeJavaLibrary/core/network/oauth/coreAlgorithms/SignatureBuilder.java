
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;
import java.net.URL;
import java.util.Iterator;

public class SignatureBuilder {
    
    // Static data fields
    private static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    private SignatureBuilder() {}
    
    public static String buildSignature(URL url, Parameters parameters, Key consumerSecret, Key tokenSecret, HttpMethod httpMethod) {
        String urlWithoutQuery = getURLWithoutQuery(url);
        
        String normalizedParameters = normalizeParameters(parameters);
        String signatureBaseString = getSignatureBaseString(normalizedParameters, httpMethod, urlWithoutQuery);
        String signatureKey = buildSignatureKey(consumerSecret, tokenSecret);
        
        logger.log("Normalized parameters", normalizedParameters);
        logger.log("Signature base string", signatureBaseString);
        logger.log("Key for building oauth signature", signatureKey);

        return HmacSha1.doHmacSha1Base64(signatureKey, signatureBaseString);     
    }
    
    public static String buildSignature(URL url, Parameters parameters, Key consumerSecret, HttpMethod httpMethod) {
        return buildSignature(url, parameters, consumerSecret, new Key(), httpMethod);
    }
    
    
    // Private helper methods
    
    
    private static String buildSignatureKey(Key consumerSecret, Key tokenSecret) {
        String consumerSecretString = Rfc3986.encode(consumerSecret.getValue());
        String tokenSecretString = Rfc3986.encode(tokenSecret.getValue());
        
        return consumerSecretString + "&" + tokenSecretString;
    }

    private static String normalizeParameters(Parameters allParameters) {
        StringBuilder normalizedParameters = new StringBuilder();
        
        Iterator<Parameters.Parameter> parametersIterator = allParameters.iterator();
        while (parametersIterator.hasNext()) {
            Parameters.Parameter currentParameter = parametersIterator.next();
            
            normalizedParameters
                    .append(currentParameter.getKey())
                    .append("=")
                    .append(currentParameter.getValue());
            
            if (parametersIterator.hasNext()) normalizedParameters.append("&");
        }
        
        return normalizedParameters.toString();
    }
    
    private static String getSignatureBaseString(String normalizedParameters, HttpMethod httpMethod, String urlWithoutQuery) {
        return Rfc3986.encode(httpMethod.toString()) + "&" + 
                Rfc3986.encode(urlWithoutQuery) + "&" + 
                Rfc3986.encode(normalizedParameters);
    }
    
    private static String getURLWithoutQuery(URL url) {
        String fullURL = url.toString();
        String urlWithoutQuery = fullURL.replaceAll("\\?.+", "");
        return urlWithoutQuery;
    }
}
