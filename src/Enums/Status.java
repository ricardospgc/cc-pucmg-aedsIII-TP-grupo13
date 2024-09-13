package Enums;

public enum Status {
    // valores
    PENDENTE((byte) 0),
    EM_PROGRESSO((byte) 1),
    CONCLUIDO((byte) 2);

    
    private final byte value;

    Status(byte value) { this.value = value; }
    public byte getValue() { return value; }

    /**
     * Converte de byte para o valor correspondente
     * @param value
     * @return
     */
    public static Status fromByte(byte value) {
        for (Status status : Status.values()) {
            if (status.value == value)
                return status;
        }
        throw new IllegalArgumentException("Valor de Status inválido: " + value);
    }// fromByte()
}// enum Status