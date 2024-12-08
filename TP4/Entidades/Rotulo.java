package Entidades;

import Interface.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Rotulo implements Registro {

    public int id;
    public String nome;

    // Construtor padrão
    public Rotulo() {
        this(-1, "");
    }

    // Construtor com apenas o nome
    public Rotulo(String n) {
        this(-1, n);
    }

    // Construtor com id e nome
    public Rotulo(int i, String n) {
        this.id = i;
        this.nome = n;
    }

    // Setter para id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para id
    public int getId() {
        return id;
    }

    // Getter para nome
    public String getNome() {
        return nome;
    }

    // Setter para nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Representação em string da Rotulo
    public String toString() {
        return "\nID..: " + this.id + "\nNome: " + this.nome;
    }

    // Converte o objeto para um array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);

        return baos.toByteArray();
    }

    // Popula o objeto a partir de um array de bytes
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }

}
