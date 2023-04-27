
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import blue.etradeJavaLibrary.core.logging.ProgramLogger;
import blue.etradeJavaLibrary.core.network.oauth.model.HttpMethod;
import blue.etradeJavaLibrary.core.network.oauth.model.Key;
import blue.etradeJavaLibrary.core.network.oauth.model.Parameters;

/**
 * This class contains the functionality to build an oauth 1.0a signature. The
 * constructor requires an HttpOauth1Parameters object, which contains all
 * information necessary to build a signature. The resulting signature is
 * encoded in Base64.
 * @author Hunter
 */
import java.net.URL;

public class SignatureBuilder {
    private static final ProgramLogger logger = ProgramLogger.getNetworkLogger();
    
    private SignatureBuilder() {}
    
    public static String buildSignature(URL url, Parameters parameters, Key consumerSecret, Key tokenSecret, HttpMethod httpMethod) {
        String urlWithoutQuery = getURLWithoutQuery(url);
        
        String normalizedParameters = normalizeParameters(parameters);
        String signatureBaseString = getSignatureBaseString(normalizedParameters, httpMethod, urlWithoutQuery);
        String key = buildKey(consumerSecret, tokenSecret);
        
        logger.log("All parameters", parameters.toString());
        logger.log("Key for building oauth signature", key);
        logger.log("Signature base string", signatureBaseString);
        logger.log("Normalized parameters", normalizedParameters);

        return HmacSha1.doHmacSha1Base64(key, signatureBaseString);     
    }
    
    
    // Private helper methods
    
    
    private static String buildKey(Key consumerSecret, Key tokenSecret) {
        String consumerSecretString = Rfc3986.encode(consumerSecret.getValue());
        String tokenSecretString = Rfc3986.encode(tokenSecret.getValue());
        
        return consumerSecretString + "&" + tokenSecretString;
    }

    private static String normalizeParameters(Parameters parameters) {
        String normalizedString = "";
        
        // Iterate through all parameters
        for (Parameters.Parameter parameter : parameters) {
            normalizedString += parameter.getKey() + "=" + parameter.getValue() + "&";
        }
        // Remove "&" from the end of the string
        normalizedString = normalizedString.substring(0, 
                normalizedString.length() - 1);
        
        return normalizedString;
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
