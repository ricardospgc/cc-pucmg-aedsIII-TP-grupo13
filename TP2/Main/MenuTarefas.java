package Main;

import Entidades.*;
import Enums.Prioridade;
import Enums.Status;
import File.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuTarefas {

    ArquivoTarefa arqTarefa;
    ArquivoCategoria arqCategoria;
    private static Scanner sc = new Scanner(System.in);

    public MenuTarefas() throws Exception {
        arqTarefa = new ArquivoTarefa();
        arqCategoria = new ArquivoCategoria();
    }

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
            System.out.println("0 - Voltar");

            System.out.print("Opcao: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarTarefa();
                    break;
                case 2:
                    incluirTarefa();
                    break;
                case 3:
                    alterarTarefa();
                    break;
                case 4:
                    excluirTarefa();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void buscarTarefa() {
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa: ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);
            System.out.println("Nome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> t = arqTarefa.readAll(c.getId());

            for (Tarefa tmp : t) {
                if (tmp.getNome().equals(nomeTarefa)) {
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);
                    return;
                }
            }
            System.out.println("Tarefa nao econtrada nesta categoria!");
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    public void incluirTarefa() {
        String categoria;
        int idCategoria = -1;

        System.out.println("\nInclusao de tarefa");

        System.out.print("Nome da Tarefa: ");
        String nome = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // insercao do status
        listaStatus();
        byte statusB = Byte.parseByte(sc.nextLine());
        Status status = Status.fromByte(statusB);

        // insercao da prioridade
        listaPrioridades();
        byte prioridadeB = Byte.parseByte(sc.nextLine());
        Prioridade prioridade = Prioridade.fromByte(prioridadeB);

        System.out.print("Data de Criação (dd/MM/yyyy) - 0 para data atual: ");
        String dc = sc.nextLine();
        LocalDate dataCriacao = LocalDate.now();
        if (!dc.equals("0")) { 
            dataCriacao = LocalDate.parse(dc, formatter);
        }

        LocalDate dataConclusao = LocalDate.parse("01/01/1970", formatter);
        if(status == Status.CONCLUIDO){
            System.out.print("Data de Conclusão (dd/MM/yyyy): ");
            String input = sc.nextLine();
            dataConclusao = LocalDate.parse(input, formatter);
        }

        // insercao da categoria
        boolean catEscolhida = true;
        do {
            System.out.println("\n Digite o NOME DA CATEGORIA para a tarefa: ");
            try {
                catEscolhida = true;

                arqCategoria.list();
                System.out.print("\n > ");
                categoria = sc.nextLine();
                
                idCategoria = arqCategoria.read(categoria).getId();
            } catch (Exception e) {
                System.err.println("\nCategoria inválida! Tente novamente");
                catEscolhida = false;
            }
        } while (catEscolhida == false);

        try {
            Tarefa novaTarefa = new Tarefa(idCategoria, nome, dataCriacao, dataConclusao, status, prioridade);
            arqTarefa.create(novaTarefa);
            System.out.println("Tarefa criada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro do sistema. Não foi possível criar a tarefa!");
        }

    }

    public void alterarTarefa() {
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa a ser excluida: ");
        arqCategoria.list();
        System.out.print("\n > ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);
            System.out.println("\nNome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> t = arqTarefa.readAll(c.getId());

            Tarefa tarefaVelha = new Tarefa();
            tarefaVelha.setId(-1);
            for (Tarefa tmp : t) {
                if (tmp.getNome().equals(nomeTarefa)) {
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);
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
                LocalDate dataCriacao = dc1.isEmpty() ? null : LocalDate.parse(dc1, formatter);

                System.out.print("Data de Conclusão (dd/MM/yyyy): ");
                String input = sc.nextLine();
                LocalDate dataConclusao = input.isEmpty() ? null : LocalDate.parse(input, formatter);

                listaStatus();
                byte statusB = Byte.parseByte(sc.nextLine());
                Status status = Status.fromByte(statusB);

                listaPrioridades();
                byte prioridadeB = Byte.parseByte(sc.nextLine());
                Prioridade prioridade = Prioridade.fromByte(prioridadeB);

                Tarefa novaTarefa = new Tarefa(tarefaVelha.getId(), tarefaVelha.getIdCategoria(), nome, dataCriacao, dataConclusao, status, prioridade);

                System.out.println("Confirma a alteracao da tarefa? (S/N) ");
                char resp = sc.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        if (arqTarefa.update(novaTarefa)) {
                            System.out.println("tarefa alterada com sucesso.");
                        } else {
                            System.out.println("Falha ao aletarar tarefa.");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro do sistema. Nao foi possível alterar a tarefa!");
                    }
                }
            } else {
                System.out.println("Tarefa nao econtrada nesta categoria!");
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    public void excluirTarefa(){
        String nomeCategoria;
        System.out.println("\nNome da categoria da tarefa: ");
        nomeCategoria = sc.nextLine();

        try {
            Categoria c = arqCategoria.read(nomeCategoria);
            System.out.println("Nome da tarefa: ");
            String nomeTarefa = sc.nextLine();
            ArrayList<Tarefa> tarefas = arqTarefa.readAll(c.getId());

            int idTarefa = -1;
            for (Tarefa tmp : tarefas) {
                if (tmp.getNome().equals(nomeTarefa)) {
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);
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
                            System.out.println("tarefa excluida com sucesso.");
                        } else {
                            System.out.println("tarefa inexistente");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro do sistema. Nao foi possível excluir a tarefa!");
                    }
                }
            } else {
                System.out.println("Tarefa nao econtrada nesta categoria!");
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    private static void listaStatus() {
        System.out.println("\nEscolha o status:"
                + "\n0 - Pendente"
                + "\n1 - Em Progresso"
                + "\n2 - Concluída"
                + "\nStatus: ");
    }

    private static void listaPrioridades() {
        System.out.println("\nEscolha a prioridade:"
                + "\n0 - Baixa                "
                + "\n1 - Média                "
                + "\n2 - Alta                 "
                + "\n3 - Urgente              "
                + "\nOpcao: ");
    }
}
