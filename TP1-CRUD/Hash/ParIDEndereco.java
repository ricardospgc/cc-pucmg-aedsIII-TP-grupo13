package Hash;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIDEndereco implements RegistroHashExtensivel<ParIDEndereco> {
    
    private int id;   // chave
    private long endereco;    // valor
    private final short TAMANHO = 12;  // tamanho em bytes

    public ParIDEndereco() {
        this.id = -1;
        this.endereco = -1;
    }

    public ParIDEndereco(int id, long end) {
        this.id = id;
        this.endereco = end;
    }

    public int getId() {
        return id;
    }

    public long getEndereco() {
        return endereco;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
    @Override
    public short size() {
        return this.TAMANHO;
    }
    @Override
    public String toString() {
        return "("+this.id + ";" + this.endereco+")";
    }
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeLong(this.endereco);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.endereco = dis.readLong();
    }

}