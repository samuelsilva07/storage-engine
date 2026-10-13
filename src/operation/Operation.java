package operation;

import record.Record;

public class Operation {
    private static long currentId = 1;
    private final long id;
    private final OperationType type;
    private final Record record;
    private final long recordKey;

    private static long getNextId() {
        return currentId++;
    }

    // construtor para operações de PUT
    public Operation(OperationType type, Long key, String value) {
        this.id = getNextId();
        this.type = type;
        this.recordKey = key;
        this.record = Record.buildRecord(key, value);
    }

    // construtor para operações de GET/DELETE
    public Operation(OperationType type, Long key) {
        this.id = getNextId();
        this.type = type;
        this.recordKey = key;
        this.record = null;
    }

    public long getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public Record getRecord() {
        return record;
    }

    public long getRecordKey() {
        return recordKey;
    }
}
