package Interface;
import java.io.IOException;

/**
 * Interface de registro
 * Descreve os metodos obrigatorios para todos os registros
 */
public interface Registro {
    public void setId(int i);
    public int getId();
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] b) throws IOException;
}