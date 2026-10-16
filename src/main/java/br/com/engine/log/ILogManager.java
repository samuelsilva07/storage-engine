package br.com.engine.log;

import br.com.engine.operation.Operation;

import java.util.List;

public interface ILogManager {
    // File logFile

    void addOperation(String operationJson);
    Operation getOperation(Long id);
    List<Operation> getOperation(Long id, Long lastId);
    // void removeOldestOperation(); TODO: implementar na etapa 2
}
