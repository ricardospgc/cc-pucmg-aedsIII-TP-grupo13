package Main;

import Entidades.*;
import File.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuCategorias {

    ArquivoTarefa arqTarefa;  // Arquivo para manipulação das tarefas
    ArquivoCategoria arqCategoria;  // Arquivo para manipulação das categorias
    private static Scanner sc = new Scanner(System.in);  // Scanner para leitura de entrada do usuário

    // Construtor da classe que inicializa os arquivos de categorias e tarefas
    public MenuCategorias() throws Exception {
        arqTarefa = new ArquivoTarefa();
        arqCategoria = new ArquivoCategoria();
    }

    // Método principal do menu de categorias
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
                opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
            } catch (NumberFormatException e) {
                opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
            }

            // Switch para tratar cada opção do menu
            switch (opcao) {
                case 1:
                    buscarCategoria();  // Chama o método para buscar uma categoria
                    break;
                case 2:
                    incluirCategoria();  // Chama o método para incluir uma nova categoria
                    break;
                case 3:
                    alterarCategoria();  // Chama o método para alterar uma categoria existente
                    break;
                case 4:
                    excluirCategoria();  // Chama o método para excluir uma categoria
                    break;
                case 5:
                    arqCategoria.list();  // Chama o método para listar todas as categorias
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");  // Mensagem para opção inválida
                    break;
            }

        } while (opcao != 0);  // Repete o menu até que o usuário escolha sair
    }

    // Método para buscar uma categoria pelo nome
    public void buscarCategoria() {
        String nome;

        System.out.println("\nBuscar categoria");
        System.out.print("\nNome da categoria: ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        try {
            Categoria c = arqCategoria.read(nome);  // Busca a categoria pelo nome
            System.out.println(c.toString());  // Exibe a categoria encontrada

            int idCategoria = c.getId();
            ArrayList<Tarefa> tarefas = arqTarefa.readAll(idCategoria);  // Busca todas as tarefas relacionadas à categoria
            for (Tarefa tmp : tarefas) {
                System.out.println(tmp);  // Exibe cada tarefa relacionada
            }
        } catch (Exception e) {
            System.out.println("Categoria nao encontrada!");  // Mensagem caso a categoria não seja encontrada
        }
    }

    // Método para incluir uma nova categoria
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
        } while (!dadosCompletos);  // Solicita o nome da categoria até que tenha pelo menos 5 caracteres

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        System.out.println("Confirma a inclusao da categoria? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Categoria c = new Categoria(nome);  // Cria uma nova categoria
                arqCategoria.create(c);  // Adiciona a categoria ao arquivo
                System.out.println("Categoria criada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro do sistema. Não foi possivel criar a categoria!");
            }
        }
    }

    // Método para alterar uma categoria existente
    public void alterarCategoria() {
        String nome;

        System.out.println("\nAlterar categoria");
        System.out.print("\nNome da categoria (min. de 5 letras): ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        System.out.println("Confirma a alteracao da categoria? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Categoria c = arqCategoria.read(nome);  // Busca a categoria pelo nome
                System.out.println("Digite o novo nome da categoria: ");
                nome = sc.nextLine();

                c.setNome(nome);  // Altera o nome da categoria
                arqCategoria.update(c);  // Atualiza a categoria no arquivo

                System.out.println("Categoria atualizada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro do sistema. Nao foi possivel criar a categoria!");
            }
        }
    }

    // Método para excluir uma categoria
    public void excluirCategoria() {
        String nome;

        System.out.println("\nExcluir categoria: ");
        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        try {
            int idCategoria = arqCategoria.read(nome).getId();  // Busca o ID da categoria pelo nome
            if (!arqTarefa.readAll(idCategoria).isEmpty()) {  // Verifica se existem tarefas relacionadas à categoria
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
                if (arqCategoria.delete(nome)) {  // Exclui a categoria do arquivo
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
