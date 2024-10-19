package Entidades;
import Enums.Prioridade;
import Enums.Status;
import Interface.Registro;
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

    private int id, idCategoria;
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
        this(nome, LocalDateTime.now(), status, prioridade, -1);
    }

    public Tarefa(String nome, LocalDateTime dataCriacao, Status status, Prioridade prioridade, int idCategoria){
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        if(status == Status.CONCLUIDO) 
            this.dataConclusao = dataCriacao;
        else // inicializa com a data 0, caso nao concluido
            this.dataConclusao = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        this.status = status;
        this.prioridade = prioridade;
        this.idCategoria = idCategoria;
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
    
    public void setStatus(Status s){
         this.status = s;
         // atualiza a data de conclusao quando o status mudar para CONCLUIDO
         if (s == Status.CONCLUIDO) 
            this.dataConclusao = LocalDateTime.now().plusMinutes(1);
    }
    public Status getStatus(){ return this.status; }
    
    public void setPrioridade(Prioridade p){ this.prioridade = p; }
    public Prioridade getPrioridade(){ return this.prioridade; }

    public void setIdCategoria(int idCategoria){ this.idCategoria = idCategoria; }
    public int getIdCategoria(){ return this.idCategoria; }
    
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
                "\n Prioridade: " + prioridade +
                "\n ID Categoria: " + idCategoria;
                
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
        dos.writeInt(idCategoria);

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
        
        this.idCategoria = dis.readInt();
        
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