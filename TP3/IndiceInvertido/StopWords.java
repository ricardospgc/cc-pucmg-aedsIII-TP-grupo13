package IndiceInvertido;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class StopWords {

    /* Lista para armazenar as palavras consideradas como stop words */
    public ArrayList<String> listaStopWords = new ArrayList<String>();
    public ListaInvertida listaInvertida;

    /* 
     * Método responsável por processar um título e remover palavras que são stop words. 
     * Retorna o título dividido em palavras já processadas.
     */
    public String[] processarStopWords(String titulo) {
        String palavras[] = titulo.split(" "); // Divide o título em palavras
        for (int i = 0; i < palavras.length; i++) {
            palavras[i] = palavras[i].toLowerCase(); // Converte cada palavra para minúsculas
            System.out.println("Palavra processada: " + palavras[i]);
        }
        for (int i = 0; i < palavras.length; i++) {
            for (int j = 0; j < listaStopWords.size(); j++) {
                if (palavras[i].equals(listaStopWords.get(j))) { // Verifica se é uma stop word
                    palavras[i] = ""; // Remove a palavra substituindo por uma string vazia
                }
            }
        }
        return palavras; // Retorna as palavras processadas
    }

    /* 
     * Método para contar palavras não vazias e calcular a frequência relativa 
     * de cada uma no contexto de uma tarefa específica.
     */
    public void contarPalavras(ArrayList<ElementoLista> elementos, String[] palavras, int idElemento) {
        int totalPalavrasValidas = 0;

        /* Conta as palavras válidas, ignorando strings vazias ou espaços */
        for (int i = 0; i < palavras.length; i++) {
            if (!palavras[i].equals("") && !palavras[i].equals(" ")) {
                totalPalavrasValidas++;
            }
        }

        /* Calcula a frequência relativa de cada palavra */
        for (int i = 0; i < palavras.length; i++) {
            float frequencia = 1;
            if (!palavras[i].equals("") && !palavras[i].equals(" ")) {
                for (int j = 0; j < palavras.length; j++) {
                    if (palavras[i].equals(palavras[j]) && i != j) { // Verifica duplicatas
                        frequencia++; // Incrementa a frequência
                        palavras[j] = ""; // Marca como processada
                    }
                }
            }
            elementos.add(new ElementoLista(idElemento, frequencia / totalPalavrasValidas));
        }
    }

    /* 
     * Construtor da classe StopWords. 
     * Inicializa a lista de stop words a partir de um arquivo e configura a lista invertida.
     */
    public StopWords() {
        File arquivo;

        /* Carrega as stop words do arquivo */
        try {
            arquivo = new File("./IndiceInvertido/stopwords.txt");
            Scanner scanner = new Scanner(arquivo);
            while (scanner.hasNext()) {
                String linha = scanner.nextLine();
                linha = linha.toLowerCase(); // Converte para minúsculas
                linha = linha.substring(0, linha.length() - 1); // Remove o último caractere
                listaStopWords.add(linha);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir o arquivo de stop words");
        }

        /* Inicializa a lista invertida */
        try {
            File diretorioDados = new File("dados");
            if (!diretorioDados.exists()) {
                diretorioDados.mkdir();
            }
            listaInvertida = new ListaInvertida(
                4, 
                "./BaseDeDados/dicionario.listainv.db", 
                "./BaseDeDados/blocos.listainv.db"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao inicializar a lista invertida");
        }
    }

    /* 
     * Insere palavras de um título processado na lista invertida, 
     * calculando a frequência relativa para cada palavra.
     */
    public void inserirTitulo(String titulo, int idTarefa) {
        String[] palavrasProcessadas;
        palavrasProcessadas = processarStopWords(titulo); // Remove stop words do título

        ArrayList<ElementoLista> elementos = new ArrayList<>();
        contarPalavras(elementos, palavrasProcessadas, idTarefa);

        /* Insere cada palavra válida na lista invertida */
        for (int i = 0; i < palavrasProcessadas.length; i++) {
            if (!palavrasProcessadas[i].equals("") && !palavrasProcessadas[i].equals(" ")) {
                try {
                    listaInvertida.create(palavrasProcessadas[i], elementos.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erro ao inserir na lista invertida");
                }
            }
        }

        /* Atualiza o contador de entidades na lista invertida */
        try {
            listaInvertida.incrementaEntidades();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao incrementar o contador de entidades");
        }
    }

    /* Método principal para testes */
    public void executarTeste(String[] args) {
        StopWords stopWords = new StopWords();
        Scanner scannerConsole = new Scanner(System.in);
        boolean adicionarNovaTarefa = true;

        /* Adiciona frases à lista invertida */
        try {
            while (adicionarNovaTarefa) {
                System.out.println("Digite o título da tarefa: ");
                String titulo = scannerConsole.nextLine();
                inserirTitulo(titulo, 0);

                System.out.println("Deseja adicionar outra tarefa? (S/N)");
                String resposta = scannerConsole.nextLine();
                if (resposta.equalsIgnoreCase("N")) {
                    adicionarNovaTarefa = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        scannerConsole.close(); // Fecha o scanner
    }
}
