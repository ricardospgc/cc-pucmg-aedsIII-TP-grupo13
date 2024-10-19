package File;

import Interface.Registro;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {

    final int CABECALHO = 4;
    Constructor<T> construtor;
    RandomAccessFile arquivo;
    String nomeArquivo;
    int numRegistros;
    HashExtensivel<ParIDEndereco> indiceDireto;

    // Construtor
    public Arquivo(Constructor<T> construtor, String nomeArquivo) throws Exception {
        // Apagar a pasta "BaseDeDados" e todo o seu conteúdo antes de recriá-la
        File f = new File("BaseDeDados");
        if (f.exists()) {
            deleteDirectory(f);  // Método auxiliar para apagar a pasta e seus conteúdos
        }
        f.mkdir();  // Recria a pasta após apagar

        numRegistros = 0;
        this.construtor = construtor;
        this.nomeArquivo = ".//BaseDeDados//" + nomeArquivo;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        if (arquivo.length() < CABECALHO) {
            arquivo.seek(0);
            arquivo.writeInt(0);
        }

        indiceDireto = new HashExtensivel<>(ParIDEndereco.class.getConstructor(), 4, this.nomeArquivo + ".d.idx", this.nomeArquivo + ".c.idx");
    }// Construtor

    // Método auxiliar para apagar uma pasta e todo o seu conteúdo
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) { // Checa se não é um arquivo vazio ou não acessível
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // Chamada recursiva para subpastas
                } else {
                    file.delete(); // Deleta arquivos
                }
            }
        }
        directory.delete(); // Deleta a pasta vazia
    }

    // ESTRUTURA DO ARQUIVO:
    /* ------------------------------------------------------ CABECALHO: int numRegistros */
 /* ----------------------------------------------------------------------------- CRUD */
 /* ------------------------------------------------------------ LAPIDE + TAMOBJ + OBJ */
    /**
     * <p>
     * Este metodo escreve um objeto no arquivo, atualizando o ID do objeto e
     * ajustando a posicao do ponteiro do arquivo conforme necessario. O metodo
     * tambem aumenta o valor do ID armazenado no inicio do arquivo antes de
     * gravar o objeto.</p>
     *
     * @param objeto O objeto a ser inserido no arquivo.
     * @return O ID do objeto inserido.
     * @throws IOException Se ocorrer algum erro de I/O durante a gravacao do
     * objeto no arquivo.
     */
    public int create(T objeto) throws Exception {
        arquivo.seek(0);
        int proximoId = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(proximoId);
        numRegistros++;
        objeto.setId(proximoId);
        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();

        byte[] b = objeto.toByteArray();
        arquivo.writeByte(' ');
        arquivo.writeShort(b.length);
        arquivo.write(b);

        indiceDireto.create(new ParIDEndereco(proximoId, endereco));

        return objeto.getId();
    }// create()

    /**
     * Metodo responsavel por ler um objeto do arquivo com base no seu ID.
     *
     * @param id O ID do objeto a ser lido.
     * @return O objeto correspondente ao ID, ou null se nao encontrado.
     * @throws IOException Se ocorrer um erro de I/O durante a leitura.
     */
    public T read(int id) throws Exception {
        if (id <= 0 || id > numRegistros) {
            throw new Exception("Id invalido para leitura: " + id);
        }

        byte[] b;
        byte lapide;
        T objeto;
        short tamRegistro;
        arquivo.seek(CABECALHO);

        ParIDEndereco pid = indiceDireto.read(id);

        if (pid != null) {
            arquivo.seek(pid.getEndereco());
            objeto = construtor.newInstance();
            lapide = arquivo.readByte();
            tamRegistro = arquivo.readShort();
            b = new byte[tamRegistro];
            arquivo.read(b);

            if (lapide == ' ') {
                objeto.fromByteArray(b);
                if (objeto.getId() == id) {
                    return objeto;
                }
            }
        }

        return null;
    }// read()

    /**
     * Metodo responsavel por atualizar um objeto do arquivo com base no seu ID.
     *
     * @param id O ID do objeto a ser atualizado.
     * @param T Objeto com os dados
     * @return boolean indicando se foi ou nao atualizado.
     * @throws IOException Se ocorrer um erro de I/O durante a atualizacao.
     */
    public boolean update(T novoObjeto) throws Exception {
        byte[] b;
        byte lapide;
        T objeto;
        short tamRegistro;

        ParIDEndereco pie = indiceDireto.read(novoObjeto.getId());

        /* quando o update atualizar o status de um objeto para concluido, 
        ele deve atualizar a data de conclusao pro momento do update */
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            objeto = construtor.newInstance();
            lapide = arquivo.readByte();

            if (lapide == ' ') {
                tamRegistro = arquivo.readShort();
                b = new byte[tamRegistro];
                arquivo.read(b);
                objeto.fromByteArray(b);

                if (objeto.getId() == novoObjeto.getId()) {
                    byte[] bb = novoObjeto.toByteArray();
                    short tamNovoRegistro = (short) bb.length;

                    if (tamNovoRegistro <= tamRegistro) {
                        arquivo.seek(pie.getEndereco() + 3);
                        arquivo.write(bb);
                    } else {
                        arquivo.seek(pie.getEndereco());
                        arquivo.writeByte('*');
                        arquivo.seek(arquivo.length());
                        long novoEndereco = arquivo.getFilePointer();
                        arquivo.writeByte(' ');
                        arquivo.writeShort(tamNovoRegistro);
                        arquivo.write(bb);
                        indiceDireto.update(new ParIDEndereco(objeto.getId(), novoEndereco));
                    }
                    return true;
                }
            }
        }
        return false;
    }// update

    /**
     * Metodo responsavel por deletar um objeto do arquivo com base no seu ID.
     *
     * @param id O ID do objeto a ser deletado.
     * @return boolean indicando se foi ou nao deletado.
     * @throws IOException Se ocorrer um erro de I/O durante o processo de
     * delete.
     */
    public boolean delete(int id) throws Exception {
        if (id <= 0 || id > numRegistros) {
            throw new Exception("Id invalido para leitura: " + id);
        }

        boolean fim = false;

        byte[] b;
        byte lapide;
        Long endereco;
        T objeto;
        short tamRegistro;

        arquivo.seek(CABECALHO);

        while (arquivo.getFilePointer() < arquivo.length() || !fim) {
            endereco = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            objeto = construtor.newInstance();
            tamRegistro = arquivo.readShort();
            b = new byte[tamRegistro];
            arquivo.read(b);

            if (lapide == ' ') {
                objeto.fromByteArray(b);
                if (objeto.getId() == id) {
                    arquivo.seek(endereco);
                    arquivo.write('*');
                    fim = true;
                }
            }
        }
        return fim;
    }// delete()

    /* ------------------------------------------------------------------------------------ */
}
