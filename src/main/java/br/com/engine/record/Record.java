package br.com.engine.record;

import br.com.engine.utils.ByteArrayCodec;

import java.util.LinkedList;
import java.util.List;

public class Record {
    private RecordStatus status;
    private Long key;
    private int valueSize;
    private String value;

    private Record(Long key, int valueSize, String value) {
        this.status = RecordStatus.ACTIVE;
        this.key = key;
        this.valueSize = valueSize; 
        this.value = value;
    }

    public static Record buildRecord(Long key, String value) {
        /* TODO:
            - calcular tamanho do valor, em bytes
            - criar registro com seus dados
         */
        return null;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public Long getKey() {
        return key;
    }

    public int getValueSize() {
        return valueSize;
    }

    public void setValueSize(int valueSize) {
        this.valueSize = valueSize;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Byte> toByteArray() {
        List<Byte> byteArray = new LinkedList<>();
        byteArray.add((byte) RecordStatus.ACTIVE.getStatus());

        byte keyBytes[] = ByteArrayCodec.serializeLong(this.getKey());
        for (byte keyByte: keyBytes) byteArray.add(keyByte);

        byte valueSizeBytes[] = ByteArrayCodec.serializeInt(this.getValueSize());
        for (byte valueSizeByte: valueSizeBytes) byteArray.add(valueSizeByte);

        byte valueBytes[] = ByteArrayCodec.serializeString(this.getValue());
        for (byte valueByte: valueBytes) byteArray.add(valueByte);

        return byteArray;
    }
}
