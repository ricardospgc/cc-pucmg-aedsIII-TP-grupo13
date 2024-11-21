package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ParIDRotulocID implements RegistroArvoreBMais<ParIDRotulocID>{
      
    /* Conexão entre ID de Categorias e ID de Tarefa */
    private int idCategoria;
    private int idTarefa;
    private final short TAMANHO = 8;
    
    public ParIDRotulocID(){
        this(-1,-1);
    }

    public ParIDRotulocID(int idCategoria){
        this(idCategoria, -1);
    }

    public ParIDRotulocID(int idCategoria, int idTarefa){
        try{
            this.idCategoria = idCategoria;
            this.idTarefa = idTarefa;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* SET's */
    public void setidCategoria(int idCategoria){
        
        this.idCategoria = idCategoria;
    } 
    
    public void setidTarefa(int idTarefa){
        
        this.idTarefa = idTarefa;
    }

    /* Fim dos Set's */
    
    /* GET's */
    public int getidCategoria(){
        
        return idCategoria;
    } 
    
    public int getidTarefa(){
        
        return idTarefa;
    }

    /* Fim dos GET's */
    
    @Override

    public ParIDRotulocID clone(){
        return new ParIDRotulocID(this.idCategoria, this.idTarefa);
    }

    public short size(){
        return this.TAMANHO;
    }

    public int compareTo(ParIDRotulocID a){
        if(this.idCategoria != a.idCategoria){
            return this.idCategoria - a.idCategoria;
        }else{
            return this.idTarefa == -1 ? 0 : this.idTarefa - a.idTarefa;
        }
    }

    public String toString(){
        return String.format("%3d", this.idCategoria) + ";" + String.format("%-3d", this.idTarefa);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idCategoria);
        dos.writeInt(this.idTarefa);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idCategoria = dis.readInt();
        this.idTarefa = dis.readInt();
    }

}