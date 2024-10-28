package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class HashExtensivel<T extends RegistroHashExtensivel<T>> {

    String nomeArquivoDiretorio;  // Nome do arquivo que contém o diretório
    String nomeArquivoCestos;  // Nome do arquivo que contém os cestos
    RandomAccessFile arqDiretorio;  // Arquivo de diretório
    RandomAccessFile arqCestos;  // Arquivo de cestos
    int quantidadeDadosPorCesto;  // Quantidade de dados armazenados por cesto
    Diretorio diretorio;  // Estrutura de diretório
    Constructor<T> construtor;  // Construtor para instanciar objetos do tipo T

    public class Cesto {

        Constructor<T> construtor;
        short quantidadeMaxima;  // Quantidade máxima de elementos que o cesto pode conter
        short bytesPorElemento;  // Quantidade de bytes necessária para armazenar um elemento
        short bytesPorCesto;  // Quantidade de bytes necessária para armazenar todo o cesto

        byte profundidadeLocal;  // Profundidade local do cesto
        short quantidade;  // Quantidade atual de elementos no cesto
        ArrayList<T> elementos;  // Lista de elementos armazenados no cesto

        // Construtor do cesto
        public Cesto(Constructor<T> ct, int qtdmax) throws Exception {
            this(ct, qtdmax, 0);
        }

        public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
            construtor = ct;
            if (qtdmax > 32767) {
                throw new Exception("Quantidade máxima de 32.767 elementos");
            }
            if (pl > 127) {
                throw new Exception("Profundidade local máxima de 127 bits");
            }
            profundidadeLocal = (byte) pl;
            quantidade = 0;
            quantidadeMaxima = (short) qtdmax;
            elementos = new ArrayList<>(quantidadeMaxima);
            bytesPorElemento = ct.newInstance().size();
            bytesPorCesto = (short) (bytesPorElemento * quantidadeMaxima + 3);
        }

        // Serializa o cesto para um array de bytes
        public byte[] toByteArray() throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeLocal);
            dos.writeShort(quantidade);
            int i = 0;
            while (i < quantidade) {
                dos.write(elementos.get(i).toByteArray());
                i++;
            }
            byte[] vazio = new byte[bytesPorElemento];
            while (i < quantidadeMaxima) {
                dos.write(vazio);
                i++;
            }
            return baos.toByteArray();
        }

        // Deserializa um array de bytes para o cesto
        public void fromByteArray(byte[] ba) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            quantidade = dis.readShort();
            int i = 0;
            elementos = new ArrayList<>(quantidadeMaxima);
            byte[] dados = new byte[bytesPorElemento];
            T elem;
            while (i < quantidadeMaxima) {
                dis.read(dados);
                elem = construtor.newInstance();
                elem.fromByteArray(dados);
                elementos.add(elem);
                i++;
            }
        }

        // Insere um novo elemento no cesto
        public boolean create(T elem) {
            if (full()) {
                return false;
            }
            int i = quantidade - 1;
            while (i >= 0 && elem.hashCode() < elementos.get(i).hashCode()) {
                i--;
            }
            elementos.add(i + 1, elem);
            quantidade++;
            return true;
        }

        // Lê um elemento do cesto a partir da chave
        public T read(int chave) {
            if (empty()) {
                return null;
            }
            int i = 0;
            while (i < quantidade && chave > elementos.get(i).hashCode()) {
                i++;
            }
            if (i < quantidade && chave == elementos.get(i).hashCode()) {
                return elementos.get(i); 
            } else {
                return null;
            }
        }

        // Atualiza um elemento no cesto
        public boolean update(T elem) {
            if (empty()) {
                return false;
            }
            int i = 0;
            while (i < quantidade && elem.hashCode() > elementos.get(i).hashCode()) {
                i++;
            }
            if (i < quantidade && elem.hashCode() == elementos.get(i).hashCode()) {
                elementos.set(i, elem);
                return true;
            } else {
                return false;
            }
        }

        // Exclui um elemento do cesto a partir da chave
        public boolean delete(int chave) {
            if (empty()) {
                return false;
            }
            int i = 0;
            while (i < quantidade && chave > elementos.get(i).hashCode()) {
                i++;
            }
            if (chave == elementos.get(i).hashCode()) {
                elementos.remove(i);
                quantidade--;
                return true;
            } else {
                return false;
            }
        }

        // Verifica se o cesto está vazio
        public boolean empty() {
            return quantidade == 0;
        }

        // Verifica se o cesto está cheio
        public boolean full() {
            return quantidade == quantidadeMaxima;
        }

        // Retorna uma representação em String do cesto
        public String toString() {
            String s = "Profundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
            int i = 0;
            while (i < quantidade) {
                s += elementos.get(i).toString() + " | ";
                i++;
            }
            while (i < quantidadeMaxima) {
                s += "- | ";
                i++;
            }
            return s;
        }

        // Retorna o tamanho do cesto em bytes
        public int size() {
            return bytesPorCesto;
        }
    }

    protected class Diretorio {

        byte profundidadeGlobal;  // Profundidade global do diretório
        long[] enderecos;  // Vetor de endereços dos cestos

        // Construtor do diretório
        public Diretorio() {
            profundidadeGlobal = 0;
            enderecos = new long[1];
            enderecos[0] = 0;
        }

        // Atualiza o endereço de um cesto no diretório
        public boolean atualizaEndereco(int p, long e) {
            if (p > Math.pow(2, profundidadeGlobal)) {
                return false;
            }
            enderecos[p] = e;
            return true;
        }

        // Serializa o diretório para um array de bytes
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            int i = 0;
            while (i < quantidade) {
                dos.writeLong(enderecos[i]);
                i++;
            }
            return baos.toByteArray();
        }

        // Deserializa um array de bytes para o diretório
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            enderecos = new long[quantidade];
            int i = 0;
            while (i < quantidade) {
                enderecos[i] = dis.readLong();
                i++;
            }
        }

        // Retorna uma representação em String do diretório
        public String toString() {
            String s = "\nProfundidade global: " + profundidadeGlobal;
            int i = 0;
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            while (i < quantidade) {
                s += "\n" + i + ": " + enderecos[i];
                i++;
            }
            return s;
        }

        // Retorna o endereço de um cesto a partir de um índice
        protected long endereco(int p) {
            if (p > Math.pow(2, profundidadeGlobal)) {
                return -1;
            }
            return enderecos[p];
        }

        // Duplica o diretório quando necessário
        protected boolean duplica() {
            if (profundidadeGlobal == 127) {
                return false;
            }
            profundidadeGlobal++;
            int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
            int q2 = (int) Math.pow(2, profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            int i = 0;
            while (i < q1) {
                novosEnderecos[i] = enderecos[i];
                i++;
            }
            while (i < q2) {
                novosEnderecos[i] = enderecos[i - q1];
                i++;
            }
            enderecos = novosEnderecos;
            return true;
        }

        // Calcula o hash para determinar o índice do diretório
        protected int hash(int chave) {
            return Math.abs(chave) % (int) Math.pow(2, profundidadeGlobal);
        }

        // Calcula o hash para um cesto específico
        protected int hash2(int chave, int pl) {
            return Math.abs(chave) % (int) Math.pow(2, pl);
        }
    }

    // Construtor da classe HashExtensivel
    public HashExtensivel(Constructor<T> ct, int n, String nd, String nc) throws Exception {
        construtor = ct;
        quantidadeDadosPorCesto = n;
        nomeArquivoDiretorio = nd;
        nomeArquivoCestos = nc;

        arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
        arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

        if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {
            diretorio = new Diretorio();
            byte[] bd = diretorio.toByteArray();
            arqDiretorio.write(bd);

            Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
            bd = c.toByteArray();
            arqCestos.seek(0);
            arqCestos.write(bd);
        }
    }

    // Cria um novo elemento na tabela hash
    public boolean create(T elem) throws Exception {
        byte[] bd = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);

        int i = diretorio.hash(elem.hashCode());

        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);

        if (c.read(elem.hashCode()) != null) {
            throw new Exception("Elemento já existe");
        }

        if (!c.full()) {
            c.create(elem);
            arqCestos.seek(enderecoCesto);
            arqCestos.write(c.toByteArray());
            return true;
        }

        byte pl = c.profundidadeLocal;
        if (pl >= diretorio.profundidadeGlobal) {
            diretorio.duplica();
        }
        byte pg = diretorio.profundidadeGlobal;

        Cesto c1 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c1.toByteArray());

        Cesto c2 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
        long novoEndereco = arqCestos.length();
        arqCestos.seek(novoEndereco);
        arqCestos.write(c2.toByteArray());

        int inicio = diretorio.hash2(elem.hashCode(), c.profundidadeLocal);
        int deslocamento = (int) Math.pow(2, pl);
        int max = (int) Math.pow(2, pg);
        boolean troca = false;
        for (int j = inicio; j < max; j += deslocamento) {
            if (troca) {
                diretorio.atualizaEndereco(j, novoEndereco);
            }
            troca = !troca;
        }

        bd = diretorio.toByteArray();
        arqDiretorio.seek(0);
        arqDiretorio.write(bd);

        for (int j = 0; j < c.quantidade; j++) {
            create(c.elementos.get(j));
        }
        create(elem);
        return true;
    }

    // Lê um elemento da tabela hash a partir da chave
    public T read(int chave) throws Exception {
        byte[] bd = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);

        int i = diretorio.hash(chave);

        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);

        return c.read(chave);
    }

    // Atualiza um elemento na tabela hash
    public boolean update(T elem) throws Exception {
        byte[] bd = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);

        int i = diretorio.hash(elem.hashCode());

        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);

        if (!c.update(elem)) {
            return false;
        }

        arqCestos.seek(enderecoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }

    // Exclui um elemento da tabela hash a partir da chave
    public boolean delete(int chave) throws Exception {
        byte[] bd = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);

        int i = diretorio.hash(chave);

        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);

        if (!c.delete(chave)) {
            return false;
        }

        arqCestos.seek(enderecoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }

    // Imprime o estado atual do diretório e dos cestos
    public void print() {
        try {
            byte[] bd = new byte[(int) arqDiretorio.length()];
            arqDiretorio.seek(0);
            arqDiretorio.read(bd);
            diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            System.out.println("\nDIRETÓRIO ------------------");
            System.out.println(diretorio);

            System.out.println("\nCESTOS ---------------------");
            arqCestos.seek(0);
            while (arqCestos.getFilePointer() != arqCestos.length()) {
                System.out.println("Endereço: " + arqCestos.getFilePointer());
                Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
                byte[] ba = new byte[c.size()];
                arqCestos.read(ba);
                c.fromByteArray(ba);
                System.out.println(c + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fecha os arquivos de diretório e cestos
    public void close() throws Exception {
        arqDiretorio.close();
        arqCestos.close();
    }
}
