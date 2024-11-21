package Main;

import Entidades.*;
import Enums.Prioridade;
import Enums.Status;
import File.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuTarefas {

    ArquivoTarefa arqTarefa;  // Arquivo para manipulação das tarefas
    ArquivoCategoria arqCategoria;  // Arquivo para manipulação das categorias
    private static Scanner sc = new Scanner(System.in);  // Scanner para leitura de entrada do usuário

    // Construtor da classe que inicializa os arquivos de categorias e tarefas
    public MenuTarefas() throws Exception {
        arqTarefa = new ArquivoTarefa();
        arqCategoria = new ArquivoCategoria();
    }

    // Método principal do menu de tarefas
    public void menu() {
        int opcao;
        do {
            System.out.println("\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Tarefas");
            System.out.println("1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar por categoria");
            System.out.println("0 - Voltar");

            System.out.print("Opcao: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
            } catch (NumberFormatException e) {
                opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
            }

            // Switch para tratar cada opção do menu
            switch (opcao) {
                case 1:
                    buscarTarefa();  // Chama o método para buscar uma tarefa
                    break;
                case 2:
                    incluirTarefa();  // Chama o método para incluir uma nova tarefa
                    break;
                case 3:
                    alterarTarefa();  // Chama o método para alterar uma tarefa existente
                    break;
                case 4:
                    excluirTarefa();  // Chama o método para excluir uma tarefa
                    break;
                case 5: // Chama o método para listar as tarefas por categoria
                    listarPorCategoria();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");  // Mensagem para opção inválida
                    break;
            }

        } while (opcao != 0);  // Repete o menu até que o usuário escolha sair
    }

    // Método para buscar uma tarefa por nome e categoria
    public void buscarTarefa() {
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa: ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);  // Busca a categoria pelo nome
            System.out.println("Nome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> t = arqTarefa.readAll(c.getId());  // Busca todas as tarefas relacionadas à categoria

            for (Tarefa tmp : t) {
                if (tmp.getNome().equals(nomeTarefa)) {  // Verifica se o nome da tarefa corresponde
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);  // Exibe a tarefa encontrada
                    return;
                }
            }
            System.out.println("Tarefa nao encontrada nesta categoria!");
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    // Método para incluir uma nova tarefa
    public void incluirTarefa() {
        String categoria;
        int idCategoria = -1;

        System.out.println("\nInclusao de tarefa");

        System.out.print("Nome da Tarefa: ");
        String nome = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Inserção do status
        listaStatus();
        byte statusB = Byte.parseByte(sc.nextLine());
        Status status = Status.fromByte(statusB);

        // Inserção da prioridade
        listaPrioridades();
        byte prioridadeB = Byte.parseByte(sc.nextLine());
        Prioridade prioridade = Prioridade.fromByte(prioridadeB);

        System.out.print("Data de Criação (dd/MM/yyyy) - 0 para data atual: ");
        String dc = sc.nextLine();
        LocalDate dataCriacao = LocalDate.now();
        if (!dc.equals("0")) { 
            dataCriacao = LocalDate.parse(dc, formatter);  // Define a data de criação conforme entrada do usuário
        }

        LocalDate dataConclusao = LocalDate.parse("01/01/1970", formatter);  // Data padrão para tarefas não concluídas
        if(status == Status.CONCLUIDO){
            System.out.print("Data de Conclusão (dd/MM/yyyy): ");
            String input = sc.nextLine();
            dataConclusao = LocalDate.parse(input, formatter);  // Define a data de conclusão caso a tarefa esteja concluída
        }

        // Inserção da categoria
        boolean catEscolhida = true;
        do {
            System.out.println("\nDigite o NOME DA CATEGORIA para a tarefa: ");
            try {
                catEscolhida = true;

                arqCategoria.list();  // Lista todas as categorias
                System.out.print("\n > ");
                categoria = sc.nextLine();
                
                idCategoria = arqCategoria.read(categoria).getId();  // Busca o ID da categoria pelo nome
            } catch (Exception e) {
                System.err.println("\nCategoria inválida! Tente novamente");
                catEscolhida = false;
            }
        } while (catEscolhida == false);

        try {
            Tarefa novaTarefa = new Tarefa(idCategoria, nome, dataCriacao, dataConclusao, status, prioridade);  // Cria uma nova tarefa
            arqTarefa.create(novaTarefa);  // Adiciona a tarefa ao arquivo
            System.out.println("Tarefa criada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro do sistema. Não foi possível criar a tarefa!");
        }
    }

    // Método para alterar uma tarefa existente
    public void alterarTarefa() {
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa a ser excluida: ");
        arqCategoria.list();
        System.out.print("\n > ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);  // Busca a categoria pelo nome
            System.out.println("\nNome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> t = arqTarefa.readAll(c.getId());  // Busca todas as tarefas relacionadas à categoria

            Tarefa tarefaVelha = new Tarefa();
            tarefaVelha.setId(-1);
            for (Tarefa tmp : t) {
                if (tmp.getNome().equals(nomeTarefa)) {  // Verifica se o nome da tarefa corresponde
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);  // Exibe a tarefa encontrada
                    tarefaVelha = tmp;
                    break;
                }
            }

            if (tarefaVelha.getId() != -1) {
                System.out.println("Novos dados da tarefa: ");
                System.out.print("Nome da Tarefa: ");
                String nome = sc.nextLine();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                System.out.println("\nData de Criacao (dd/MM/yyyy) - 0 para data atual");
                System.out.print(": ");
                String dc1 = sc.nextLine();
                LocalDate dataCriacao = dc1.isEmpty() ? null : LocalDate.parse(dc1, formatter);  // Define a nova data de criação

                System.out.print("Data de Conclusão (dd/MM/yyyy): ");
                String input = sc.nextLine();
                LocalDate dataConclusao = input.isEmpty() ? null : LocalDate.parse(input, formatter);  // Define a nova data de conclusão

                listaStatus();
                byte statusB = Byte.parseByte(sc.nextLine());
                Status status = Status.fromByte(statusB);  // Define o novo status da tarefa

                listaPrioridades();
                byte prioridadeB = Byte.parseByte(sc.nextLine());
                Prioridade prioridade = Prioridade.fromByte(prioridadeB);  // Define a nova prioridade da tarefa

                Tarefa novaTarefa = new Tarefa(tarefaVelha.getId(), tarefaVelha.getIdCategoria(), nome, dataCriacao, dataConclusao, status, prioridade);

                System.out.println("Confirma a alteracao da tarefa? (S/N) ");
                char resp = sc.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        if (arqTarefa.update(novaTarefa)) {
                            System.out.println("Tarefa alterada com sucesso.");
                        } else {
                            System.out.println("Falha ao alterar tarefa.");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro do sistema. Não foi possível alterar a tarefa!");
                    }
                }
            } else {
                System.out.println("Tarefa não encontrada nesta categoria!");
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    // Método para excluir uma tarefa
    public void excluirTarefa() {
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa: ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);  // Busca a categoria pelo nome
            System.out.println("Nome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> tarefas = arqTarefa.readAll(c.getId());  // Busca todas as tarefas relacionadas à categoria

            int idTarefa = -1;
            for (Tarefa tmp : tarefas) {
                if (tmp.getNome().equals(nomeTarefa)) {  // Verifica se o nome da tarefa corresponde
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);  // Exibe a tarefa encontrada
                    idTarefa = tmp.getId();
                    break;
                }
            }

            if (idTarefa != -1) {
                System.out.println("Confirma a exclusao da tarefa? (S/N) ");
                char resp = sc.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        if (arqTarefa.delete(idTarefa)) {
                            System.out.println("Tarefa excluída com sucesso.");
                        } else {
                            System.out.println("Tarefa inexistente");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro do sistema. Não foi possível excluir a tarefa!");
                    }
                }
            } else {
                System.out.println("Tarefa não encontrada nesta categoria!");
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    public void listarPorCategoria() {
        System.out.println("\n> Buscar Tarefa por Categoria:");
        try {
            // Obtém todas as categorias existentes
            List<Categoria> categorias = arqCategoria.readAll();
            
            // Verifica se há categorias cadastradas
            if (categorias.isEmpty()) {
                System.out.println("Não há categorias cadastradas!");
            } else {
                // Lista as categorias disponíveis
                arqCategoria.list();
                System.out.print("> ");
                int idCategoria = Integer.parseInt(sc.nextLine());
    
                // Verifica se o ID da categoria inserido é válido
                if (idCategoria > 0) {
                    // Obtém todas as tarefas associadas à categoria escolhida
                    List<Tarefa> tarefas = arqTarefa.readAll(idCategoria);
    
                    // Verifica se há tarefas cadastradas para a categoria escolhida
                    if (tarefas.isEmpty()) {
                        System.out.println("Não há tarefas cadastradas!");
                    } else {
                        // Exibe a lista de tarefas da categoria
                        System.out.println("\nLista de tarefas:");
                        for (Tarefa tarefa : tarefas) {
                            System.out.println(tarefa);
                        }
                    }
                } else {
                    System.out.println("ID inválido!");
                }
            }
        } catch (Exception e) { // Trata qualquer exceção inesperada que possa ocorrer
            System.out.println("Erro no sistema. Não foi possível buscar tarefa!");
        }
    }
    
    

    // Método para listar os status disponíveis
    private static void listaStatus() {
        System.out.println("\nEscolha o status:"
                + "\n0 - Pendente"
                + "\n1 - Em Progresso"
                + "\n2 - Concluída"
                + "\nStatus: ");
    }

    // Método para listar as prioridades disponíveis
    private static void listaPrioridades() {
        System.out.println("\nEscolha a prioridade:"
                + "\n0 - Baixa"
                + "\n1 - Média"
                + "\n2 - Alta"
                + "\n3 - Urgente"
                + "\nOpção: ");
    }
}
