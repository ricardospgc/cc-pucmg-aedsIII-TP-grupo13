package Main;

import File.Backup;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class MenuBackup extends Principal {

    private static Backup backup;
    private static Scanner sc;

    public MenuBackup() throws Exception {
        backup = new Backup();
    } // MenuBackup ( )

    public void menu() {
        try {
            sc = new Scanner(System.in);  // Scanner para leitura de entrada do usuário
            int opcao;

            // Loop do menu principal do programa
            do {
                System.out.println("\n> ----------------------");
                System.out.println("> Início > Backup        ");
                System.out.println("1 - Realizar Backup      ");
                System.out.println("2 - Restaurar Backup     ");
                System.out.println("0 - Voltar               ");
                System.out.print("Opção: ");
                System.out.print("Opcao: ");

                try {
                    opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
                } catch (NumberFormatException e) {
                    opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
                }

                // Switch para tratar cada opção do menu principal
                switch (opcao) {
                    case 0:
                        break;
                    case 1:
                        realizarBackup();
                        break;
                    case 2:
                        restaurarBackup();
                        break;
                    default:
                        System.err.println("Opção inválida.");
                        break;
                }

            } while (opcao != 0);  // Repete o menu até que o usuário escolha sair

        } catch (Exception e) {
            e.printStackTrace();  // Exibe o stack trace em caso de erro inesperado
        }
    }

    public static void realizarBackup() throws Exception {
        System.out.println("\n> Relizando Backup:");
        try {
            System.out.println("\nConfirma a realização do backup? (S/N)");
            char resp = sc.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {
                backup.createBackup(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss")) + ".db");
                System.out.println("Backup realizado com sucesso.");
            } else {
                System.out.println("Operação cancelada.");
            } // if
        } catch (Exception e) {
            System.err.println("Erro ao realizar backup.");
        } // try-catch
    } // realizarBackup ( )

    public static void restaurarBackup() throws Exception {
        System.out.println("\n> Restaurar Backup:");

        try {
            ArrayList<String> backupsList = backup.listBackups();

            if (!backupsList.isEmpty()) {
                System.out.print("ID do arquivo de backup: ");
                String input = sc.nextLine();

                if (input.length() > 0) {
                    int idBackup = Integer.parseInt(input);
                    backup.restoreBackup(backupsList.get(idBackup - 1) + ".db");
                    System.out.println("Backup restaurado com sucesso.");
                } else {
                    System.err.println("ID inválido. Operação cancelada!");
                } // if

            } // if
        } catch (Exception e) {
            System.err.println("Erro ao restaurar backup.");
        } // try-catch
    } // restaurarBackup ( )

} // MenuBackup
