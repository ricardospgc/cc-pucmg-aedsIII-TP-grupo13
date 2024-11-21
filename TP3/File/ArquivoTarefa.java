package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoTarefa extends Arquivo<Tarefa> {
    Arquivo<Tarefa> arq_tarefa;  // Referência para o arquivo de tarefas
    ArvoreBMais<ParIdId> indice_indireto_id;  // Índice indireto para acessar tarefas por ID da categoria

    // Construtor da classe ArquivoTarefa
    public ArquivoTarefa() throws Exception {
        super("Tarefas", Tarefa.class.getConstructor());
        // Inicializa o índice indireto utilizando uma Árvore B+
        indice_indireto_id = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "./BaseDeDados/indice_indireto_id.btree.db");
    }

    // Sobrescreve o método create para incluir a inserção no índice indireto
    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        indice_indireto_id.create(new ParIdId(c.getIdCategoria(), id));  // Atualiza o índice indireto
        return id;
    }

    // Método para ler todas as tarefas de uma categoria pelo ID da categoria
    public ArrayList<Tarefa> readAll(int id) throws Exception {
        // Busca no índice indireto a lista de pares com o ID da categoria fornecido
        ArrayList<ParIdId> p = indice_indireto_id.read(new ParIdId(id, -1));
        ArrayList<Tarefa> t = new ArrayList<>();
        Arquivo<Tarefa> arq = new Arquivo<>("Tarefas", Tarefa.class.getConstructor());

        if (p != null && !p.isEmpty()) {
            for (ParIdId p_aux : p) {
                Tarefa tarefa = (Tarefa) arq.read(p_aux.getId2());
                if (tarefa != null) {
                    t.add(tarefa);  // Adiciona a tarefa à lista
                }
            }
        }
        return t;  // Retorna a lista de tarefas
    }

    // Sobrescreve o método delete para excluir também do índice indireto
    public boolean delete(int id) throws Exception {
        boolean result = false;
        Tarefa obj = super.read(id);
        if (obj != null) {
            // Exclui do índice indireto e marca o registro como excluído no arquivo
            if (indice_indireto_id.delete(new ParIdId(obj.getIdCategoria(), obj.getId()))) {
                result = super.delete(obj.getId());
            }
        }
        return result;
    }

    // Sobrescreve o método update para atualizar também o índice indireto
    @Override
    public boolean update(Tarefa novaTarefa) throws Exception {
        Tarefa TarefaVelho = read(novaTarefa.getId());
        if (super.update(novaTarefa)) {
            if (novaTarefa.getId() == TarefaVelho.getId()) {
                // Atualiza o índice indireto, removendo o antigo e inserindo o novo
                indice_indireto_id.delete(new ParIdId(TarefaVelho.getIdCategoria(), TarefaVelho.getId()));
                indice_indireto_id.create(new ParIdId(novaTarefa.getIdCategoria(), novaTarefa.getId()));
            }
            return true;
        }
        return false;
    }
}
