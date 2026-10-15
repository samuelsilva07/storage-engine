package main.java.operation;

import main.java.record.Record;

public record Operation (
        Long id,
        String type,
        Record record,
        long recordKey,
        long lastRecordKey // utilizado para marcar o último registro lido na operação de SCAN
) {
    // construtor para operações de GET/DELETE
    public Operation(Long id, String type, Long key) {
        this(id, type, null, key, key);
    }

    // construtor para operações de PUT
    public Operation(Long id, String type, Long key, String value) {
        this(id, type, Record.buildRecord(key, value), key, key);
    }

    // construtor para as operações de SCANs
    public Operation(Long id, String type, Long key, Long lastRecordKey) {
        this(id, type, null, key, lastRecordKey);
    }
}
