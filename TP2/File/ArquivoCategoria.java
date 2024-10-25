package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoCategoria extends Arquivo<Categoria> {

    Arquivo<Categoria> arqCategoria;
    ArvoreBMais<ParNomeId> indiceIndiretoNome;

    public ArquivoCategoria() throws Exception {
        super("categorias", Categoria.class.getConstructor());
        indiceIndiretoNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/indiceIndiretoNome.btree.db");
    }

    @Override
    public int create(Categoria c) throws Exception {
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeId(c.getNome(), id));
        return id;
    }

    public Categoria read(String n) throws Exception {
        ArrayList<ParNomeId> p = indiceIndiretoNome.read(new ParNomeId(n, -1));
        return super.read(p.get(0).getId());
    }

    public boolean delete(String n) throws Exception {
        return delete(read(n).getId());
    }

    public boolean delete(int id) throws Exception {
        boolean result = false;
        Categoria obj = super.read(id);
        if (obj != null) {
            if (indiceIndiretoNome.delete(new ParNomeId(obj.getNome(), obj.getId()))) {
                result = super.delete(obj.getId());
            }
        }
        return result;
    }

    public void list() {
        try {
            System.out.println();
            indiceIndiretoNome.show();
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    @Override
    public boolean update(Categoria novaCategoria) throws Exception {
        Categoria categoriaVelha = read(novaCategoria.getId());
        if (super.update(novaCategoria)) {
            if (novaCategoria.getId() == categoriaVelha.getId()) {
                indiceIndiretoNome.delete(new ParNomeId(categoriaVelha.getNome(), categoriaVelha.getId()));
                indiceIndiretoNome.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId()));
            }
            return true;
        }
        return false;
    }
}
