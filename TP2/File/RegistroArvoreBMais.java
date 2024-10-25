package File;
import java.io.IOException;

public interface RegistroArvoreBMais<T> {
  public short size();
  public byte[] toByteArray() throws IOException;
  public void fromByteArray(byte[] ba) throws IOException;
  public int compareTo(T obj);
  public T clone();
}
