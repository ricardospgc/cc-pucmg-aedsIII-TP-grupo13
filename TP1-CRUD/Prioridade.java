public enum Prioridade {
    BAIXA((byte) 0),
    MEDIA((byte) 1),
    ALTA((byte) 2);

    private final byte value;

    Prioridade(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static Prioridade fromByte(byte value) {
        for (Prioridade prioridade : Prioridade.values()) {
            if (prioridade.value == value) {
                return prioridade;
            }
        }
        throw new IllegalArgumentException("Valor de Prioridade inválido: " + value);
    }
}