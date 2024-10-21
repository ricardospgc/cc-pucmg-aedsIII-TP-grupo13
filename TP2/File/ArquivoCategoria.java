package File;

import Entidades.Categoria;
import java.util.ArrayList;
import java.util.List;


public class ArquivoCategoria extends Arquivo<Categoria> {
    ArvoreBMais<ParNomeId> indiceIndiretoNome;
    Arquivo<Categoria> arqTarefa;

    public ArquivoCategoria() throws Exception {
        super(Categoria.class.getConstructor(),"Categorias.db");
        indiceIndiretoNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "BaseDeDados//IndiceIndiretoNome.btree.db");
    } 

    @Override
    public int create(Categoria obj) throws Exception {
        int id = super.create(obj);
        indiceIndiretoNome.create(new ParNomeId(obj.getNome(), id));
        System.out.println(obj.getNome());
        return id;
    } 

    public Categoria read(String nome) throws Exception {
        ArrayList<ParNomeId> picn = indiceIndiretoNome.read(new ParNomeId(nome, -1));
        return super.read(picn.get(0).getId());
    } 
    
    
    public boolean delete(int nome) throws Exception {
        boolean result = false;
        Categoria obj = super.read(nome);
        if(obj != null) {
            if(indiceIndiretoNome.delete(new ParNomeId(obj.getNome(), obj.getId()))) {
                result = super.delete(obj.getId());
            } 
        } 
        return result;
    }

    @Override
    public boolean update(Categoria novaCategoria) throws Exception {
        boolean result = false;
        Categoria categoriaAntiga = super.read(novaCategoria.getId());
        if(super.update(novaCategoria)) {
            if(novaCategoria.getNome().compareTo(categoriaAntiga.getNome()) != 0) {
                if( indiceIndiretoNome.delete(new ParNomeId(categoriaAntiga.getNome(), categoriaAntiga.getId())) ) {
                    indiceIndiretoNome.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId()));
                } 
                result = true;
            } 
        } 
        return result;
    } 

    public List<Categoria> leTodasCategorias() throws Exception {
        List<Categoria> categorias = new ArrayList<>();

        arquivo.seek(CABECALHO); 
        byte lapide = ' ';
        short tam = 0;
        byte[] b = null;
        Categoria c = null;

        while( arquivo.getFilePointer() < arquivo.length() ) {
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            b = new byte[tam];
            arquivo.read(b);
            if(lapide != '*') {
                c = new Categoria();
                c.fromByteArray(b);
                categorias.add(c);
            }
        } 
        return categorias;  
    }

    public boolean buscarCategoriaNome(String nomeCategoria) throws Exception {
        arquivo.seek(CABECALHO); 
        byte lapide = ' ';
        short tam = 0;
        byte[] b = null;
        Categoria c = null;

        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            b = new byte[tam];
            arquivo.read(b);
            if (lapide != '*') {
                c = new Categoria();
                c.fromByteArray(b);
                if (c.getNome().equals(nomeCategoria)) {
                    return true;
                }
            }
        }
        return false;
    }



} 