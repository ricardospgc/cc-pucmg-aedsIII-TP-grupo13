package Main;

import File.Backup;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class MenuBackup extends Principal {

    private static Backup backup;
    private static Scanner sc;

    /*
    func: MenuBackup
    Construtor que inicializa a classe Backup.
    Retornos: Nenhum
     */
    public MenuBackup() throws Exception {
        backup = new Backup();
    }

    /*
    func: menu
    Exibe o menu principal para realizar ou restaurar backups.
    Retornos: Nenhum
     */
    public void menu() {
        try {
            sc = new Scanner(System.in);
            int opcao;
            do {
                System.out.println("\n> ----------------------");
                System.out.println("> Início > Backup        ");
                System.out.println("1 - Realizar Backup      ");
                System.out.println("2 - Restaurar Backup     ");
                System.out.println("0 - Voltar               ");
                System.out.print("Opção: ");
                try {
                    opcao = Integer.valueOf(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }
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
            } while (opcao != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    func: realizarBackup
    Solicita confirmação do usuário e realiza o backup dos dados.
    Retornos: Nenhum
     */
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
            }
        } catch (Exception e) {
            System.err.println("Erro ao realizar backup.");
        }
    }

    /*
    func: restaurarBackup
    Lista os backups disponíveis e restaura o backup selecionado pelo usuário.
    Retornos: Nenhum
     */
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
                } 
            } 
        } catch (Exception e) {
            System.err.println("Erro ao restaurar backup.");
        } 
    } 
} 
