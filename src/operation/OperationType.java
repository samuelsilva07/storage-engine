package operation;

public enum OperationType {
    PUT(0), GET(1), DELETE(2);

    private int operation;

    OperationType(int operation) {
        this.operation = operation;
    }

    public int getValue() {
        return operation;
    }
}
