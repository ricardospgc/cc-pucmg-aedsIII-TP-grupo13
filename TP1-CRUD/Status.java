public enum Status {
    PENDENTE((byte) 0),
    EM_ANDAMENTO((byte) 1),
    CONCLUIDO((byte) 2);

    private final byte value;

    Status(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static Status fromByte(byte value) {
        for (Status status : Status.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor de Status inválido: " + value);
    }
}