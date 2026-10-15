package log;

import operation.Operation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogManager implements ILogManager {
    private Long maxBytes;
    private Long operationCount = 0L;
    private File logFile;

    public LogManager(Long maxBytes, String logFile) {
        this.maxBytes = maxBytes;
        this.logFile = new File(logFile);
    }

    public LogManager(String logFile) {
        this.logFile = new File(logFile);
    }

    public void addOperation(Operation operation) {
        operationCount++;
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.logFile, true)
        )) {
            writer.write(operation.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Operation getOperation(Long address) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFile)
        )) {
            String operation;
            reader.skip(address);
            operation = reader.readLine();
        //  return Operation.fromString(operation); TODO: adicionar conversão de JSON para objeto Operation
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Operation> getOperation(Long address, Long id, Long lastId) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFile)
        )) {
            List<Operation> operations = new ArrayList<>();
            String operation;
            reader.skip(address);
            int linesRead = (int) (lastId - id);
            for (int i = 0; i < linesRead; i++){
                operation = reader.readLine();
            //  operations.add(Operation.fromString(operation)); TODO: adicionar conversão de JSON para objeto Operation
            }
            return operations;
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
/*
    public void removeOldestOperation() {
        operationCount--;
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.logFile, true)
        )) {
            TODO: implementar método na etapa 2
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
}
