package File;

import java.io.IOException;

// Interface que define os métodos para um registro do Hash Extensível
public interface RegistroHashExtensivel<T> {
  
  // Método que retorna o código hash do registro, utilizado para identificar a chave do objeto
  @Override
  public int hashCode();

  // Método que retorna o tamanho do registro em bytes
  public short size();

  // Método que serializa o registro para um array de bytes
  public byte[] toByteArray() throws IOException;

  // Método que deserializa um array de bytes para um registro
  public void fromByteArray(byte[] ba) throws IOException;
}
