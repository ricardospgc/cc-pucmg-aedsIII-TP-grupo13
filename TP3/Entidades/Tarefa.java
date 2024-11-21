package Entidades;

import Enums.*;
import Interface.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Tarefa implements Registro {
    private int id;
    private int idCategoria;
    private String nome;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private Status status;
    private Prioridade prioridade;

    // Construtor padrao
    public Tarefa() {
        this(-1, -1, "", LocalDate.now(), LocalDate.now(), Status.PENDENTE, Prioridade.BAIXA);
    }

    // Construtor com parametros principais
    public Tarefa(int idCategoria, String nome, LocalDate dCria, LocalDate dConc, Status s, Prioridade p) {
        this(-1, idCategoria, nome, dCria, dConc, s, p);
    }

    // Construtor completo
    public Tarefa(int idTf, int idCat, String nome, LocalDate dCria, LocalDate dConc, Status s, Prioridade p) {
        this.id = idTf;
        this.idCategoria = idCat;
        this.nome = nome;
        this.dataCriacao = dCria;
        this.dataConclusao = dConc;
        this.status = s;
        this.prioridade = p;
    }

    // Getter e Setter para id
    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    // Getter e Setter para idCategoria
    public int getIdCategoria() { return this.idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    // Getter e Setter para nome
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }

    // Getter e Setter para dataCriacao
    public LocalDate getDataCriacao() { return this.dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    // Getter e Setter para dataConclusao
    public LocalDate getDataConclusao() { return this.dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    // Getter e Setter para status
    public Status getStatus() { return this.status; }
    public void setStatus(Status status) { 
        this.status = status;
        // Atualiza a data de conclusao quando o status mudar para CONCLUIDO
        if (status == Status.CONCLUIDO) 
            this.dataConclusao = LocalDate.now();
    }

    // Getter e Setter para prioridade
    public Prioridade getPrioridade() { return this.prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    // Converte o objeto para um array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(this.idCategoria);
        dos.writeUTF(this.nome);
        dos.writeInt((int) this.dataCriacao.toEpochDay());
        dos.writeInt((int) this.dataConclusao.toEpochDay());
        dos.writeByte(this.status.getValue());
        dos.writeByte(this.prioridade.getValue());

        return baos.toByteArray();
    }

    // Popula o objeto a partir de um array de bytes
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.idCategoria = dis.readInt();
        this.nome = dis.readUTF();
        this.dataCriacao = LocalDate.ofEpochDay(dis.readInt());
        this.dataConclusao = LocalDate.ofEpochDay(dis.readInt());

        byte statusByte = dis.readByte();
        this.status = Status.fromByte(statusByte);
        
        byte prioridadeByte = dis.readByte();
        this.prioridade = Prioridade.fromByte(prioridadeByte);
    }

    // Representacao em string da Tarefa
    public String toString() {
        String resp =
               "\nID............: " + this.id +
               "\nID Categoria..: " + this.idCategoria +
               "\nNome..........: " + this.nome +
               "\nData Criacao..: " + this.dataCriacao +
               "\nData Conclusao: ";

                if(status == Status.CONCLUIDO) resp += this.dataConclusao;
                else resp += "Nao Concluido";

        resp += "\nStatus........: " + this.status +
               "\nPrioridade....: " + this.prioridade;
        return resp;
    }
}
