package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ParIDRotulocID implements RegistroArvoreBMais<ParIDRotulocID>{
      
    /* Conexão entre ID de Rotulos e ID de Tarefa */
    private int idRotulo;
    private int idTarefa;
    private final short TAMANHO = 8;
    
    public ParIDRotulocID(){
        this(-1,-1);
    }

    public ParIDRotulocID(int idRotulo){
        this(idRotulo, -1);
    }

    public ParIDRotulocID(int idRotulo, int idTarefa){
        try{
            this.idRotulo = idRotulo;
            this.idTarefa = idTarefa;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* SET's */
    public void setidRotulo(int idRotulo){
        
        this.idRotulo = idRotulo;
    } 
    
    public void setidTarefa(int idTarefa){
        
        this.idTarefa = idTarefa;
    }

    /* Fim dos Set's */
    
    /* GET's */
    public int getidRotulo(){
        
        return idRotulo;
    } 
    
    public int getidTarefa(){
        
        return idTarefa;
    }

    /* Fim dos GET's */
    
    @Override
    public ParIDRotulocID clone(){
        return new ParIDRotulocID(this.idRotulo, this.idTarefa);
    }

    public short size(){
        return this.TAMANHO;
    }

    public int compareTo(ParIDRotulocID a){
        if(this.idRotulo != a.idRotulo){
            return this.idRotulo - a.idRotulo;
        }else{
            return this.idTarefa == -1 ? 0 : this.idTarefa - a.idTarefa;
        }
    }

    public String toString(){
        return String.format("%3d", this.idRotulo) + ";" + String.format("%-3d", this.idTarefa);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idRotulo);
        dos.writeInt(this.idTarefa);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idRotulo = dis.readInt();
        this.idTarefa = dis.readInt();
    }

}