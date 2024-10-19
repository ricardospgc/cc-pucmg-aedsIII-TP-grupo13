package File;

import Entidades.Categoria;
import java.util.ArrayList;


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
            if(novaCategoria.getNome() != categoriaAntiga.getNome()) {
                if( indiceIndiretoNome.delete(new ParNomeId(categoriaAntiga.getNome(), categoriaAntiga.getId())) ) {
                    indiceIndiretoNome.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId()));
                } 
                result = true;
            } 
        } 
        return result;
    } 

} 