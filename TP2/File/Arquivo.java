package File;

import Interface.Registro;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {
    final int TAM_CABECALHO = 4;  // Tamanho do cabeçalho do arquivo
    RandomAccessFile arquivo;  // Referência para o arquivo binário
    String nomeArquivo;  // Nome do arquivo
    Constructor<T> construtor;  // Construtor para criar objetos do tipo T
    HashExtensivel<ParIDEndereco> indiceDireto;  // Índice direto para acessar registros

    // Construtor da classe Arquivo, recebendo o nome do arquivo e o construtor dos objetos
    public Arquivo(String na, Constructor<T> c) throws Exception {
        // Cria o diretório BaseDeDados, caso não exista
        File d = new File(".\\BaseDeDados");
        if (!d.exists())
            d.mkdir();

        // Define o nome do arquivo e inicializa variáveis
        this.nomeArquivo = ".\\BaseDeDados\\" + na + ".db";
        this.construtor = c;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");

        // Caso o arquivo esteja vazio, escreve o cabeçalho inicial (valor 0)
        if (arquivo.length() < TAM_CABECALHO)
            arquivo.writeInt(0);

        // Inicializa o índice direto utilizando Hash Extensível
        indiceDireto = new HashExtensivel<>(
                ParIDEndereco.class.getConstructor(),
                4,
                ".\\BaseDeDados\\" + na + ".hash_d.db",
                ".\\BaseDeDados\\" + na + ".hash_c.db");
    }

    // Método para criar um novo registro no arquivo
    public int create(T obj) throws Exception {
        // Atualiza o cabeçalho com o próximo ID disponível
        arquivo.seek(0);
        int proximoId = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(proximoId);
        obj.setId(proximoId);

        // Define o local no final do arquivo para gravar o novo registro
        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();

        // Escreve o registro no arquivo, incluindo a lápide e o tamanho
        byte[] b = obj.toByteArray();
        arquivo.writeByte(' ');  // Lápide indicando registro ativo
        arquivo.writeShort(b.length);  // Tamanho do registro
        arquivo.write(b);  // Conteúdo do registro

        // Atualiza o índice direto com o novo registro
        indiceDireto.create(new ParIDEndereco(proximoId, endereco));

        return obj.getId();  // Retorna o ID do registro criado
    }

    // Método para ler um registro a partir do ID
    public T read(int id) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        // Busca o endereço do registro no índice direto
        ParIDEndereco pid = indiceDireto.read(id);
        if (pid != null) {
            // Posiciona o ponteiro no endereço do registro e lê os dados
            arquivo.seek(pid.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {  // Verifica se o registro não está excluído
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == id)
                    return obj;  // Retorna o objeto lido
            }
        }

        return null;  // Retorna null caso o registro não seja encontrado
    }

    // Método para excluir um registro a partir do ID
    public boolean delete(int id) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        // Busca o endereço do registro no índice direto
        ParIDEndereco pie = indiceDireto.read(id);
        if (pie != null) {
            // Posiciona o ponteiro no endereço do registro e lê os dados
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {  // Verifica se o registro não está excluído
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == id) {
                    // Atualiza o índice e marca o registro como excluído (lápide '*')
                    if (indiceDireto.delete(id)) {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        return true;  // Retorna true indicando que o registro foi excluído
                    }
                }
            }
        }

        return false;  // Retorna false caso o registro não seja encontrado ou não seja excluído
    }

    // Método para atualizar um registro existente
    public boolean update(T novo_obj) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        // Busca o endereço do registro no índice direto
        ParIDEndereco pie = indiceDireto.read(novo_obj.getId());
        if (pie != null) {
            // Posiciona o ponteiro no endereço do registro e lê os dados
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {  // Verifica se o registro não está excluído
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == novo_obj.getId()) {
                    byte[] novoB = novo_obj.toByteArray();
                    short novoTamanho = (short) novoB.length;

                    // Verifica se o novo tamanho é menor ou igual ao tamanho original
                    if (novoTamanho <= tamanho) {
                        arquivo.seek(pie.getEndereco() + 3);  // Atualiza o registro no mesmo local
                        arquivo.write(novoB);
                    } else {
                        // Marca o registro antigo como excluído e grava o novo no final do arquivo
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        arquivo.seek(arquivo.length());
                        long novo_endereco = arquivo.getFilePointer();

                        arquivo.writeByte(' ');
                        arquivo.writeShort(novoTamanho);
                        arquivo.write(novoB);
                        indiceDireto.update(new ParIDEndereco(novo_obj.getId(), novo_endereco));
                    }

                    return true;  // Retorna true indicando que o registro foi atualizado
                }
            }
        }

        return false;  // Retorna false caso o registro não seja encontrado ou não seja atualizado
    }

    // Método para fechar o arquivo e o índice
    public void close() throws Exception {
        arquivo.close();
        indiceDireto.close();
    }
}