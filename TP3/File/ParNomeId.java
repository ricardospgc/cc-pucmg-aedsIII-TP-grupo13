package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {

    private String nome;  // Nome associado ao identificador
    private int id;  // Identificador
    private short TAMANHO = 30;  // Tamanho em bytes do registro

    // Construtor padrão que inicializa o nome e o identificador com valores padrão
    public ParNomeId() throws Exception {
        this("", -1);
    }

    // Construtor que recebe apenas o nome e inicializa o identificador com valor padrão
    public ParNomeId(String n) throws Exception {
        this(n, -1);
    }

    // Construtor que recebe o nome e o identificador como parâmetros
    public ParNomeId(String n, int i) throws Exception {
        if (n.getBytes().length > 26) {
            throw new Exception("Nome extenso demais. Diminua o número de caracteres.");
        }
        this.nome = n;
        this.id = i;
    }

    // Método para definir o identificador
    public void setId(int id) {
        this.id = id;
    }

    // Método para obter o identificador
    public int getId() {
        return this.id;
    }

    // Método para definir o nome
    public void setNome(String s) {
        this.nome = s;
    }

    // Método para obter o nome
    public String getNome() {
        return this.nome;
    }

    // Método que cria uma cópia do objeto
    @Override
    public ParNomeId clone() {
        try {
            return new ParNomeId(this.nome, this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método que retorna o tamanho do registro em bytes
    public short size() {
        return this.TAMANHO;
    }

    // Método que compara dois objetos ParNomeId usando a versão normalizada dos nomes
    public int compareTo(ParNomeId a) {
        return strnormalize(this.nome).compareTo(strnormalize(a.nome));
    }

    // Método que retorna a representação em String do objeto
    public String toString() {
        return this.id + ") " + this.nome;
    }

    // Serializa o objeto para um array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        byte[] vb = new byte[26];  // Array de bytes para armazenar o nome com tamanho fixo
        byte[] vbNome = this.nome.getBytes();

        int i = 0;

        // Copia os bytes do nome para o array de tamanho fixo
        while (i < vbNome.length) {
            vb[i] = vbNome[i];
            i++;
        }

        // Preenche o restante do array com espaços em branco
        while (i < 26) {
            vb[i] = ' ';
            i++;
        }
        dos.write(vb);  // Escreve o nome no array de bytes
        dos.writeInt(this.id);  // Escreve o identificador
        return baos.toByteArray();
    }

    // Deserializa um array de bytes para um objeto ParNomeId
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb = new byte[26];
        dis.read(vb);  // Lê o nome do array de bytes

        this.nome = (new String(vb)).trim();  // Remove espaços em branco do final do nome
        this.id = dis.readInt();  // Lê o identificador
    }

    // Método que normaliza uma string removendo acentos e convertendo para minúsculas
    private static String strnormalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
    }
}
