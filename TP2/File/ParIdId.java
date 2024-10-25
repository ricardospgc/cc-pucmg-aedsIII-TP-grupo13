package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdId implements RegistroArvoreBMais<ParIdId> {

  private int id1;
  private int id2;
  private short TAMANHO = 8;

  public ParIdId() {
    this(-1, -1);
  }

  public ParIdId(int n1) {
    this(n1, -1);
  }

  public ParIdId(int n1, int n2) {
    this.id1 = n1;
    this.id2 = n2;
  }

  public void setId1(int id) {
    this.id1 = id;
  }

  public int getId1() {
    return this.id1;
  }

  public void setId2(int id) {
    this.id2 = id;
  }

  public int getId2() {
    return this.id2;
  }

  @Override
  public ParIdId clone() {
    return new ParIdId(this.id1, this.id2);
  }

  public short size() {
    return this.TAMANHO;
  }

  public int compareTo(ParIdId a) {
    if (this.id1 != a.id1) {
      return this.id1 - a.id1;
    } else {
      return this.id2 == -1 ? 0 : this.id2 - a.id2;
    }
  }

  public String toString() {
    return String.format("%3d", this.id1) + ";" + String.format("%-3d", this.id2);
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    dos.writeInt(this.id1);
    dos.writeInt(this.id2);

    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);

    this.id1 = dis.readInt();
    this.id2 = dis.readInt();
  }
}
