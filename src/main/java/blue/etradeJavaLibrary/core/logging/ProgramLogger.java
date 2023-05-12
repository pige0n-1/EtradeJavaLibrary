
package blue.etradeJavaLibrary.core.logging;

import java.io.IOException;
import java.io.File;
import java.time.*;
import java.time.format.*;
import java.util.logging.*;

/**
 * An internal logging class for the entire EtradeJavaLibrary project. There are multiple statically-generated
 * logging objects that can be used project-wide. There are boolean data fields that are used as options for logging,
 * such as clearing previous logs and if logging is allowed.
 */
public class ProgramLogger {
    
    // Instance data fields
    /**
     * The encapsulated Java API logger that is used internally
     */
    private final Logger javaLogger;
    
    // Static data fields
    private static final String NETWORK_LOG_FILE_NAME = "network_log.txt";
    private static final String API_LOG_FILE_NAME = "api_log.txt";
    private static final Level DEFAULT_LEVEL = Level.FINE;
    private static ProgramLogger networkInstanceOfProgramLogger;
    private static ProgramLogger apiInstanceOfProgramLogger;
    public static boolean clearPreviousLogs = false;
    public static boolean loggingAllowed = true;

    /* Prevent instantiation */
    private ProgramLogger(LoggerType loggerType) throws IOException {
        String fileName = NETWORK_LOG_FILE_NAME;
        
        if (loggerType == LoggerType.API) fileName = API_LOG_FILE_NAME;
        
        if (clearPreviousLogs) deleteOldLogFile(fileName);
        
        var fileHandler = new MyFileHandler(fileName);
        fileHandler.setFormatter(new SimpleLogFormatter());
        
        javaLogger = Logger.getLogger(loggerType.name().toLowerCase());
        javaLogger.setLevel(DEFAULT_LEVEL);
        javaLogger.addHandler(fileHandler);
    }

    /**
     * Returns the only instance of the network logger
     * @return
     */
    public static ProgramLogger getNetworkLogger() {
        return getProgramLogger(LoggerType.NETWORK);
    }

    /**
     * Returns the only instance of the API logger
     * @return
     */
    public static ProgramLogger getAPILogger() {
        return getProgramLogger(LoggerType.API);
    }

    /**
     * Returns the only instance of the specified logger (either NETWORK or API)
     * @param loggerType
     * @return
     */
    public static ProgramLogger getProgramLogger(LoggerType loggerType) {
        ensureInstantiationHasOccurred();
        
        if (loggerType == LoggerType.NETWORK) return networkInstanceOfProgramLogger;
        else return apiInstanceOfProgramLogger;
    }

    /**
     * Outputs a message to the appropriate *log.txt file
     * @param message
     */
    public void log(String message) {
        if (loggingAllowed) javaLogger.fine(message);
    }

    /**
     * Outputs label: message to the appropriate *log.txt file
     * @param label
     * @param message
     */
    public void log(String label, String message) {
        if (loggingAllowed) {
            String newMessage = String.format("%s: %s", label, message);
            javaLogger.fine(newMessage);
        }
    }

    /**
     * Lists the two types of logger supported in the library
     */
    public enum LoggerType {
        NETWORK,
        API
    }
    
    
    // Private helper methods
    
    
    private void deleteOldLogFile(String fileName) {
        var logFile = new java.io.File(fileName);
        
        if (logFile.exists()) logFile.delete();
    }
    
    private static void ensureInstantiationHasOccurred() {
        try {
            if (networkInstanceOfProgramLogger == null)
                networkInstanceOfProgramLogger = new ProgramLogger(LoggerType.NETWORK);
            
            if (apiInstanceOfProgramLogger == null)
                apiInstanceOfProgramLogger = new ProgramLogger(LoggerType.API);
        }
        
        catch (IOException ex) {
            System.out.println("Warning: program logging not functional.");
            loggingAllowed = false;
        }
    }
    
    private class SimpleLogFormatter extends Formatter {
        private static final String FORMAT_STRING = "hh:mm:ss a:";
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_STRING);
        
        @Override
        public String format(LogRecord record) {
            String message = String.format("%-14s%s%n", formatDate(), record.getMessage());
            
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
            ZoneId currentTimeZone = ZoneId.systemDefault();
            var currentTime = Instant.now().atZone(currentTimeZone);
            
            return formatter.format(currentTime);
        }
    }
    
    private class MyFileHandler extends FileHandler {
        
        // Static data fields
        private static final boolean APPEND_TO_LOG_FILE = true;
        
        public MyFileHandler(String fileName) throws IOException {
            super(fileName, APPEND_TO_LOG_FILE);
        }
        
        @Override
        public void close() {
            LogRecord newLine = new LogRecord(Level.INFO, "\n\n");
            publish(newLine);
            
            super.close();
        }
    }
}
