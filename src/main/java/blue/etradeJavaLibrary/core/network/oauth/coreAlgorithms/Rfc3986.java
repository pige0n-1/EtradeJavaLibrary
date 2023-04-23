
package blue.etradeJavaLibrary.core.network.oauth.coreAlgorithms;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

/**
 * Contains functionality to encode a string using RFC3986 percent-encoding.
 * The encode method only supports strings within the extended-ASCII range.
 * @author Hunter
 */
public final class Rfc3986 {
    
    /* Prevent instantiation */
    private Rfc3986() {}
    
    public static String encode(String string) {
        try {
            return percentEncode(string);
        }
        catch (UnsupportedEncodingException ex) {
            return null; // This will never happen
        }
    }
    
    public static String decode(String string) {
        try {
            return percentDecode(string);
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    
    // Private helper methods
    
    
    private static String percentDecode(String encodedString) throws UnsupportedEncodingException {
        StringBuilder decodedStringBuilder = new StringBuilder();
        
        // Iterate through the input encodedString
        String currentEscapeSequence;
        for (int i = 0; i < encodedString.length(); i++) {
            char currentCharacter = encodedString.charAt(i);
            
            if (currentCharacter == '%') {
                currentEscapeSequence = "";
                currentEscapeSequence += encodedString.charAt(++i);
                currentEscapeSequence += encodedString.charAt(++i);
                
                String decodedEscape = decodePercentEscapeSequence(currentEscapeSequence);
                decodedStringBuilder.append(decodedEscape);
            }
            else 
                decodedStringBuilder.append(currentCharacter);
        }
        
        return decodedStringBuilder.toString();
    }
    
    private static String decodePercentEscapeSequence(String escapeSequence) {
        assert escapeSequence.length() == 2;
        byte escapeByteValue = Byte.parseByte(escapeSequence, 16);
        return Character.toString(escapeByteValue);
    }
    
    private static String convertArrayListToString(ArrayList<Byte> arrayList) throws UnsupportedEncodingException{
        byte[] resultArray = new byte[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            resultArray[i] = arrayList.get(i);
        }
        
        return new String(resultArray, "UTF-8");
    }
    
    private static String percentEncode(String string) throws UnsupportedEncodingException {
        byte[] codePoints = string.getBytes();
        
        ArrayList<Byte> newCodePoints = new ArrayList<>();
        // Iterate through input string
        for (int i = 0; i < codePoints.length; i++) {
            byte currentCodePoint = codePoints[i];
            
            if (isAnUnreservedCharacter(currentCodePoint))
                newCodePoints.add(currentCodePoint);

            else { // Reserved characters must be replaced with "%" followed by uppercase hex value
                newCodePoints.add((byte)'%');
                String codePointHexString = Integer.toHexString(currentCodePoint).toUpperCase();
                byte[] codePointCodePoints = codePointHexString.getBytes();
                newCodePoints.add(codePointCodePoints[0]);
                newCodePoints.add(codePointCodePoints[1]);
            }
        }
        
        return convertArrayListToString(newCodePoints);
    }
    
    private static boolean isAnUnreservedCharacter(byte value) {
        return value >= '0' && value <= '9' || value >= 'A' && value <= 'Z' ||
                value >= 'a' && value <= 'z' || value == '-' || value == '.' ||
                value == '_' || value == '~';
    }
}
