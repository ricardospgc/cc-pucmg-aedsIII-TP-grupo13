package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIDEndereco implements RegistroHashExtensivel<ParIDEndereco> {
    
    private int id;   // chave
    private long endereco;    // valor
    private final short TAMANHO = 12;  // tamanho em bytes

    // Construtor padrão que inicializa o id e o endereço com valores inválidos
    public ParIDEndereco() {
        this.id = -1;
        this.endereco = -1;
    }

    // Construtor que recebe o id e o endereço como parâmetros
    public ParIDEndereco(int id, long end) {
        this.id = id;
        this.endereco = end;
    }

    // Método que retorna o id
    public int getId() {
        return id;
    }

    // Método que retorna o endereço
    public long getEndereco() {
        return endereco;
    }

    // Método que retorna o hash code do objeto, que é o próprio id
    @Override
    public int hashCode() {
        return this.id;
    }

    // Método que retorna o tamanho do registro em bytes
    public short size() {
        return this.TAMANHO;
    }

    // Método que retorna a representação em String do objeto
    public String toString() {
        return "(" + this.id + ";" + this.endereco + ")";
    }

    // Serializa o objeto para um array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);  // Escreve o id
        dos.writeLong(this.endereco);  // Escreve o endereço
        return baos.toByteArray();
    }

    // Deserializa um array de bytes para um objeto ParIDEndereco
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();  // Lê o id
        this.endereco = dis.readLong();  // Lê o endereço
    }
}
