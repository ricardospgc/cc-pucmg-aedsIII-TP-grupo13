package Main;

import Entidades.Categoria;
import File.ArquivoCategoria;


public class MenuCategorias extends Principal {
    private static ArquivoCategoria arqCategorias;

    public MenuCategorias () throws Exception {
        arqCategorias = new ArquivoCategoria();
    } 

    public void menu() {
        try{
            int opcao = 0;
            do {
                opcoesMenu();
                opcao = leOpcao();
                executaOpcao(opcao);
            } while( opcao != 0 ); 
        } catch ( Exception e ) {
            e.printStackTrace();
        } 
    } 

    protected static void opcoes_menu() {
        System.out.println("\nAEDs-III 1.0           "
        +"\n-------------------------"
        +"\n> Início > Categorias    "
        +"\n1 - Buscar               "
        +"\n2 - Incluir              "
        +"\n3 - Alterar              "
        +"\n4 - Excluir              "
        +"\n0 - Voltar               "
        +"\nOpção: ");
    } // end opcoes_menu ()

    protected static void executar_opcao(int opcao) {
        switch(opcao) {
            case 0:
                break;
            case 1:
                buscaCategoria();
                break;
            case 2:
                incluiCategoria();
                break;
            case 3:
                alteraCategoria();
                break;
            case 4:
                excluiCategoria();
                break;
            
            default:
                System.out.println("Opção inválida!");
                break;
        } 
    } 

    public static void incluiCategoria() {
        String  nome = "";
        boolean dadosCompletos = false;
        System.out.println("\nInclusão de categoria:");
        do {
            System.out.print("\nNome da categoria (min. de 4 letras): ");
            nome = sc.nextLine();
            if( nome.length() >= 4 || nome.length() == 0 ) {
                dadosCompletos = true;
            } else {
                System.err.println("O nome da categoria deve ter no mínimo 4 caracteres.");
            } 
        } while( dadosCompletos == false ); 
        if( nome.length() > 0 ) {
            System.out.println("Confirma a inclusao? (S/N)");
            char resp = sc.nextLine().charAt(0);
            if(resp == 'S' || resp == 's') {
                try {
                    Categoria c = new Categoria(nome);
                    arqCategorias.create(c);
                    System.out.println("Categoria < "+ nome +">criada!");
                } catch(Exception e) {
                    System.out.println("Erro! " + e.getMessage());
                } 
            } 
        }

    } 

    public static boolean buscaCategoria() {
        boolean result = false;
        System.out.println( "\nBuscar categoria:" );
        return result;
    } 

    public static boolean alteraCategoria() {
        boolean result = false;
        System.out.println( "\nAlterar categoria:" );
        return result;
    } 

    public static boolean excluiCategoria() {
        boolean result = false;
        System.out.println( "\nExcluir categoria:" );
        return result;
    }

}
