package Interface;

import java.io.IOException;

// Interface que define os métodos para um registro genérico
public interface Registro {
    
    // Método para definir o ID do registro
    public void setId(int i);

    // Método para obter o ID do registro
    public int getId();

    // Método que serializa o registro para um array de bytes
    public byte[] toByteArray() throws IOException;

    // Método que deserializa um array de bytes para um registro
    public void fromByteArray(byte[] b) throws IOException;
}
