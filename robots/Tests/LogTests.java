import log.LogEntry;
import log.LogLevel;
import log.LogWindowSource;
import log.Logger;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


public class LogTests {
    @Test
    void deleteOldMessagesWhenOverflowing(){
        LogWindowSource logWindowSource = new LogWindowSource(3);

        for (int i = 0; i < 2; i++){
            logWindowSource.append(LogLevel.Debug, "Old String");
        }
        for (int i = 0; i < 3; i++){
            logWindowSource.append(LogLevel.Debug, "New String");
        }

        assertEquals(3, logWindowSource.size());
        for (var value : logWindowSource.all()){
            assertEquals("New String", value.getMessage());
        }
    }

    @Test
    void appendNewMessagesToLogSource(){
        int beginSize = Logger.getDefaultLogSource().size();
        Logger.debug("debug");
        Logger.error("error");
        int endSize = Logger.getDefaultLogSource().size();

        Iterator<LogEntry> logSourceIterator = Logger.getDefaultLogSource().range(0, 2).iterator();
        LogEntry firstElement = logSourceIterator.next();
        LogEntry secondElement = logSourceIterator.next();

        assertEquals(0, beginSize);
        assertEquals(2, endSize);
        assertEquals(LogLevel.Debug, firstElement.getLevel());
        assertEquals("debug", firstElement.getMessage());
        assertEquals(LogLevel.Error, secondElement.getLevel());
        assertEquals("error", secondElement.getMessage());
    }

    @Test
    void returnEmptyCollectionWhenStartIndexIncorrect(){
        LogWindowSource logSource = new LogWindowSource(1);
        logSource.append(LogLevel.Debug, "debug");

        Iterable<LogEntry> firstRangeLogSource = logSource.range(-1, 2);
        Iterable<LogEntry> secondRangeLogSource = logSource.range(3, 2);

        assertIterableEquals(Collections.emptyList(), firstRangeLogSource);
        assertIterableEquals(Collections.emptyList(), secondRangeLogSource);
    }
}
