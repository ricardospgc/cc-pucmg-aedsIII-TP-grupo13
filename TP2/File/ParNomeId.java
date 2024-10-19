package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {
    private int id;
    private String nome;
    private final short TAMANHO = 34;

    public ParNomeId() {
        this.id = -1;
        this.nome = "";
    }

    public ParNomeId(String nome, int id) throws Exception {
        this.id = id;
        if (nome.getBytes().length <= 30) {
            this.nome = nome;
        } else{
            throw new Exception("Nome extenso. Diminua o tamanho");
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;                                 
    }

    @Override
    public ParNomeId clone() {
        ParNomeId p = null;
        try {
            p = new ParNomeId(this.nome, this.id);
        }
        catch ( Exception e ) {
        } 
        return p;
    } 

    private static String strnormalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
    } 

    public int compareTo(ParNomeId p) {
        return strnormalize(this.nome).compareTo( strnormalize(p.nome) );
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }
    @Override
    public String toString() {
        return "("+this.id + ";" + this.nome+")";
    }
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.nome);
        for(int i = this.nome.length();i<TAMANHO-4;i++){
            dos.writeByte(' ');
        }
        dos.writeInt(this.id);
        return baos.toByteArray();
    }
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.nome = dis.readUTF().trim();
        this.id = dis.readInt();
    }
}
