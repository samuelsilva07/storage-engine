package log;

import java.util.List;

public interface ILogManager {
    // File logFile

    public void openLogFile();
    public void closeLogFile();
    public void addOperation(String operation);
    public String getOperation(Long id);
    public List<String> getOperation(Long id, Long lastId);
    public void removeOldestOperation();
}
