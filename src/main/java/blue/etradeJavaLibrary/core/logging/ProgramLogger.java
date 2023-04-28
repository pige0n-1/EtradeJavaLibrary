
package blue.etradeJavaLibrary.core.logging;

import java.util.logging.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.io.Serializable;

public class ProgramLogger implements Serializable {
    
    // Instance data fields
    private final Logger javaLogger;
    
    // Static data fields
    public static boolean loggingAllowed = true;
    private static final String NETWORK_LOG_FILE_NAME = "src/main/java/blue/etradeJavaLibrary/core/logging/network_log.txt";
    private static final String API_LOG_FILE_NAME = "src/main/java/blue/etradeJavaLibrary/core/logging/api_log.txt";
    private static final Level DEFAULT_LEVEL = Level.FINE;
    private static final boolean APPEND_TO_LOG_FILE = true;
    private static ProgramLogger networkInstanceOfProgramLogger;
    private static ProgramLogger apiInstanceOfProgramLogger;
    public static boolean clearPreviousLogs = false;
    
    private ProgramLogger(LoggerType loggerType) throws IOException {
        String fileName = NETWORK_LOG_FILE_NAME;
        if (loggerType == loggerType.API)
            fileName = API_LOG_FILE_NAME;
        
        FileHandler fileHandler = new MyFileHandler(fileName, APPEND_TO_LOG_FILE);
        fileHandler.setFormatter(new SimpleLogFormatter());
        
        javaLogger = Logger.getLogger(loggerType.name().toLowerCase());
        javaLogger.setLevel(DEFAULT_LEVEL);
        javaLogger.addHandler(fileHandler);
    }
    
    public static ProgramLogger getNetworkLogger() {
        return getProgramLogger(LoggerType.NETWORK);
    }
    
    public static ProgramLogger getAPILogger() {
        return getProgramLogger(LoggerType.API);
    }
    
    public static ProgramLogger getProgramLogger(LoggerType loggerType) {
        ensureInstantiationHasOccurred();
        
        if (loggerType == LoggerType.NETWORK)
            return networkInstanceOfProgramLogger;
        else
            return apiInstanceOfProgramLogger;
    }
    
    public void log(String message) {
        if (loggingAllowed)
            javaLogger.fine(message);
    }
    
    public void log(String label, String message) {
        if (loggingAllowed) {
            String newMessage = String.format("%s: %s", label, message);
            javaLogger.fine(newMessage);
        }
    }
    
    public enum LoggerType {
        NETWORK,
        API
    }
    
    
    // Private helper methods
    
    
    private static void ensureInstantiationHasOccurred() {
        try {
            if (networkInstanceOfProgramLogger == null)
                networkInstanceOfProgramLogger = new ProgramLogger(LoggerType.NETWORK);
            
            if (apiInstanceOfProgramLogger == null)
                apiInstanceOfProgramLogger = new ProgramLogger(LoggerType.API);
        }
        
        catch (IOException ex) {
            System.out.println("Warning: program logging not functional.");
        }
    }
    
    private class SimpleLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String message = String.format("%20s:   %s%n", formatDate(), record.getMessage());
            
            // This will only be true upon closing the logger
            if (record.getLevel() == Level.INFO)
                return formatWithoutDate(record);
            
            return message;
        }
        
        public String formatWithoutDate(LogRecord record) {
            StringBuilder messageOutput = new StringBuilder();
            
            messageOutput.append(record.getMessage());
            
            return messageOutput.toString();
        }
        
        
        // Private helper methods
        
        
        private String formatDate() {
            StringBuilder formattedDate = new StringBuilder();
            Calendar currentTime = GregorianCalendar.getInstance();
            
            formattedDate.append(currentTime.get(Calendar.DAY_OF_MONTH));
            formattedDate.append(" ");
            
            formattedDate.append(currentTime.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.ENGLISH));
            formattedDate.append(" ");
            
            int hour = currentTime.get(Calendar.HOUR);
            hour = (hour == 0) ? 12 : hour;
            formattedDate.append(hour);
            formattedDate.append(":");
            
            formattedDate.append(currentTime.get(Calendar.MINUTE));
            formattedDate.append(":");
            
            formattedDate.append(currentTime.get(Calendar.SECOND));
            formattedDate.append(" ");
            
            formattedDate.append(currentTime.getDisplayName(Calendar.AM_PM, Calendar.SHORT_FORMAT, Locale.ENGLISH));
            
            return formattedDate.toString();
        }
    }
    
    private class MyFileHandler extends FileHandler {
        
        public MyFileHandler(String fileName, boolean append) throws IOException {
            super(fileName, !clearPreviousLogs);
        }
        
        @Override
        public void close() {
            LogRecord newLine = new LogRecord(Level.INFO, "\n\n");
            publish(newLine);
            
            super.close();
        }
    }
}
