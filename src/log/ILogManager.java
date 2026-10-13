package log;

import java.util.List;

public interface ILogManager {
    // File logFile

    void openLogFile();
    void closeLogFile();
    void addOperation(String operation);
    String getOperation(Long id);
    List<String> getOperation(Long id, Long lastId);
    void removeOldestOperation();
}
