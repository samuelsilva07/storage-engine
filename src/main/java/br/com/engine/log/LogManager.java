package br.com.engine.log;

import br.com.engine.operation.Operation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogManager implements ILogManager {
    private String logFileName;
    private int operationCount = 0;

    public LogManager(String logFileName) {
        this.logFileName = logFileName;
    }

    public void addOperation(String operationJson) {
        operationCount++;
        Long address;
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.logFileName, true)
        )) {
            writer.write(operationJson.trim());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Operation getOperation(Long id) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFileName, StandardCharsets.UTF_8)
        )) {
            String operationJson = reader.lines()
                                    .skip(id - 1)
                                    .findFirst()
                                    .get();
            return Operation.getFromJson(operationJson);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Operation> getOperation(Long id, Long lastId) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFileName)
        )) {
            List<Operation> operations = new ArrayList<>();
            // TODO: implementar o salto de id linhas e ler até a linha lastId
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
                new FileWriter(this.logFileName, true)
        )) {
            TODO: implementar método na etapa 2
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/

    public static void main(String[] args) {
        List<String> request = new ArrayList<>();

        for (int i = 0; i < 50_000; i++)
            request.add(String.format("{\"id\": %d, \"op\": \"put\", \"key\": 91, \"value\": \"abc\"}", i + 1));
        System.out.println("Vetor construído");

        LogManager logManager = new LogManager("log.jsonl");
        for (String op: request)
            logManager.addOperation(op);
        System.out.println("Arquivo construído");

        for (Long id = 1L; id <= request.size(); id++)
            System.out.println(logManager.getOperation(id));
        System.out.println("Todas as operações foram obtidas");

    }
}
