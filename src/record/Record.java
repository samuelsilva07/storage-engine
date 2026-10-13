package record;

public class Record {
    private RecordStatus status;
    private Long key;
    private Long valueSize;
    private String value;

    private Record(Long key, Long valueSize, String value) {
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
