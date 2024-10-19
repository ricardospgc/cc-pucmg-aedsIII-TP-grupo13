package File;

import Entidades.Tarefa;
import java.util.ArrayList;

public class ArquivoTarefa extends Arquivo<Tarefa> {
    Arquivo<Tarefa> arqTarefa;
    ArvoreBMais<ParIdId> indiceIndiretoIdCategoria;

    public ArquivoTarefa() throws Exception {
        super(Tarefa.class.getConstructor(), "Tarefas.db");
        indiceIndiretoIdCategoria = new ArvoreBMais<>(
            ParIdId.class.getConstructor(), 
            4, 
            "BaseDeDados//IndiceIndiretoId.btree.db"
        );
    }

    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        indiceIndiretoIdCategoria.create(new ParIdId(c.getId(), id));
        return id;
    }

    public Tarefa read(int id) throws Exception {
        ArrayList<ParIdId> p = indiceIndiretoIdCategoria.read(new ParIdId(id, -1));
        return super.read(p.get(0).getIDTarefa());
    }
    
    public boolean delete(int id) throws Exception {
        boolean result = false;
        Tarefa obj = super.read(id);
        if(obj != null) {
            if(indiceIndiretoIdCategoria.delete(new ParIdId(obj.getIdCategoria(), obj.getId()))) {
                result = super.delete(obj.getId());
            } 
        } 
        return result;
    }

    @Override
    public boolean update(Tarefa novaTarefa) throws Exception {
        Tarefa TarefaVelho = read(novaTarefa.getId());
        if(super.update(novaTarefa)) {
            if(novaTarefa.getId() == TarefaVelho.getId()) {
                indiceIndiretoIdCategoria.delete(new ParIdId(TarefaVelho.getIdCategoria(), TarefaVelho.getId()));
                indiceIndiretoIdCategoria.create(new ParIdId(novaTarefa.getIdCategoria(), novaTarefa.getId()));
            }
            return true;
        }
        return false;
    } 
}
