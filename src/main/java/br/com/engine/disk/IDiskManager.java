package br.com.engine.disk;

import br.com.engine.record.Record;

import java.util.Map;

public interface IDiskManager {
    // File diskFile

    void put(Record record, Long address);
    String get(Long address);
    void delete(Long address);
    Map<Long, Long> getActiveRecords();
}
