package main.java.record;

public enum RecordStatus {
    NOACTIVE(0),
    ACTIVE(1);

    private int status;

    RecordStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
