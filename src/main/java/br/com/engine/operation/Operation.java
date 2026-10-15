package br.com.engine.operation;

import br.com.engine.record.Record;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
    public Operation(Long id, String type, Long recordKey, Long lastRecordKey) {
        this(id, type, null, recordKey, lastRecordKey);
    }

    public static Operation fromJson(String operation) {
        Gson jsonParser = new Gson();
        String jsonLine = jsonParser.toJson(operation);

        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> operationMap = jsonParser.fromJson(jsonLine, mapType);

        Long id = Long.getLong(operationMap.get("id"));
        String type = operationMap.get("type");
        Long key;

        if (type.equals("scan")) {
            key = Long.getLong(operationMap.get("start"));
            Long end = Long.getLong(operationMap.get("end"));
            return new Operation(id, type, key, end);
        } else {
            key = Long.getLong(operationMap.get("key"));
            if (type.equals("get")) {
                String value = operationMap.get("value");
                return new Operation(id, type, key, value);
            } else { // operações GET/DELETE
                return new Operation(id, type, key);
            }
        }
    }
}
