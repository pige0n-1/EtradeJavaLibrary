
package blue.etradeJavaLibrary.core.logging;

import java.util.logging.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.io.Serializable;

public class ProgramLogger implements Serializable {
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
