package File;

import Entidades.*;
import IndiceInvertido.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArquivoTarefa extends Arquivo<Tarefa> {
    ArvoreBMais<ParIdId> indice_indireto_id;  // Índice indireto para acessar tarefas por ID da categoria
    ArvoreBMais<ParIDRotulocID> arvoreTarefasRotulos;
    StopWords stopWords;
    // Construtor da classe ArquivoTarefa
    public ArquivoTarefa() throws Exception {
        super("Tarefas", Tarefa.class.getConstructor());
        // Inicializa o índice indireto utilizando uma Árvore B+
        indice_indireto_id = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "./BaseDeDados/indice_indireto_id.btree.db");
        arvoreTarefasRotulos = new ArvoreBMais<>(ParIDRotulocID.class.getConstructor(), 5,"./BaseDeDados/ArvoreTarefasRotulos.btree.db");
        stopWords = new StopWords();
    }

    // Sobrescreve o método create para incluir a inserção no índice indireto
    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        c.setId(id);
        indice_indireto_id.create(new ParIdId(c.getIdCategoria(), id));  // Atualiza o índice indireto
        ArrayList<Integer> idRotulos = c.getIdRotulos();
        for(int i=0; i<idRotulos.size(); i++){
            arvoreTarefasRotulos.create(new ParIDRotulocID(idRotulos.get(i), id));
        }
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

    // Lê tarefas associadas a um rótulo específico
    public ArrayList<Tarefa> read(ParRotuloId parRotuloId) throws Exception {
        ArrayList<Tarefa> t = new ArrayList<>();
        ArrayList<ParIDRotulocID> id = arvoreTarefasRotulos.read(new ParIDRotulocID(parRotuloId.getId())); // Busca associações de rótulos
        for(int i = 0; i < id.size(); i++) {
            t.add(super.read(id.get(i).getidRotulo())); // Adiciona as tarefas à lista
        }
        return t;
    }

    // Sobrescreve o método delete para excluir também do índice indireto
    public boolean delete(Tarefa tarefa) {
        boolean result = false;
        try {
            result = super.delete(tarefa.getId()) ? indice_indireto_id.delete(new ParIdId(tarefa.getIdCategoria(), tarefa.getId())) : false;
            String[] chaves = stopWords.stopWordsCheck(tarefa.getNome()); // Obtém palavras associadas à tarefa
            ArrayList<Integer> idRotulos = tarefa.getIdRotulos(); // Obtém os IDs dos rótulos
            for(int i = 0; i < idRotulos.size(); i++) {
                arvoreTarefasRotulos.delete(new ParIDRotulocID(idRotulos.get(i), tarefa.getId())); // Remove as associações de rótulos
            }
            for(int i = 0; i < chaves.length; i++) {
                chaves[i] = chaves[i].toLowerCase();
                stopWords.lista.delete(chaves[i], tarefa.getId()); // Remove palavras associadas
            }
            stopWords.lista.decrementaEntidades(); // Atualiza contadores no gerenciador de stopwords
        } catch(Exception e) {
            System.out.println(e.getMessage()); // Tratamento de erro
        }
        return result;
    }

    // Atualiza uma tarefa e ajusta as associações no gerenciador de stopwords
    public boolean update(Tarefa tarefa, Tarefa update) {
        boolean result = false;
        update.setId(tarefa.getId()); // Mantém o ID original da tarefa

        try {
            String[] chaves = stopWords.stopWordsCheck(tarefa.getNome()); // Obtém palavras da tarefa antiga
            for(int i = 0; i < chaves.length; i++) {
                chaves[i] = chaves[i].toLowerCase();
                stopWords.lista.delete(chaves[i], tarefa.getId()); // Remove palavras da tarefa antiga
            }
            stopWords.inserir(update.getNome(), update.getId()); // Insere palavras da nova tarefa
            result = super.update(update); // Atualiza a tarefa no arquivo
        } catch(Exception e) {
            System.out.println(e.getMessage()); // Tratamento de erro
        }
        return result;
    }

    // Lista tarefas baseadas em palavras-chave, utilizando stopwords
    public ArrayList<Tarefa> listar(String titulo) throws Exception {
        ArrayList<ElementoLista> elementos = new ArrayList<>();
        String[] chaves = stopWords.stopWordsCheck(titulo); // Divide o título em palavras-chave
        for(int i = 0; i < chaves.length; i++) {
            if(!chaves[i].isEmpty()) {
                try {
                    ElementoLista[] elementoEncontrados = stopWords.lista.read(chaves[i]); // Busca elementos na lista invertida
                    if(elementoEncontrados != null) {
                        for(ElementoLista elemento : elementoEncontrados) {
                            float frequencia = elemento.getFrequencia(); // Calcula a relevância da tarefa
                            float idf = stopWords.lista.numeroEntidades() / (float) elementoEncontrados.length;
                            ElementoLista elementoAux = new ElementoLista(elemento.getId(), frequencia * idf);
                            boolean existe = false;
                            for(ElementoLista e : elementos) {
                                if(e.getId() == elementoAux.getId()) {
                                    e.setFrequencia(e.getFrequencia() + elementoAux.getFrequencia());
                                    existe = true;
                                    break;
                                }
                            }
                            if(!existe) {
                                elementos.add(elementoAux);
                            }
                        }
                    }
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // Ordena os elementos pela relevância em ordem decrescente
        Collections.sort(elementos, new Comparator<ElementoLista>() {
            @Override
            public int compare(ElementoLista e1, ElementoLista e2) {
                return Float.compare(e2.getFrequencia(), e1.getFrequencia());
            }
        });

        ArrayList<Tarefa> tarefas = new ArrayList<>();
        for(ElementoLista elemento : elementos) {
            tarefas.add(super.read(elemento.getId())); // Adiciona as tarefas à lista
        }

        return tarefas;
    }

     // Atualiza os rótulos associados a uma tarefa
     public boolean updateRotulos(Tarefa tarefa, ArrayList<Integer> removed, ArrayList<Integer> added) {
        boolean result = false;
        try {
            ArrayList<Integer> idRotulo = tarefa.getIdRotulos(); // Obtém os rótulos atuais da tarefa
            for(Integer removeId : removed) {
                if(idRotulo.contains(removeId)) {
                    arvoreTarefasRotulos.delete(new ParIDRotulocID(removeId, tarefa.getId())); // Remove rótulo do índice
                    idRotulo.remove(removeId);
                } else {
                    System.out.println("Rótulo não encontrado");
                }
            }
            for(Integer addId : added) {
                if(!idRotulo.contains(addId)) {
                    idRotulo.add(addId); // Adiciona o novo rótulo
                    arvoreTarefasRotulos.create(new ParIDRotulocID(addId, tarefa.getId())); // Atualiza o índice
                } else {
                    System.out.println("Rótulo já existente");
                }
            }
            tarefa.setIdRotulos(idRotulo); // Atualiza os rótulos da tarefa
            result = super.update(tarefa); // Persiste as alterações no arquivo
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}