package br.com.engine.log;

import br.com.engine.operation.Operation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogManager implements ILogManager {
    private File logFile;
    private int operationCount = 0;
    private Map<Long, Long> filePositionMap = new HashMap<>();

    public LogManager(String logFile) {
        this.logFile = new File(logFile);
        filePositionMap.put(1L, 0L);
    }

    public Map<Long, Long> getFilePositionMap() {
        return filePositionMap;
    }

    public void addOperation(String operationJson) {
        operationCount++;
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.logFile, true)
        )) {
            writer.write(operationJson.trim());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        addNextPositionInMap(Operation.getFromJson(operationJson),
                (long) operationJson.length());
    }

    public void addNextPositionInMap(Operation operation, Long operationLength) {
        System.out.println("Address = " + operationLength);
        this.filePositionMap.put(operation.id() + 1, operationLength + 3);
    }

    public Operation getOperation(Long id) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFile, StandardCharsets.UTF_8)
        )) {
            String line;
            if (id != 1) {
                Long posSkip = this.filePositionMap.get(id) - 0.1;
                reader.skip(posSkip); // pula a quantidade de caracteres para a linha desejada
                System.out.println("Positions skipped: " + posSkip);
            }
            line = reader.readLine();
            System.out.println("line: " + line);
            return Operation.getFromJson(line);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Operation> getOperation(Long id, Long lastId) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFile)
        )) {
            List<Operation> operations = new ArrayList<>();
            String line;
            reader.skip(this.filePositionMap.get(id));
            int linesRead = (int) (lastId - id);
            for (int i = 0; i <= linesRead; i++){
                line = reader.readLine();
                operations.add(Operation.getFromJson(line));
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

    public static void main(String[] args) {
        String request[] = {
                "{\"id\": 1, \"op\": \"put\", \"key\": 91, \"value\": \"abc\"}",
                "{\"id\": 2, \"op\": \"get\", \"key\": 91}",
                "{\"id\": 3, \"op\": \"delete\", \"key\": 91}"
        };

        LogManager logManager = new LogManager("data1.dat");
        for (String op: request)
            logManager.addOperation(op);

        System.out.println(logManager.getFilePositionMap());

        System.out.println(logManager.getOperation(1L));
        System.out.println(logManager.getOperation(2L));
        System.out.println(logManager.getOperation(3L));

    }
}
