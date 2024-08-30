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

    /*
    *   Construtores da classe Tarfa
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
        this.dataConclusao = dataCriacao; // a data e a mesma durante a criacao, indicando que nao foi concluida
        this.status = status;
        this.prioridade = prioridade;
    }

    /*
    *   Gets e Sets
    */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

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

    /*
    *   Metodo toString: Escreve os atributos de um registro
    */
    @Override
    public String toString() {
        String resp = "\n ID: " + id +
                      "\n Nome: " + nome +
                      "\n Data de Criacao: " + dataCriacao.format(formatter) +
                      "\n Data de Conclusão: ";

        if(dataConclusao == dataCriacao) resp += dataConclusao.format(formatter);
        else resp += "---";

        resp += "\n Status: " + status +
                "\n Prioridade: " + prioridade;
                
        return resp;
    }

    /*
    *   Transforma um registro em bytes e retorna um array de bytes
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

    //Converte um LocalDateTime em long e o retorna 
    public long LocalDateTimeToLong(LocalDateTime d){
        return d.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /*
    *   Transforma um array de bytes para cada atributo do registro
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
    
    //Converte um long em LocalDateTime e o retorna
    public LocalDateTime LongToLocalDateTime(long d){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d), ZoneOffset.UTC);
    }
}

enum Prioridade {
    // estados
    BAIXA((byte) 0),
    MEDIA((byte) 1),
    ALTA((byte) 2),
    URGENTE((byte) 3);

    private final byte value;
    
    Prioridade(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    // converte um byte em um estado
    public static Prioridade fromByte(byte value) {
        for (Prioridade prioridade : Prioridade.values()) {
            if (prioridade.value == value) {
                return prioridade;
            }
        }
        throw new IllegalArgumentException("Valor de Prioridade inválido: " + value);
    }
}

enum Status {
    // valores
    PENDENTE((byte) 0),
    EM_PROGRESSO((byte) 1),
    CONCLUIDO((byte) 2);

    
    private final byte value;

    Status(byte value) { this.value = value; }

    public byte getValue() { return value; }

    /**
     * Converte o byte em um estado
     * @param value
     * @return
     */
    public static Status fromByte(byte value) {
        for (Status status : Status.values()) {
            if (status.value == value)
                return status;
        }
        throw new IllegalArgumentException("Valor de Status inválido: " + value);
    }
}