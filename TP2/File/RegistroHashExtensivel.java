package File;

import java.io.IOException;

public interface RegistroHashExtensivel<T> {
  @Override
  public int hashCode();
  public short size();
  public byte[] toByteArray() throws IOException;
  public void fromByteArray(byte[] ba) throws IOException;

}