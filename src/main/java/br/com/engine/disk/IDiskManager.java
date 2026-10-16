package br.com.engine.disk;

import main.java.record.Record;

import java.io.IOException;
import java.util.Map;

public interface IDiskManager {
    // File diskFile

    Long put(Record record, Long address) throws IOException;
    String get(Long address) throws IOException;
    void delete(Long address) throws IOException;
    Map<Long, Long> getActiveRecords() throws IOException, InvalidFileAddressException;
}
