package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ParIdId implements RegistroArvoreBMais<ParIdId> {
    private int idCategoria; // chave
    private int idTarefa;    // valor
    private final short TAMANHO = 15;  // tamanho em bytes

    public ParIdId() throws Exception {
        this(-1, -1);
    } 

    public ParIdId(int idCategoria) throws Exception {
        this(idCategoria, -1);
    } 

    public ParIdId(int idCategoria, int idTarefa) throws Exception 
    {
        this.idCategoria = idCategoria;
        this.idTarefa = idTarefa;
    } 

    public int getIDCategoria() {
        return idCategoria;
    } 

    public int getIDTarefa() {
        return idTarefa;
    } 

    public short size() {
        return this.TAMANHO;
    } 

    public String toString() {
        return  String.format("%3d", this.idCategoria) + ";" + String.format("%3d", this.idTarefa);
    } 

    @Override
    public ParIdId clone(){
        ParIdId clone = null;
        try {
            clone = new ParIdId(this.idCategoria, this.idTarefa);
        } catch ( Exception e ) {
            e.printStackTrace();
        } 
        return clone;
    } 

    public int compareTo(ParIdId obj) {
        if(this.idCategoria != obj.idCategoria) return this.idCategoria - obj.idCategoria;
        else return this.idTarefa - obj.idTarefa;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idCategoria);
        dos.writeInt(this.idTarefa);
        return (baos.toByteArray());
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idCategoria = dis.readInt( );
        this.idTarefa    = dis.readInt();
    } 

}