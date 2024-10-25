package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoTarefa extends Arquivo<Tarefa> {
    Arquivo<Tarefa> arq_tarefa;
    ArvoreBMais<ParIdId> indice_indireto_id;

    public ArquivoTarefa() throws Exception {
        super("tarefas", Tarefa.class.getConstructor());
        indice_indireto_id = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "./dados/indice_indireto_id.btree.db");
    }

    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        indice_indireto_id.create(new ParIdId(c.getIdCategoria(), id));
        return id;
    }

    public ArrayList<Tarefa> readAll(int id) throws Exception {
        ArrayList<ParIdId> p = indice_indireto_id.read(new ParIdId(id, -1));
        ArrayList<Tarefa> t = new ArrayList<>();
        Arquivo<Tarefa> arq = new Arquivo<>("tarefas", Tarefa.class.getConstructor());

        if (p != null && !p.isEmpty()) {
            for (ParIdId p_aux : p) {
                Tarefa tarefa = (Tarefa) arq.read(p_aux.getId2());
                if (tarefa != null) {
                    t.add(tarefa);
                }
            }
        }
        return t;
    }

    public boolean delete(int id) throws Exception {
        boolean result = false;
        Tarefa obj = super.read(id);
        if (obj != null) {
            if (indice_indireto_id.delete(new ParIdId(obj.getIdCategoria(), obj.getId()))) {
                result = super.delete(obj.getId());
            }
        }
        return result;
    }

    @Override
    public boolean update(Tarefa novaTarefa) throws Exception {
        Tarefa TarefaVelho = read(novaTarefa.getId());
        if (super.update(novaTarefa)) {
            if (novaTarefa.getId() == TarefaVelho.getId()) {
                indice_indireto_id.delete(new ParIdId(TarefaVelho.getIdCategoria(), TarefaVelho.getId()));
                indice_indireto_id.create(new ParIdId(novaTarefa.getIdCategoria(), novaTarefa.getId()));
            }
            return true;
        }
        return false;
    }
}
