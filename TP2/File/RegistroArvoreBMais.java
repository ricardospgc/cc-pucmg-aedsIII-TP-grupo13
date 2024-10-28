package File;

import java.io.IOException;

// Interface que define os métodos para um registro da Árvore B+
public interface RegistroArvoreBMais<T> {
  
  // Método que retorna o tamanho do registro em bytes
  public short size();

  // Método que serializa o registro para um array de bytes
  public byte[] toByteArray() throws IOException;

  // Método que deserializa um array de bytes para um registro
  public void fromByteArray(byte[] ba) throws IOException;

  // Método que compara dois registros
  public int compareTo(T obj);

  // Método que cria uma cópia do registro
  public T clone();
}
