package File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIDEndereco implements RegistroHashExtensivel<ParIDEndereco> {
    
    private int id;  
    private long endereco;    
    private final short TAMANHO = 12;  

    /*
     * Construtores
     */
    public ParIDEndereco() {
        this.id = -1;
        this.endereco = -1;
    }

    public ParIDEndereco(int id, long end) {
        this.id = id;
        this.endereco = end;
    }
    /*
     * Gets
     */
    public int getId() {
        return id;
    }

    public long getEndereco() {
        return endereco;
    }
    /*
     * Função Hash que retorna o id
     */
    @Override
    public int hashCode() {
        return this.id;
    }
    /*
     * Função que retorna o Tamanho de cada Registro
     */
    @Override
    public short size() {
        return this.TAMANHO;
    }
    /*
     * Método retorna uma String com o id e endereço
     */
    @Override
    public String toString() {
        return "("+this.id + ";" + this.endereco+")";
    }
    /*
     * Função que retorna um array de byte contendo um Registro
     */
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeLong(this.endereco);
        return baos.toByteArray();
    }
    /*
     * Função que lê os atributos de um array de byte e atribui a um registro
     */
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.endereco = dis.readLong();
    }

}