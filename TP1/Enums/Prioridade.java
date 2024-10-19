package Enums;

public enum Prioridade {
    // valores
    BAIXA((byte) 0),
    MEDIA((byte) 1),
    ALTA((byte) 2),
    URGENTE((byte) 3);

    private final byte value;
    
    Prioridade(byte value) { this.value = value; }
    public byte getValue() { return value; }

    /**
     * Converte de byte para o valor correspondente
     * @param value
     * @return
     */
    public static Prioridade fromByte(byte value) {
        for (Prioridade prioridade : Prioridade.values()) {
            if (prioridade.value == value) {
                return prioridade;
            }
        }
        throw new IllegalArgumentException("Valor de Prioridade inválido: " + value);
    }// fromByte()
}// enum Prioridade