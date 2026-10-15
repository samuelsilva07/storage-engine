package main.java.record;

public class Record {
    private boolean isActive;
    private Long key;
    private Long valueSize;
    private String value;

    private Record(Long key, Long valueSize, String value) {
        this.isActive = true;
        this.key = key;
        this.valueSize = valueSize;
        this.value = value;
    }

    public static Record buildRecord(Long key, String value) {
        /* TODO:
            - calcular tamanho do valor, em bytes
            - criar registro com seus dados
         */
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getKey() {
        return key;
    }

    public Long getValueSize() {
        return valueSize;
    }

    public void setValueSize(Long valueSize) {
        this.valueSize = valueSize;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
