package Main;

import Entidades.Tarefa;
import Enums.Prioridade;
import Enums.Status;
import File.ArquivoCategoria;
import File.ArquivoTarefa;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MenuTarefas extends Principal {

    private static ArquivoTarefa arqTarefas;
    private static ArquivoCategoria arqCategoria;

    public MenuTarefas() throws Exception {
        arqTarefas = new ArquivoTarefa();
    }

    public void menu() {
        try {
            int opcao = 0;
            do {
                opcoesMenu();
                opcao = leOpcao();
                executaOpcao(opcao);
            } while (opcao != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void opcoesMenu() {
        System.out.println("-------------------------"
                + "\nTarefas       "
                + "\n1 - Buscar               "
                + "\n2 - Incluir              "
                + "\n3 - Alterar              "
                + "\n4 - Excluir              "
                + "\n0 - Voltar               "
                + "\nOpção: ");
    }

    protected static void executaOpcao(int opcao) throws Exception {
        switch (opcao) {
            case 0:
                break;
            case 1:
                arqTarefas.buscaTarefa();
                break;
            case 2:
                incluiTarefa();
                break;
            case 3:
                arqTarefas.alteraTarefa();
                break;
            case 4:
                arqTarefas.excluiTarefa();
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    public static LocalDate formatarData(String dataStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = null;
        try {
            data = LocalDate.parse(dataStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("\nFormato inválido. Por favor, use o formato dd/MM/yyyy.");
        }
        return data;
    }

    public static void listaStatus() {
        System.out.println("\nEscolha o status:"
                + "\n0 - Pendente"
                + "\n1 - Em Progresso"
                + "\n2 - Concluída"
                + "\nStatus: ");
    }

    public static void listaPrioridades() {
        System.out.println("\nEscolha a prioridade:"
                + "\n0 - Baixa                "
                + "\n1 - Média                "
                + "\n2 - Alta                 "
                + "\n3 - Urgente              "
                + "\nOpcao: ");
    }

    public static Tarefa leTarefa() {
        Tarefa tarefa = null;
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.println("\nData de Criacao (dd/MM/yyyy) - 0 para data atual");
            System.out.print(": ");
            String dc1 = sc.nextLine();
            LocalDate dataCriacao = (dc1.equals("0")) ? LocalDate.now() : formatarData(dc1);

            listaStatus();
            byte statusB = Byte.parseByte(sc.nextLine());
            Status status = Status.fromByte(statusB);

            listaPrioridades();
            byte prioridadeB = Byte.parseByte(sc.nextLine());
            Prioridade prioridade = Prioridade.fromByte(prioridadeB);

            arqCategoria.mostraCategorias();
            int idCategoria = Integer.parseInt(sc.nextLine());

            tarefa = new Tarefa(nome, dataCriacao, status, prioridade, idCategoria);
        } catch (Exception e) {
            System.out.println("\nErro na leitura da tarefa!");
        }
        return tarefa;
    }

    public static void incluiTarefa() {
        System.out.println("\nIncluir Tarefa:");
        try {
            Tarefa novaTarefa = leTarefa();
            if (novaTarefa != null) {
                System.out.println(novaTarefa);
                System.out.println("\nConfirma inclusao? (S/N)");
                char resp = sc.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        arqTarefas.create(novaTarefa);
                        System.out.println("Tarefa criada!");
                    } catch (Exception e) {
                        System.out.println("Erro do sistema. Nao foi possível criar a tarefa!");
                    }
                } else {
                    System.out.println("Inclusao cancelada!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao incluir!");
        }
    }
}
