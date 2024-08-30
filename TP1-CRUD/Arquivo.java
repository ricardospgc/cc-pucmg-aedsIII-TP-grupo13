import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro>{
    
    final int CABECALHO = 4;
    Constructor<T> construtor;
    RandomAccessFile arquivo;
    String nomeArquivo;

    public Arquivo(Constructor<T> construtor, String nomeArquivo) throws IOException{
        File f = new File("BaseDeDados");
        if(!f.exists()){
            f.mkdir();
        }
        this.construtor = construtor;
        this.nomeArquivo = ".//BaseDeDados//"+nomeArquivo;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        if(arquivo.length() < CABECALHO){
            arquivo.seek(0);
            arquivo.writeInt(0);
        }
    }

    /* ----------------------------------------------------------------------------- CRUD */
    /* ------------------------------------------------------------ LAPIDE + TAMOBJ + OBJ */

    /**
     * <p>Este metodo escreve um objeto no arquivo, atualizando o ID do objeto e 
     * ajustando a posicao do ponteiro do arquivo conforme necessario. O metodo 
     * tambem aumenta o valor do ID armazenado no inicio do arquivo antes de 
     * gravar o objeto.</p>
     *
     * @param objeto O objeto a ser inserido no arquivo. 
     * @return O ID do objeto inserido.
     * @throws IOException Se ocorrer algum erro de I/O durante a gravacao do objeto no arquivo.
     */
    protected int create(T objeto) throws IOException{
        arquivo.seek(0);
        int proximoId = arquivo.readInt()+1;    
        arquivo.seek(0);
        arquivo.writeInt(proximoId);
        objeto.setId(proximoId);
        arquivo.seek(arquivo.length());

        byte[] b = objeto.toByteArray();
        arquivo.writeByte(' ');
        arquivo.writeShort(b.length);
        arquivo.write(b);

        return objeto.getId();
    }


    /**
     * Metodo responsavel por ler um objeto do arquivo com base no seu ID.
     * 
     * @param id O ID do objeto a ser lido.
     * @return O objeto correspondente ao ID, ou null se nao encontrado.
     * @throws IOException Se ocorrer um erro de I/O durante a leitura.
     */
    protected T read(int id) throws Exception{
        byte[] b;
        byte lapide;
        T objeto = null;
        short tamRegistro;
        boolean fim = false;
        arquivo.seek(CABECALHO);

        while(arquivo.getFilePointer()<arquivo.length() || !fim){
            lapide = arquivo.readByte();
            objeto = construtor.newInstance();
            tamRegistro = arquivo.readShort();
            b = new byte[tamRegistro];
            arquivo.read(b);

            if(lapide == ' '){
                
                objeto.fromByteArray(b);

                if(objeto.getId()==id) fim = true;
                else objeto = null;
            }
        } 

        return objeto;  
    }

    /**
     * Metodo responsavel por atualizar um objeto do arquivo com base no seu ID.
     * 
     * @param id O ID do objeto a ser atualizado.
     * @param T Objeto com os dados 
     * @return boolean indicando se foi ou nao atualizado.
     * @throws IOException Se ocorrer um erro de I/O durante a atualizacao.
     */
    protected boolean update(T novoObjeto) throws Exception{
        byte[] b;
        byte lapide;
        long endereco;
        T objeto;
        short tamRegistro;
        boolean fim = false;
        arquivo.seek(CABECALHO);

        /* quando o update atualizar o status de um objeto para concluido, 
        ele deve atualizar a data de conclusao pro momento do update */

        while(arquivo.getFilePointer() < arquivo.length() && !fim){
            objeto = construtor.newInstance();
            endereco = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            tamRegistro = arquivo.readShort();
            b = new byte[tamRegistro];
            arquivo.read(b);

            if(lapide == ' '){
                objeto.fromByteArray(b);
                
                if(objeto.getId() == novoObjeto.getId()){
                    byte[] bb = novoObjeto.toByteArray();
                    short tamNovoRegistro = (short) bb.length;

                    if(tamNovoRegistro <= tamRegistro){
                        arquivo.seek(endereco+3);
                        arquivo.write(bb);
                    }
                    else{
                        arquivo.seek(endereco);
                        arquivo.writeByte('*');

                        arquivo.seek(arquivo.length());
                        arquivo.writeByte(' ');
                        arquivo.writeShort(tamNovoRegistro);
                        arquivo.write(bb);
                    }
                    fim = true;
                }
            }   
        }
        return fim;
    }


    /**
     * Metodo responsavel por deletar um objeto do arquivo com base no seu ID.
     * 
     * @param id O ID do objeto a ser deletado.
     * @return boolean indicando se foi ou nao deletado.
     * @throws IOException Se ocorrer um erro de I/O durante o processo de delete.
     */
    protected boolean delete(int id) throws Exception{
        byte[] b;
        byte lapide;
        Long endereco;
        T objeto = null;
        boolean fim=false;
        short tamRegistro;
        arquivo.seek(CABECALHO);

        while(arquivo.getFilePointer()<arquivo.length() || !fim){
            endereco = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            objeto = construtor.newInstance();
            tamRegistro = arquivo.readShort();
            b = new byte[tamRegistro];
            arquivo.read(b);

            if(lapide==' '){
                objeto.fromByteArray(b);
                if(objeto.getId()==id){
                    arquivo.seek(endereco);
                    arquivo.writeByte('*');
                    fim = true; 
                }
            }
        }
        return fim;
    }

    /* ------------------------------------------------------------------------------------ */ 
}