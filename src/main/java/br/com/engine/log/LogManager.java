package br.com.engine.log;

import br.com.engine.operation.Operation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
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

    private Map<String, Object> buildOperationMap(String operationJson) {
        Gson jsonParser = new Gson();

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        return jsonParser.fromJson(operationJson, mapType);
    }

    public Operation getOperation(Long id) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.logFileName, StandardCharsets.UTF_8)
        )) {
            String operationJson = reader.lines()
                                    .skip(id - 1)
                                    .findFirst()
                                    .get();
            Map<String, Object> operationMap = buildOperationMap(operationJson);
            return Operation.getFromJson(operationMap);
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
}
