package File;

import Entidades.Tarefa;
import Enums.Prioridade;
import Enums.Status;
import Main.MenuTarefas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    Arquivo<Tarefa> arqTarefa;
    ArvoreBMais<ParIdId> indiceIndiretoIdCategoria;
    ArquivoCategoria arqCategoria;
    protected Scanner sc = new Scanner(System.in);

    public ArquivoTarefa() throws Exception {
        super(Tarefa.class.getConstructor(), "Tarefas.db");
        indiceIndiretoIdCategoria = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                4,
                "BaseDeDados//IndiceIndiretoId.btree.db"
        );
    }

    public void mostrarTarefas() throws Exception {
        System.out.println("Você deseja mostrar as tarefas de todas as categorias? (S/N)");
        char resp = sc.nextLine().charAt(0);
        ArrayList<ParIdId> ict = indiceIndiretoIdCategoria.read(null);
        if (resp == 'S' || resp == 's') {

            System.out.println("\nResposta: ");

            for (int i = 0; i < ict.size(); i++) {
                Tarefa tarefa = arqTarefa.read(ict.get(i).getIDTarefa());
                if (tarefa != null) {
                    System.out.println(tarefa.toString() + " ");
                    System.out.println();
                }
            }
        } else {
            System.out.println("Selecione a categoria desejada: ");
            int idCategoria = arqCategoria.leCategoriaPorNome();
            for (int i = 0; i < ict.size(); i++) {

                Tarefa tarefa = arqTarefa.read(ict.get(i).getIDTarefa());
                if ((tarefa != null) && (tarefa.getIdCategoria() == idCategoria)) {
                    System.out.println(tarefa.toString() + " ");
                    System.out.println();
                }
            }
        }
    }

    @Override
    public int create(Tarefa c) throws Exception {
        int id = arqTarefa.create(c);
        indiceIndiretoIdCategoria.create(new ParIdId(c.getIdCategoria(), c.getId()));
        return id;
    }

    public void buscaTarefa() throws Exception {
        String entrada;
        System.out.println("Você deseja pesquisar alguma tarefa? (S/N)");
        char caracter = sc.nextLine().trim().toUpperCase().charAt(0); // Melhorado para ser case insensitive

        if (caracter == 'S') {
            System.out.println("Mostrando as tarefas existentes...");
            mostrarTarefas(); // Presumo que você tenha esse método implementado

            System.out.println("Entre com o nome da tarefa a ser retornada: ");
            entrada = sc.nextLine();

            ArrayList<ParIdId> ict = indiceIndiretoIdCategoria.read(null); // Certifique-se de que arvoreBMais não retorne
            // null

            if (ict != null) {
                Tarefa tarefa;
                for (ParIdId idCategoriaIdTarefa : ict) {
                    tarefa = arqTarefa.read(idCategoriaIdTarefa.getIDTarefa()); // Lendo a tarefa a partir do ID da categoria
                    if (tarefa != null && tarefa.getNome().equals(entrada)) { // Simplificado
                        System.out.println(tarefa.toString());
                    }
                }
            } else {
                System.out.println("Nenhuma tarefa encontrada.");
            }
        }
    }

    public void excluiTarefa() throws Exception {
        boolean resp = false;
        String entrada;
        System.out.println("Você realmente deseja apagar alguma tarefa? (S/N)");
        char caracter = sc.nextLine().charAt(0);
        if (caracter == 'S' || caracter == 's') {
            mostrarTarefas();
            System.out.println("Entre com o nome da tarefa a ser deletada: ");
            entrada = sc.nextLine();
            ArrayList<ParIdId> ict = indiceIndiretoIdCategoria.read(null);
            Tarefa tarefa;
            for (int i = 0; i < ict.size(); i++) {
                tarefa = arqTarefa.read(ict.get(i).getIDTarefa());
                if (tarefa != null) {
                    if (tarefa.getNome().equals(entrada)) {
                        int idTarefa = tarefa.getId();
                        resp = arqTarefa.delete(idTarefa);
                    }
                }
            }
            if (resp == true) {
                System.out.println("Tarefa apagada com sucesso!");
            }
        }
    }

    public void alteraTarefa() throws Exception {
        String entrada;
        boolean resp = false;
        System.out.println("Você deseja atualizar alguma tarefa? (S/N)");
        char caracter = sc.nextLine().trim().toUpperCase().charAt(0);

        if (caracter == 'S') {
            System.out.println("Mostrando as tarefas existentes...");
            mostrarTarefas(); // Presumo que você tenha esse método implementado

            System.out.println("Entre com o nome da tarefa a ser atualizada: ");
            entrada = sc.nextLine();

            // Certifique-se de que 'arvoreBMais' não retorne null
            ArrayList<ParIdId> ict = indiceIndiretoIdCategoria.read(null);

            if (ict != null) {
                for (ParIdId ParIdId : ict) {
                    Tarefa tarefa = arqTarefa.read(ParIdId.getIDTarefa()); // Lendo a tarefa a partir do ID da
                    // categoria

                    if (tarefa != null && tarefa.getNome().equals(entrada)) {
                        System.out.print("Novo nome: ");
                        String nome = sc.nextLine();

                        System.out.println("\nNova Data de Criacao (dd/MM/yyyy) - 0 para data atual");
                        System.out.print(": ");
                        String dc1 = sc.nextLine();
                        LocalDate dataCriacao = (dc1.equals("0")) ? LocalDate.now() : MenuTarefas.formatarData(dc1);

                        MenuTarefas.listaStatus();
                        byte statusB = Byte.parseByte(sc.nextLine());
                        Status status = Status.fromByte(statusB);

                        MenuTarefas.listaPrioridades();
                        byte prioridadeB = Byte.parseByte(sc.nextLine());
                        Prioridade prioridade = Prioridade.fromByte(prioridadeB);

                        arqCategoria.mostraCategorias();
                        int idCategoria = Integer.parseInt(sc.nextLine());

                        tarefa = new Tarefa(nome, dataCriacao, status, prioridade, idCategoria);
                        arqTarefa.update(tarefa);
                        if (resp) {
                            System.out.println("Tarefa atualizada com sucesso!");
                        }
                    }
                }
            } else {
                System.out.println("Nenhuma tarefa encontrada.");
            }
        }
    }
    

    public int tarefasPorCategoria(int idCategoria) throws Exception {
        int n = 0;
        ArrayList<ParIdId> ict = indiceIndiretoIdCategoria.read(null);
        for (int i = 0; i < ict.size(); i++) {
            Tarefa tarefa = arqTarefa.read(ict.get(i).getIDTarefa());
            if ((tarefa != null) && (tarefa.getIdCategoria() == idCategoria)) {
                n++;
            }
        }
        return n;
    }
}
