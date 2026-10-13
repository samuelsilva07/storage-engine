package disk;

import record.Record;

import java.util.Map;

public interface IDiskManager {
    // File diskFile

    public void put(Record record, Long address);
    public String get(Long address);
    public void delete(Long address);
    public Map<Long, Long> getActiveRecords();
}
