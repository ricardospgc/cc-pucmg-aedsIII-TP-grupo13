package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {
    public int id;
    public String nome;
    private final short TAMANHO = 34;

    public ParNomeId() throws Exception {
        this("", -1);
    }

    public ParNomeId(String s) throws Exception {
        this(s, -1);
    }

    public ParNomeId(String nome, int id) throws Exception {
        this.id = id;
        if (nome.getBytes().length <= 26) {
            this.nome = nome;
        } else {
            throw new Exception("Nome extenso. Diminua o tamanho");
        }
    }

    @Override
    public ParNomeId clone() {
        ParNomeId p = null;
        try {
            p = new ParNomeId(this.nome, this.id);
        } catch (Exception e) {
        }
        return p;
    }

    private static String strnormalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
    }

    @Override
    public int compareTo(ParNomeId p) {
        return strnormalize(this.nome).compareTo(strnormalize(p.nome));
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public String toString() {
        return "(" + this.id + " , " + this.nome + ")";
    }

    @Override
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

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] vb = new byte[26]; 
        dis.read(vb);             
        this.nome = (new String(vb)).trim(); 
        this.id = dis.readInt();
    }
}
