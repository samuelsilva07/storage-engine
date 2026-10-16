package br.com.engine.operation;

import br.com.engine.record.Record;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    public static Operation getFromJson(String operationJson) {
        Gson jsonParser = new Gson();

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> operationMap = jsonParser.fromJson(operationJson, mapType);

        System.out.println(operationMap);

        Long id = ((Number) operationMap.get("id")).longValue();
        String op = operationMap.get("op").toString();
        Long key;

        if (op.equals("scan")) {
            key = ((Number)operationMap.get("start")).longValue();
            Long end = ((Number)operationMap.get("end")).longValue();
            return new Operation(id, op, key, end);
        } else {
            key = ((Number)operationMap.get("key")).longValue();
            if (op.equals("put")) {
                String value = operationMap.get("value").toString();
                return new Operation(id, op, key, value);
            } else { // operações GET/DELETE
                return new Operation(id, op, key);
            }
        }
    }
}
