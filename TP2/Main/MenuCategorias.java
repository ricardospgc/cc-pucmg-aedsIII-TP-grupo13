package Main;

import Entidades.*;
import File.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuCategorias {

    ArquivoTarefa arqTarefa;
    ArquivoCategoria arqCategoria;
    private static Scanner sc = new Scanner(System.in);

    public MenuCategorias() throws Exception {
        arqTarefa = new ArquivoTarefa();
        arqCategoria = new ArquivoCategoria();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\nAEDsIII");
            System.out.println("-------");
            System.out.println("- Inicio Categorias");
            System.out.println("1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarCategoria();
                    break;
                case 2:
                    incluirCategoria();
                    break;
                case 3:
                    alterarCategoria();
                    break;
                case 4:
                    excluirCategoria();
                    break;
                case 5:
                    arqCategoria.list();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void buscarCategoria() {
        String nome;

        System.out.println("\nBuscar categoria");
        System.out.print("\nNome da categoria: ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;
        }
        try {
            Categoria c = arqCategoria.read(nome);
            System.out.println(c.toString());

            int idCategoria = c.getId();
            ArrayList<Tarefa> tarefas = arqTarefa.readAll(idCategoria);
            for (Tarefa tmp : tarefas) {
                System.out.println(tmp);
            }
        } catch (Exception e) {
            System.out.println("Categoria nao encontrada!");
        }
    }

    public void incluirCategoria() {
        String nome;
        boolean dadosCompletos = false;

        System.out.println("\nInclusao de categoria");
        do {
            System.out.print("\nNome da categoria (min. de 5 letras): ");
            nome = sc.nextLine();
            if (nome.length() >= 5 || nome.length() == 0) {
                dadosCompletos = true;
            } else {
                System.err.println("O nome da categoria deve ter no mínimo 5 caracteres.");
            }
        } while (!dadosCompletos);

        if (nome.length() == 0) {
            return;
        }
        System.out.println("Confirma a inclusao da categoria? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Categoria c = new Categoria(nome);
                arqCategoria.create(c);
                System.out.println("Categoria criada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro do sistema. Não foi possivel criar a categoria!");
            }
        }
    }

    public void alterarCategoria() {
        String nome;

        System.out.println("\nAlterar categoria");
        System.out.print("\nNome da categoria (min. de 5 letras): ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;
        }
        System.out.println("Confirma a alteracao da categoria? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Categoria c = arqCategoria.read(nome);
                System.out.println("Digite o novo nome da categoria: ");
                nome = sc.nextLine();

                c.setNome(nome);
                arqCategoria.update(c);

                System.out.println("Categoria atualizada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro do sistema. Nao foi possivel criar a categoria!");
            }
        }
    }

    public void excluirCategoria() {
        String nome;

        System.out.println("\nExcluir categoria: ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;
        }
        try {
            int idCategoria = arqCategoria.read(nome).getId();
            if (!arqTarefa.readAll(idCategoria).isEmpty()) {
                System.err.println("Nao eh possivel excluir, existem tarefas relacionadas");
                return;
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }

        System.out.println("Confirma a exclusao da categoria? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                if (arqCategoria.delete(nome)) {
                    System.out.println("Categoria excluida com sucesso.");
                } else {
                    System.out.println("Categoria inexistente");
                }
            } catch (Exception e) {
                System.err.println("Erro do sistema. Nao foi possível excluir a categoria!");
            }
        }
    }
}
