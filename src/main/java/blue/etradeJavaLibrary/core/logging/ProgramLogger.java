
package blue.etradeJavaLibrary.core.logging;

import java.util.logging.*;
import java.io.IOException;
import java.util.Date;

public class ProgramLogger {
    private static final String LOG_FILE = "src/main/java/blue/etradeJavaLibrary/core/logging/log.txt";
    private static final Level DEFAULT_LEVEL = Level.FINE;
    private static final boolean APPEND_TO_LOG_FILE = true;
    
    private static final Logger encapsulatedJavaLogger = Logger.getLogger("network");
    private static ProgramLogger onlyInstanceOfProgramLogger;
    
    private ProgramLogger() throws IOException {
        FileHandler fileHandler = new MyFileHandler(LOG_FILE, APPEND_TO_LOG_FILE);
        fileHandler.setFormatter(new SimpleLogFormatter());
        encapsulatedJavaLogger.setLevel(DEFAULT_LEVEL);
        encapsulatedJavaLogger.addHandler(fileHandler);
    }
    
    public static ProgramLogger getProgramLogger() {
        ensureProgramLoggerInstanceHasBeenCreated();
        
        return onlyInstanceOfProgramLogger;
    }
    
    public void log(String message) {
        encapsulatedJavaLogger.fine(message);
    }
    
    public void log(String label, String message) {    
        String newMessage = String.format("%s: %s", label, message);
        encapsulatedJavaLogger.fine(newMessage);
    }
    
    
    // Private helper methods
    
    
    private static void ensureProgramLoggerInstanceHasBeenCreated() {
        try {
            if (onlyInstanceOfProgramLogger == null)
                onlyInstanceOfProgramLogger = new ProgramLogger();
        }
        catch (IOException ex) {
            System.out.println("Warning: program logging not functional.");
        }
    }
    
    private class SimpleLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder messageOutput = new StringBuilder();
            
            messageOutput.append(new Date().toString());
            messageOutput.append(":   ");
            messageOutput.append(record.getMessage());
            messageOutput.append("\n");
            
            // This will only be true upon closing the logger
            if (record.getLevel() == Level.INFO)
                return formatWithoutDate(record);
            
            return messageOutput.toString();
        }
        
        public String formatWithoutDate(LogRecord record) {
            StringBuilder messageOutput = new StringBuilder();
            
            messageOutput.append(record.getMessage());
            
            return messageOutput.toString();
        }
    }
    
    private class MyFileHandler extends FileHandler {
        
        public MyFileHandler(String fileName, boolean append) throws IOException {
            super(fileName, append);
        }
        
        @Override
        public void close() {
            LogRecord newLine = new LogRecord(Level.INFO, "\n\n");
            publish(newLine);
            
            super.close();
        } 
    }
}
