package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ParIDRotulocID implements RegistroArvoreBMais<ParIDRotulocID>{
      
    /* ID de Rotulos e ID de Tarefa */
    private int idRotulo;
    private int idTarefa;
    private final short TAMANHO = 8;
    
    /*
     * Construtores da classe ParIDRotulocID
     */
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
    
    /*
     * Método para clonar um objeto do tipo ParIDRotulocID
     */
    @Override
    public ParIDRotulocID clone(){
        return new ParIDRotulocID(this.idRotulo, this.idTarefa);
    }

    /*
     * Método para retornar o tamanho do objeto em bytes
     */
    public short size(){
        return this.TAMANHO;
    }

    /*
     * Método para comparar dois objetos do tipo ParIDRotulocID
     */
    @Override
    public int compareTo(ParIDRotulocID a){
        if(this.idRotulo != a.idRotulo){
            return this.idRotulo - a.idRotulo;
        }else{
            return this.idTarefa == -1 ? 0 : this.idTarefa - a.idTarefa;
        }
    }

    /*
     * Método para escrever os dados do objeto em um stream de saída
     */
    @Override
    public String toString(){
        return String.format("%3d", this.idRotulo) + ";" + String.format("%-3d", this.idTarefa);
    }

    /*
     * Método para escrever os dados do objeto em um stream de saída
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idRotulo);
        dos.writeInt(this.idTarefa);
        return baos.toByteArray();
    }

    /*
     * Método para ler os dados do objeto a partir de um stream de entrada
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idRotulo = dis.readInt();
        this.idTarefa = dis.readInt();
    }

}