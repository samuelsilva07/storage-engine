package br.com.engine.disk;

import br.com.engine.record.Record;

import java.io.IOException;
import java.util.Map;

public interface IDiskManager {
    // File diskFile

    Long put(Record record, Long address) throws IOException;
    String get(Long address) throws IOException;
    void delete(Long address) throws IOException;
    Map<Long, Long> getActiveRecords() throws IOException, InvalidFileAddressException;
}
