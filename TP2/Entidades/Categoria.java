package Entidades;

import Interface.Registro;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Categoria implements Registro, Comparable<Categoria> {

    public int id;
    public String nome;

    public Categoria() {
        this(-1, "");
    }
    public Categoria(String n) {
        this(-1, n);
    }

    public Categoria(int i, String n) {
        this.id = i;
        this.nome = n;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String toString() {
        return "\nID..: " + this.id +
               "\nNome: " + this.nome;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }

    @Override
    public int compareTo(Categoria other) {
        return this.nome.compareTo(other.nome);
    }
}
