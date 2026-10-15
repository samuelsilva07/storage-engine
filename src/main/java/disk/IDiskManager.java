package main.java.disk;

import main.java.record.Record;

import java.util.Map;

public interface IDiskManager {
    // File diskFile

    void put(Record record, Long address);
    String get(Long address);
    void delete(Long address);
    Map<Long, Long> getActiveRecords();
}
