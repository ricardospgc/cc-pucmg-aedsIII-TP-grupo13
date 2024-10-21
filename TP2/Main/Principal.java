package Main;

import java.util.Scanner;

/*
Na pasta ../TP2/
Compilar: javac -d NULL File/*.java Entidades/*.java Enums/*.java Interface/*.java Main/*.java
Rodar: java -cp NULL Main.Principal
*/

public class Principal {
    protected static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            int opcao = 0;
            do {
                opcoesMenu();
                opcao = leOpcao();
                executaOpcao(opcao);
            } while(opcao != 0); 
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }// main()

    protected static void opcoesMenu() {
        System.out.println("\nAEDs-III"
        +"\n-------------------------"
        +"\n> Inicio"
        +"\n1) Tarefas"
        +"\n2) Categorias"
        +"\n0) Sair"
        +"\nOpcao: ");
    } 

    protected static int leOpcao() {
        int opcao = 0;
        try {
            opcao = Integer.valueOf(sc.nextLine());
        } catch(NumberFormatException e) {
            opcao = -1;
        } 
        return opcao;
    } 

    protected static void executaOpcao(int opcao) throws Exception {
        switch(opcao) {
            case 0:
                System.out.println("Fim do programa.");
                break;
            case 1:
                (new MenuTarefas()).menu();
                break;
            case 2:
                (new MenuCategorias()).menu();
                break;
            default:
                System.out.println("Opcao invalida!");
                break;
        } 
    } 
} 

