package Main;

import java.util.Scanner;

/*
 * NA PASTA TP2, RODAR OS SEGUINTES COMANDOS NO TERMINAL:
 * javac -d NULL File/*.java Entidades/*.java Enums/*.java Interface/*.java Main/*.java
 * java -cp NULL Main.Principal
 */

// Classe principal do programa, responsável por exibir o menu inicial e direcionar para os menus específicos
public class Principal {

    public static void main(String[] args) {
        Scanner sc;

        try {
            sc = new Scanner(System.in);  // Scanner para leitura de entrada do usuário
            int opcao;

            // Loop do menu principal do programa
            do {
                System.out.println("\n> Inicio");
                System.out.println("-------");
                System.out.println("1 - Categorias");
                System.out.println("2 - Tarefas");
                System.out.println("0 - Sair");
                System.out.print("Opcao: ");

                try {
                    opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
                } catch (NumberFormatException e) {
                    opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
                }

                // Switch para tratar cada opção do menu principal
                switch (opcao) {
                    case 1:
                        (new MenuCategorias()).menu();  // Chama o menu de categorias
                        break;
                    case 2:
                        (new MenuTarefas()).menu();  // Chama o menu de tarefas
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcao invalida!");  // Mensagem para opção inválida
                        break;
                }

            } while (opcao != 0);  // Repete o menu até que o usuário escolha sair

        } catch (Exception e) {
            e.printStackTrace();  // Exibe o stack trace em caso de erro inesperado
        }
    }
}
