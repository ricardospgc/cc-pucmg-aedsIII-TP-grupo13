package Entidades;

import Enums.*;
import Interface.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tarefa implements Registro {
    private int id;
    private int idCategoria;
    private ArrayList<Integer> idRotulos;
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
        this.idRotulos = new ArrayList<Integer>();
    }
    // Construtor completo
    public Tarefa(int idCat, String nome, LocalDate dCria, LocalDate dConc, Status s, Prioridade p, ArrayList<Integer> idRotulos) {
        this.idCategoria = idCat;
        this.nome = nome;
        this.dataCriacao = dCria;
        this.dataConclusao = dConc;
        this.status = s;
        this.prioridade = p;
        this.idRotulos = new ArrayList<>(idRotulos);
    }
    public Tarefa(int idTf, int idCat, String nome, LocalDate dCria, LocalDate dConc, Status s, Prioridade p, ArrayList<Integer> idRotulos) {
        this.id = idTf;
        this.idCategoria = idCat;
        this.nome = nome;
        this.dataCriacao = dCria;
        this.dataConclusao = dConc;
        this.status = s;
        this.prioridade = p;
        this.idRotulos = new ArrayList<>(idRotulos);
    }


    // Getter e Setter para id
    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    // Getter e Setter para idCategoria
    public int getIdCategoria() { return this.idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    // Getter e Setter para idCategoria
    public ArrayList<Integer> getIdRotulos() { return this.idRotulos; }
    public void setIdRotulos(ArrayList<Integer> idRotulos) { this.idRotulos = idRotulos; }

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
        dos.writeUTF(this.nome);
        dos.writeInt((int) this.dataCriacao.toEpochDay());
        dos.writeInt((int) this.dataConclusao.toEpochDay());
        dos.writeByte(this.status.getValue());
        dos.writeByte(this.prioridade.getValue());
        //Chaves Estrangeiras
        dos.writeInt(this.idCategoria);
        dos.writeInt(this.idRotulos.size());
        for(int i=0;i<this.idRotulos.size();i++){
            dos.writeInt(this.idRotulos.get(i));
        }

        return baos.toByteArray();
    }

    // Popula o objeto a partir de um array de bytes
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.dataCriacao = LocalDate.ofEpochDay(dis.readInt());
        this.dataConclusao = LocalDate.ofEpochDay(dis.readInt());
        byte statusByte = dis.readByte();
        this.status = Status.fromByte(statusByte);
        byte prioridadeByte = dis.readByte();
        this.prioridade = Prioridade.fromByte(prioridadeByte);
        //Chaves Estrangeiras
        this.idCategoria = dis.readInt();
        int tamanho = dis.readInt();
        for(int i=0;i<tamanho;i++){
            this.idRotulos.add(dis.readInt());
        }
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
