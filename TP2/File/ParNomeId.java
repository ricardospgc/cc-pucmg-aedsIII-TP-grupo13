package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {

    private String nome;
    private int id;
    private short TAMANHO = 30;

    public ParNomeId() throws Exception {
        this("", -1);
    }

    public ParNomeId(String n) throws Exception {
        this(n, -1);
    }

    public ParNomeId(String n, int i) throws Exception {
        if (n.getBytes().length > 26) {
            throw new Exception("Nome extenso demais. Diminua o número de caracteres.");
        }
        this.nome = n;
        this.id = i;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setNome(String s) {
        this.nome = s;
    }

    public String getNome() {
        return this.nome;
    }

    @Override
    public ParNomeId clone() {
        try {
            return new ParNomeId(this.nome, this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int compareTo(ParNomeId a) {
        return strnormalize(this.nome).compareTo(strnormalize(a.nome));
    }

    public String toString() {
        return this.id + ") " + this.nome;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        byte[] vb = new byte[26];
        byte[] vbNome = this.nome.getBytes();

        int i = 0;

        while (i < vbNome.length) {
            vb[i] = vbNome[i];
            i++;
        }

        while (i < 26) {
            vb[i] = ' ';
            i++;
        }
        dos.write(vb);
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb = new byte[26];
        dis.read(vb);

        this.nome = (new String(vb)).trim();
        this.id = dis.readInt();
    }

    private static String strnormalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
    }
}
