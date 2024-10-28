package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdId implements RegistroArvoreBMais<ParIdId> {

  private int id1;  // Primeiro identificador
  private int id2;  // Segundo identificador
  private short TAMANHO = 8;  // Tamanho em bytes do registro

  // Construtor padrão que inicializa os identificadores com valores inválidos
  public ParIdId() {
    this(-1, -1);
  }

  // Construtor que recebe um identificador e inicializa o outro com valor inválido
  public ParIdId(int n1) {
    this(n1, -1);
  }

  // Construtor que recebe os dois identificadores como parâmetros
  public ParIdId(int n1, int n2) {
    this.id1 = n1;
    this.id2 = n2;
  }

  // Método para definir o valor do primeiro identificador
  public void setId1(int id) {
    this.id1 = id;
  }

  // Método para obter o valor do primeiro identificador
  public int getId1() {
    return this.id1;
  }

  // Método para definir o valor do segundo identificador
  public void setId2(int id) {
    this.id2 = id;
  }

  // Método para obter o valor do segundo identificador
  public int getId2() {
    return this.id2;
  }

  // Método que cria uma cópia do objeto
  @Override
  public ParIdId clone() {
    return new ParIdId(this.id1, this.id2);
  }

  // Método que retorna o tamanho do registro em bytes
  public short size() {
    return this.TAMANHO;
  }

  // Método que compara dois objetos ParIdId
  public int compareTo(ParIdId a) {
    if (this.id1 != a.id1) {
      return this.id1 - a.id1;  // Compara pelo primeiro identificador
    } else {
      return this.id2 == -1 ? 0 : this.id2 - a.id2;  // Se o primeiro for igual, compara pelo segundo
    }
  }

  // Método que retorna a representação em String do objeto
  public String toString() {
    return String.format("%3d", this.id1) + ";" + String.format("%-3d", this.id2);
  }

  // Serializa o objeto para um array de bytes
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    dos.writeInt(this.id1);  // Escreve o primeiro identificador
    dos.writeInt(this.id2);  // Escreve o segundo identificador

    return baos.toByteArray();
  }

  // Deserializa um array de bytes para um objeto ParIdId
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);

    this.id1 = dis.readInt();  // Lê o primeiro identificador
    this.id2 = dis.readInt();  // Lê o segundo identificador
  }
}
