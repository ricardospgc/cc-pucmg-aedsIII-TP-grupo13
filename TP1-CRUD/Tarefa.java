import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.*;

public class Tarefa implements Registro {

    private int id;
    private String nome;
    private LocalDateTime dataCriacao, dataConclusao;
    private Status status;
    private Prioridade prioridade;

    /**
     * Construtores
     
     */
    public Tarefa(){
        this("", Status.PENDENTE, Prioridade.BAIXA); 
    }

    public Tarefa(String nome, Status status, Prioridade prioridade){
        this(nome, LocalDateTime.now(), status, prioridade);
    }

    public Tarefa(String nome, LocalDateTime dataCriacao, Status status, Prioridade prioridade){
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        if(status == Status.CONCLUIDO) 
            this.dataConclusao = dataCriacao;
        else // inicializa com a data 0, caso nao concluido
            this.dataConclusao = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        this.status = status;
        this.prioridade = prioridade;
    }

    /*
    *   Gets e Sets
    */
    @Override
    public void setId(int id) { this.id = id; }
    @Override
    public int getId() { return id; }

    public void setNome(String nome){ this.nome = nome;}
    public String getNome(){ return this.nome; }

    public void setDataCriacao(LocalDateTime d){ this.dataCriacao = d; }
    public LocalDateTime getDataCriacao(){ return this.dataCriacao; }
    
    public void setDataConclusao(LocalDateTime d){ this.dataConclusao = d; }
    public LocalDateTime getDataConclusao(){ return this.dataConclusao; }
    
    public void setStatus(Status s){ this.status = s; }
    public Status getStatus(){ return this.status; }
    
    public void setPrioridade(Prioridade p){ this.prioridade = p; }
    public Prioridade getPrioridade(){ return this.prioridade; }
    
    // Formata LocalDateTime
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss");

    /**
     * Metodo responsavel por retornar uma String que descreve um registro
     * 
     * @param void
     * @return String
     */
    @Override
    public String toString() {
        String resp = "\n ID: " + id +
                      "\n Nome: " + nome +
                      "\n Data de Criacao: " + dataCriacao.format(formatter) +
                      "\n Data de Conclusao: ";

        if(status == Status.CONCLUIDO) resp += dataConclusao.format(formatter);
        else resp += "Nao Concluido";

        resp += "\n Status: " + status +
                "\n Prioridade: " + prioridade;
                
        return resp;
    }

    /**
     * Metodo responsavel por retornar um array de bytes contendo um registro
     * 
     * @param void
     * @return byte[]
     * @throws IOException Se ocorrer um erro de I/O durante o processo.
     */
    @Override
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeLong(LocalDateTimeToLong(this.dataCriacao));
        dos.writeLong(LocalDateTimeToLong(this.dataConclusao));
        dos.writeByte(status.getValue());
        dos.writeByte(prioridade.getValue());

        return baos.toByteArray();
    }

    /**
     * Metodo responsavel por ler os atributos e atribuir a um registro
     * 
     * @param byte[]
     * @throws IOException Se ocorrer um erro de I/O durante o processo.
     */
    @Override
    public void fromByteArray(byte[] b) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.dataCriacao = LongToLocalDateTime(dis.readLong());
        this.dataConclusao = LongToLocalDateTime(dis.readLong());
        
        byte statusByte = dis.readByte();
        this.status = Status.fromByte(statusByte);
        
        byte prioridadeByte = dis.readByte();
        this.prioridade = Prioridade.fromByte(prioridadeByte);
        
    }

    /**
     * Metodo responsavel por transformar um LocalDateTime em long
     * @param LocalDateTime
     * @return long
     */
    public long LocalDateTimeToLong(LocalDateTime d){
        return d.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    
    /**
     * Transformar um long em um LocalDateTime
     * 
     * @param long
     * @return LocalDateTime
     */
    public LocalDateTime LongToLocalDateTime(long d){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d), ZoneOffset.UTC);
    }

}// class Tarefa

// enum que descreve os valores de prioridade da tarefa
enum Prioridade {
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


// enum que descreve os valores de status da tarefa
enum Status {
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