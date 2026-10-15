package br.com.engine.log;

import br.com.engine.operation.Operation;

import java.util.List;

public interface ILogManager {
    // File logFile

    void addOperation(Operation operation);
    Operation getOperation(Long address);
    List<Operation> getOperation(Long address, Long id, Long lastId);
    // void removeOldestOperation(); TODO: implementar na etapa 2
}
