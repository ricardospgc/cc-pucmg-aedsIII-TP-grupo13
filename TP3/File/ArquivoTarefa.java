package File;

import Entidades.*;
import IndiceInvertido.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    ArvoreBMais<ParIdId> indiceCategoriaParaTarefa;
    /* Índice para associar IDs de categorias com IDs de tarefas */
    ArvoreBMais<ParIDRotulocID> indiceRotuloParaTarefa;
    /* Índice para associar IDs de rótulos com IDs de tarefas */
    StopWords gerenciadorStopWords;

    /* Gerenciador de palavras-chave para busca */

 /*
        Construtor da classe ArquivoTarefa
     */
    public ArquivoTarefa() throws Exception {
        super("Tarefas", Tarefa.class.getConstructor());
        indiceCategoriaParaTarefa = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                5,
                "./BaseDeDados/indiceCategoriaParaTarefa.btree.db"
        );
        indiceRotuloParaTarefa = new ArvoreBMais<>(
                ParIDRotulocID.class.getConstructor(),
                5,
                "./BaseDeDados/indiceRotuloParaTarefa.btree.db"
        );
        gerenciadorStopWords = new StopWords();
    }

    /*
        Cria uma nova tarefa e atualiza os índices
     */
    @Override
    public int create(Tarefa tarefa) throws Exception {
        int id = super.create(tarefa);
        /* Cria a tarefa no arquivo base e retorna o ID */
        tarefa.setId(id);
        indiceCategoriaParaTarefa.create(new ParIdId(tarefa.getIdCategoria(), id));
        /* Adiciona ao índice de categorias */
        ArrayList<Integer> idsRotulos = tarefa.getIdRotulos();
        for (int i = 0; i < idsRotulos.size(); i++) {
            indiceRotuloParaTarefa.create(new ParIDRotulocID(idsRotulos.get(i), id));
            /* Atualiza o índice de rótulos */
        }
        gerenciadorStopWords.inserirTitulo(tarefa.getNome(), id);
        /* Adiciona palavras-chave da tarefa no gerenciador */
        return id;
    }

    /* 
        Lê todas as tarefas associadas a uma categoria pelo ID da categoria 
     */
    public ArrayList<Tarefa> readAll(int idCategoria) throws Exception {
        ArrayList<ParIdId> paresCategoriaTarefa = indiceCategoriaParaTarefa.read(new ParIdId(idCategoria, -1));
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        Arquivo<Tarefa> arquivoBase = new Arquivo<>("Tarefas", Tarefa.class.getConstructor());

        if (paresCategoriaTarefa != null && !paresCategoriaTarefa.isEmpty()) {
            for (ParIdId par : paresCategoriaTarefa) {
                Tarefa tarefa = arquivoBase.read(par.getId2());
                /* Lê cada tarefa associada */
                if (tarefa != null) {
                    tarefas.add(tarefa);
                    /* Adiciona a tarefa à lista */
                }
            }
        }
        return tarefas;
        /* Retorna a lista de tarefas */
    }

    /* 
        Lê todas as tarefas associadas a um rótulo específico 
     */
    public ArrayList<Tarefa> read(ParRotuloId parRotulo) throws Exception {
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        ArrayList<ParIDRotulocID> associacoesRotulo = indiceRotuloParaTarefa.read(new ParIDRotulocID(parRotulo.getId()));
        for (int i = 0; i < associacoesRotulo.size(); i++) {
            tarefas.add(super.read(associacoesRotulo.get(i).getidRotulo()));
            /* Adiciona tarefas correspondentes à lista */
        }
        return tarefas;
    }

    /* 
        Exclui uma tarefa e remove suas associações nos índices e gerenciador de palavras-chave 
     */
    public boolean delete(Tarefa tarefa) {
        boolean resultado = false;
        try {
            resultado = super.delete(tarefa.getId())
                    ? indiceCategoriaParaTarefa.delete(new ParIdId(tarefa.getIdCategoria(), tarefa.getId()))
                    : false;

            String[] palavrasChave = gerenciadorStopWords.processarStopWords(tarefa.getNome());
            /* Obtém palavras associadas */
            ArrayList<Integer> idsRotulos = tarefa.getIdRotulos();
            /* Obtém IDs dos rótulos associados */

            for (int idRotulo : idsRotulos) {
                indiceRotuloParaTarefa.delete(new ParIDRotulocID(idRotulo, tarefa.getId()));
                /* Remove associações de rótulos */
            }
            for (String palavra : palavrasChave) {
                palavra = palavra.toLowerCase();
                gerenciadorStopWords.listaInvertida.delete(palavra, tarefa.getId());
                /* Remove palavras do gerenciador */
            }
            gerenciadorStopWords.listaInvertida.decrementaEntidades();
            /* Atualiza contadores do gerenciador */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            /* Tratamento de erros */
        }
        return resultado;
    }

    /* 
        Atualiza uma tarefa e ajusta as palavras-chave no gerenciador 
    */
    public boolean update(Tarefa tarefaAntiga, Tarefa tarefaNova) {
        boolean resultado = false;
        tarefaNova.setId(tarefaAntiga.getId());
        /* Mantém o ID original */

        try {
            String[] palavrasAntigas = gerenciadorStopWords.processarStopWords(tarefaAntiga.getNome());
            /* Palavras da tarefa antiga */
            for (String palavra : palavrasAntigas) {
                palavra = palavra.toLowerCase();
                gerenciadorStopWords.listaInvertida.delete(palavra, tarefaAntiga.getId());
                /* Remove palavras antigas */
            }
            gerenciadorStopWords.inserirTitulo(tarefaNova.getNome(), tarefaNova.getId());
            /* Adiciona palavras da nova tarefa */
            resultado = super.update(tarefaNova);
            /* Atualiza a tarefa no arquivo */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            /* Tratamento de erros */
        }
        return resultado;
    }

    /*
        Lista tarefas baseando-se em palavras-chave
    */
    public ArrayList<Tarefa> listar(String palavraChave) throws Exception {
        ArrayList<ElementoLista> elementosRelevantes = new ArrayList<>();
        String[] palavras = gerenciadorStopWords.processarStopWords(palavraChave);
        /* Divide o título em palavras-chave */

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                try {
                    ElementoLista[] elementosEncontrados = gerenciadorStopWords.listaInvertida.read(palavra);
                    /* Busca na lista invertida */
                    if (elementosEncontrados != null) {
                        for (ElementoLista elemento : elementosEncontrados) {
                            float relevancia = elemento.getFrequencia();
                            float idf = gerenciadorStopWords.listaInvertida.numeroEntidades() / (float) elementosEncontrados.length;
                            ElementoLista novoElemento = new ElementoLista(elemento.getId(), relevancia * idf);

                            boolean jaExiste = false;
                            for (ElementoLista e : elementosRelevantes) {
                                if (e.getId() == novoElemento.getId()) {
                                    e.setFrequencia(e.getFrequencia() + novoElemento.getFrequencia());
                                    jaExiste = true;
                                    break;
                                }
                            }
                            if (!jaExiste) {
                                elementosRelevantes.add(novoElemento);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    /* Tratamento de erros */
                }
            }
        }

        /* Ordena os elementos pela relevância */
        Collections.sort(elementosRelevantes, new Comparator<ElementoLista>() {
            @Override
            public int compare(ElementoLista e1, ElementoLista e2) {
                return Float.compare(e2.getFrequencia(), e1.getFrequencia());
            }
        });

        ArrayList<Tarefa> tarefasOrdenadas = new ArrayList<>();
        for (ElementoLista elemento : elementosRelevantes) {
            tarefasOrdenadas.add(super.read(elemento.getId()));
            /* Adiciona as tarefas na lista final */
        }

        return tarefasOrdenadas;
    }

    /* 
        Atualiza os rótulos associados a uma tarefa 
    */
    public boolean updateRotulos(Tarefa tarefa, ArrayList<Integer> idsRemovidos, ArrayList<Integer> idsAdicionados) {
        boolean resultado = false;
        try {
            ArrayList<Integer> rotulosAtuais = tarefa.getIdRotulos();
            /* Obtém os rótulos atuais */

            for (Integer idRemovido : idsRemovidos) {
                if (rotulosAtuais.contains(idRemovido)) {
                    indiceRotuloParaTarefa.delete(new ParIDRotulocID(idRemovido, tarefa.getId()));
                    /* Remove rótulo do índice */
                    rotulosAtuais.remove(idRemovido);
                } else {
                    System.out.println("Rótulo não encontrado");
                }
            }

            for (Integer idAdicionado : idsAdicionados) {
                if (!rotulosAtuais.contains(idAdicionado)) {
                    rotulosAtuais.add(idAdicionado);
                    /* Adiciona o novo rótulo */
                    indiceRotuloParaTarefa.create(new ParIDRotulocID(idAdicionado, tarefa.getId()));
                    /* Atualiza o índice */
                } else {
                    System.out.println("Rótulo já existente");
                }
            }

            tarefa.setIdRotulos(rotulosAtuais);
            /* Atualiza os rótulos da tarefa */
            resultado = super.update(tarefa);
            /* Persiste as alterações */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            /* Tratamento de erros */
        }
        return resultado;
    }
}
