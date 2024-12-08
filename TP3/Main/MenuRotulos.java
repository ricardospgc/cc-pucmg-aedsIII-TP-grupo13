package Main;

import Entidades.*;
import File.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuRotulos {

    public static ArquivoRotulo arqRotulo;
    Scanner scanf = new Scanner(System.in);

    public void menu() throws Exception {

        int opcao;
        do {
            arqRotulo = new ArquivoRotulo();
            System.out.println("\n> Inicio > Rotulos");
            System.out.println("1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = Integer.parseInt(scanf.nextLine());
            System.out.println();
            switch (opcao) {
                case 1:
                    criarRotulo();
                    break;
                case 2:
                    listarRotulo();
                    break;
                case 3:
                    atualizarRotulo();
                    break;
                case 4:
                    deletarRotulo();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
        } while (opcao != 0);
    }

    public void criarRotulo() throws Exception {
        try {
            System.out.println("Digite o NOME DO ROTULO a ser criado:");
            arqRotulo.create(scanf.nextLine());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Criado com sucesso");
        System.out.println();
        arqRotulo.listar();
    }

    public void listarRotulo() throws Exception {
        String nomeRotulo;
        try {
            arqRotulo.listar();

            System.out.print("\nDigite o NOME DO ROTULO que deseja listar as tarefas: ");

            nomeRotulo = scanf.nextLine();

            ArrayList<Tarefa> t = arqRotulo.readTarefasPorRotulo(nomeRotulo);

            // Itera sobre os rotulos
            for (Tarefa tmp : t) {
                System.out.println(tmp);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void atualizarRotulo() throws Exception {
        String nomeRotulo, novaRotulo;
        try {
            arqRotulo.listar();

            System.out.print("Digite o NOME DO ROTULO que deseja atualizar: ");
            nomeRotulo = scanf.nextLine();

            ArrayList<Rotulo> t = arqRotulo.listar();
            boolean find = false;
            for (Rotulo tmp : t) {
                if (tmp.getNome().equals(nomeRotulo)) {
                    find = true;
                }
            }
            if (find == false) {
                System.out.println("Rotulo nao encontrado");
                return;
            }

            System.out.println("Digite o nome do novo Rotulo:");
            novaRotulo = scanf.nextLine();

            arqRotulo.update(nomeRotulo, novaRotulo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Atualizado com sucesso");
    }

    public void deletarRotulo() throws Exception {
        String nomeRotulo;
        try {
            ArrayList<Rotulo> Rotulos = arqRotulo.listar();
            System.out.println();

            System.out.print("Digite o INDICE DO ROTULO que deseja deletar: ");
            int index = scanf.nextInt();
            if (index < 0 || index > Rotulos.size()) {
                System.out.println("Indice invalido");
                return;
            }

            nomeRotulo = Rotulos.get(index - 1).getNome();
            if (arqRotulo.delete(nomeRotulo)) {
                System.out.println("Deletado com sucesso");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
