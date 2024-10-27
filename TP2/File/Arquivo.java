package File;

import Interface.Registro;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {
    final int TAM_CABECALHO = 4;
    RandomAccessFile arquivo;
    String nomeArquivo;
    Constructor<T> construtor;
    HashExtensivel<ParIDEndereco> indiceDireto;

    public Arquivo(String na, Constructor<T> c) throws Exception {
        File d = new File(".\\BaseDeDados");
        if (!d.exists())
            d.mkdir();

        this.nomeArquivo = ".\\BaseDeDados\\" + na + ".db";
        this.construtor = c;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");

        if (arquivo.length() < TAM_CABECALHO)
            arquivo.writeInt(0);

        indiceDireto = new HashExtensivel<>(
                ParIDEndereco.class.getConstructor(),
                4,
                ".\\BaseDeDados\\" + na + ".hash_d.db",
                ".\\BaseDeDados\\" + na + ".hash_c.db");
    }

    public int create(T obj) throws Exception {
        arquivo.seek(0);
        int proximoId = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(proximoId);
        obj.setId(proximoId);

        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();

        byte[] b = obj.toByteArray();
        arquivo.writeByte(' ');
        arquivo.writeShort(b.length);
        arquivo.write(b);

        indiceDireto.create(new ParIDEndereco(proximoId, endereco));

        return obj.getId();
    }

    public T read(int id) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        ParIDEndereco pid = indiceDireto.read(id);
        if (pid != null) {
            arquivo.seek(pid.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == id)
                    return obj;
            }
        }

        return null;
    }

    public boolean delete(int id) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        ParIDEndereco pie = indiceDireto.read(id);
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == id) {
                    if (indiceDireto.delete(id)) {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean update(T novo_obj) throws Exception {
        T obj;
        short tamanho;
        byte[] b;
        byte lapide;

        ParIDEndereco pie = indiceDireto.read(novo_obj.getId());
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            if (lapide == ' ') {
                tamanho = arquivo.readShort();
                b = new byte[tamanho];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getId() == novo_obj.getId()) {
                    byte[] novoB = novo_obj.toByteArray();
                    short novoTamanho = (short) novoB.length;

                    if (novoTamanho <= tamanho) {
                        arquivo.seek(pie.getEndereco() + 3);
                        arquivo.write(novoB);
                    } else {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*');
                        arquivo.seek(arquivo.length());
                        long novo_endereco = arquivo.getFilePointer();

                        arquivo.writeByte(' ');
                        arquivo.writeShort(novoTamanho);
                        arquivo.write(novoB);
                        indiceDireto.update(new ParIDEndereco(novo_obj.getId(), novo_endereco));
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void close() throws Exception {
        arquivo.close();
        indiceDireto.close();
    }
}
