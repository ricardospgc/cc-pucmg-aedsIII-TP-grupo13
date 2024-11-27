package Main;

import Entidades.*;
import Enums.*;
import File.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuTarefas {

    ArquivoTarefa arqTarefas;       // Arquivo para manipulação de tarefas
    ArquivoCategoria arqCategorias; // Arquivo para manipulação de categorias
    ArquivoRotulo arqRotulos;

    private static Scanner sc = new Scanner(System.in); // Scanner para entrada do usuário

    public MenuTarefas() throws Exception {
        arqTarefas = new ArquivoTarefa();       // Inicializa o arquivo de tarefas
        arqCategorias = new ArquivoCategoria(); // Inicializa o arquivo de categorias
        arqRotulos = new ArquivoRotulo(); // Inicializa o arquivo de categorias

    }

    public void menu() throws Exception {
        int opcao; // Opção selecionada pelo usuário
        do {
            // Exibe o menu
            System.out.println("\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Tarefas");
            System.out.println("1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Atualizar Rotulo");
            System.out.println("6 - Listar Tarefas por Categoria");
            System.out.println("0 - Voltar");

            System.out.print("Opcao: ");
            try {
                opcao = Integer.valueOf(sc.nextLine()); // Lê a opção do usuário
            } catch (NumberFormatException e) {
                opcao = -1; // Define opção inválida se ocorrer erro
            }

            System.out.println();

            switch (opcao) {
                case 1:
                    buscarTarefa(); // Método para buscar tarefa
                    break;
                case 2:
                    incluirTarefa(); // Chama o método para incluir nova tarefa
                    break;
                case 3:
                    alterarTarefa(); // Método para alterar tarefa
                    break;
                case 4:
                    excluirTarefa(); // Método para excluir tarefa
                    break;
                case 5:
                    atualizarRotulo(); // Atualiza os Rotulos
                    break;
                case 6:
                    listarTarefasPorCategoria(); // Listar as tarefas por categoria
                    break;
                case 0:
                    break; // Sai do loop se a opção for 0
                default:
                    System.out.println("Opcao invalida!"); // Mensagem de erro para opção inválida
                    break;
            }

        } while (opcao != 0); // Continua até que a opção seja 0
    }

    // Buscar uma tarefa, tendo que pesquisar pela categoria dela primeiro para acessar suas tarefas e depois fazer a busca
    public void buscarTarefa() {
        System.out.println("\nDigite o NOME DA CATEGORIA da tarefa: ");
        arqCategorias.listarCategorias();  // Lista todas as categorias
        System.out.print("\n > ");
        String nomeCategoria = sc.nextLine(); // Lê o nome da categoria

        try {
            Categoria c = arqCategorias.read(nomeCategoria); // Busca a categoria pelo nome
            ArrayList<Tarefa> t = arqTarefas.readAll(c.getId()); // Lê todas as tarefas da categoria

            if (t.isEmpty()) {
                System.out.println("Nao existem tarefas nesta categoria");
                return;
            }
            for (int i = 0; i < t.size(); i++) {
                System.out.println((i + 1) + ") " + t.get(i).getNome());
            }

            System.out.print("\nID DA TAREFA: ");
            int idTarefa = sc.nextInt();
            
            for (Tarefa tmp : t){ // Itera sobre as tarefas
                if (tmp.getId() == idTarefa) { // Verifica se a tarefa corresponde ao nome fornecido     
                    System.out.println("Tarefa encontrada: ");
                    System.out.println(tmp);
                    return;
                }
            }
            System.out.println("Id invalido!");
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    public void incluirTarefa() throws Exception {
        String categoria;
        int idCategoria = -1;

        System.out.println("\nInclusao de tarefa");

        System.out.print("Nome da Tarefa: ");
        String nome = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Inserção do status
        listaStatus();
        byte statusB = Byte.parseByte(sc.nextLine());
        Status status = Status.fromByte(statusB);

        // Inserção da prioridade
        listaPrioridades();
        byte prioridadeB = Byte.parseByte(sc.nextLine());
        Prioridade prioridade = Prioridade.fromByte(prioridadeB);

        System.out.print("Data de Criação (dd/MM/yyyy) - 0 para data atual: ");
        String dc = sc.nextLine();
        LocalDate dataCriacao = LocalDate.now();
        if (!dc.equals("0")) {
            dataCriacao = LocalDate.parse(dc, formatter);  // Define a data de criação conforme entrada do usuário
        }

        LocalDate dataConclusao = LocalDate.parse("01/01/1970", formatter);  // Data padrão para tarefas não concluídas
        if (status == Status.CONCLUIDO) {
            System.out.print("Data de Conclusão (dd/MM/yyyy): ");
            String input = sc.nextLine();
            dataConclusao = LocalDate.parse(input, formatter);  // Define a data de conclusão caso a tarefa esteja concluída
        }

        // Inserção da categoria
        boolean catEscolhida = true;
        do {
            System.out.println("\nDigite o NOME DA CATEGORIA para a tarefa: ");
            try {
                catEscolhida = true;

                arqCategorias.listarCategorias();  // Lista todas as categorias
                System.out.print("\n > ");
                categoria = sc.nextLine();

                idCategoria = arqCategorias.read(categoria).getId();  // Busca o ID da categoria pelo nome
            } catch (Exception e) {
                System.err.println("\nCategoria inválida! Tente novamente");
                catEscolhida = false;
            }
        } while (catEscolhida == false);

        int newRotulo = 1;
        ArrayList<Rotulo> rotulo = new ArrayList<>();
        ArrayList<Integer> posRotulosLista = new ArrayList<>();
        System.out.println("Digite o INDICE DO ROTULO que deseja adicionar a tarefa (0 para nenhum)");
        newRotulo = sc.nextInt();
        while (newRotulo != 0) {
            System.out.println("");
            System.out.println();
            rotulo = arqRotulos.listar();
            System.out.println();
            posRotulosLista.add(newRotulo - 1);
            System.out.println("Digite o INDICE DO ROTULO que deseja adicionar a tarefa (0 para fim)");
            newRotulo = sc.nextInt();
        }
        ArrayList<Integer> aux = new ArrayList<>();
        for (int i = 0; i < posRotulosLista.size(); i++) {
            aux.add(rotulo.get(posRotulosLista.get(i)).getId());
        }
        try {
            Tarefa novaTarefa = new Tarefa(idCategoria, nome, dataCriacao, dataConclusao, status, prioridade, aux);  // Cria uma nova tarefa
            arqTarefas.create(novaTarefa);  // Adiciona a tarefa ao arquivo
            System.out.println("Tarefa criada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro do sistema. Não foi possível criar a tarefa!");
        }
    }

    public void alterarTarefa() throws Exception {
        String termo;
        int numeroTarefa = -1;
        Tarefa t = new Tarefa(), old;
        ArrayList<Tarefa> tarefas = null;

        try {
            while (tarefas == null || tarefas.isEmpty()) {
                System.out.print("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                try {
                    tarefas = listarTarefas(termo);
                    if (tarefas == null || tarefas.isEmpty()) {
                        System.out.println("Tarefas nao encontradas");
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao listar tarefas: " + e.getMessage());
                    return;
                }
            }

            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o ID DA TAREFA que deseja atualizar (0 para cancelar)");
                try {
                    numeroTarefa = Integer.valueOf(sc.nextLine());

                    if (numeroTarefa == 0) {
                        System.out.println("Operação cancelada.");
                        return;
                    }
                    if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                        System.out.println("Tarefa não encontrada, tente novamente");
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao selecionar tarefa: " + e.getMessage());
                }
            }
            old = tarefas.get(numeroTarefa - 1);
            System.out.println("Tarefa Selecionada: " + old.getNome());

            System.out.println("\nNovos dados da tarefa: ");
            System.out.print("Nome da Tarefa: ");
            String nome = sc.nextLine();

            // Criando o formatador para dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.print("Data de Criação (dd/MM/yyyy): ");
            LocalDate data_criacao = LocalDate.parse(sc.nextLine(), formatter);

            System.out.print("Data de Conclusão (dd/MM/yyyy): ");
            String data = sc.nextLine();
            LocalDate data_conclusao = data.isEmpty() ? null : LocalDate.parse(data, formatter);

            System.out.print("Status da Tarefa (pendente / em progresso / concluída): ");
            byte status = Byte.parseByte(sc.nextLine());
            Status s = Status.fromByte(status);

            System.out.print("Prioridade da Tarefa (baixa / media / alta / urgente): ");
            byte prioridade = Byte.parseByte(sc.nextLine());
            Prioridade p = Prioridade.fromByte(prioridade);

            // Criar a nova tarefa usando o construtor
            t = new Tarefa(old.getId(), old.getIdCategoria(), nome, data_criacao, data_conclusao, s, p, old.getIdRotulos());
            System.out.println("Confirma a alteracao da tarefa? (S/N) ");
            char resp = sc.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {
                try {
                    if (arqTarefas.update(old, t)) {
                        System.out.println("Tarefa alterada com sucesso.");
                    } else {
                        System.out.println("Falha ao alterar tarefa.");
                    }
                } catch (Exception e) {
                    System.err.println("Erro do sistema. Não foi possível alterar a tarefa: " + e.getMessage());
                }
            } else {
                System.out.println("Alteração cancelada.");
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    public void excluirTarefa() throws Exception {
        try {
            String termo;
            int numeroTarefa = -1;
            ArrayList<Tarefa> tarefas = null;

            while (tarefas == null || tarefas.isEmpty()) {
                System.out.print("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                tarefas = listarTarefas(termo);
                if (tarefas == null || tarefas.isEmpty()) {
                    System.out.println("Tarefas nao encontradas");
                    return;
                }
            }
            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o ID DA TAREFA que deseja deletar (0 para cancelar)");
                numeroTarefa = sc.nextInt();
                if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                    System.out.println("Tarefa não encontrada, tente novamente");
                }
            }
            if (arqTarefas.delete(tarefas.get(numeroTarefa - 1))) {
                System.out.println("Tarefa deletada com sucesso");
            } else {
                System.out.println("Erro ao deletar a tarefa");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void listarTarefasPorCategoria() {
        System.out.print("\nDigite o NOME DA CATEGORIA que deseja listar as tarefas: ");
        arqCategorias.listarCategorias();  // Lista todas as categorias
        System.out.print("\n > ");
        String nome = sc.nextLine(); // Lê o nome da categoria

        if (nome.length() == 0) {
            return; // Se o nome for vazio, retorna
        }
        try {
            Categoria c = arqCategorias.read(nome); // Lê a categoria do arquivo
            System.out.println(c.getNome());       // Exibe a categoria encontrada

            int id_categoria = c.getId();
            ArrayList<Tarefa> t = arqTarefas.readAll(id_categoria);

            if (t.isEmpty()) {
                System.out.println("Nao existem tarefas nesta categoria!");
            } else {
                for (int i = 0; i < t.size(); i++) {
                    System.out.println((i + 1) + ") " + t.get(i).getNome());
                }
            }
        } catch (Exception e) {
            System.out.println("Categoria nao encontrada!"); // Categoria não encontrada
        }
    }

    public ArrayList<Tarefa> listarTarefas(String termo) throws Exception {
        ArrayList<Tarefa> tarefas = null;
        try {
            int numeroTarefa = 1;
            termo = termo.toLowerCase();
            tarefas = arqTarefas.listar(termo);
            for (Tarefa tmp : tarefas) {
                System.out.print("\nN° Tarefa.....: " + numeroTarefa);
                System.out.println(tmp);
                numeroTarefa++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tarefas;
    }

    public void listarTarefasPorTermo() throws Exception {

        try {
            int numeroTarefa = 1;
            System.out.print("Digite o termo que deseja buscar no banco de tarefas: ");
            String titulo = sc.nextLine();
            titulo = titulo.toLowerCase();

            ArrayList<Tarefa> tarefas = arqTarefas.listar(titulo);

            if (tarefas.isEmpty()) {
                System.err.println("Termo nao encontrado");
                return;
            }

            for (Tarefa tmp : tarefas) {
                System.out.print("\nN° Tarefa.....: " + numeroTarefa);
                System.out.println(tmp);
                numeroTarefa++;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void atualizarRotulo() throws Exception {
        String termo;
        int numeroTarefa = -1;
        Tarefa old = new Tarefa();
        ArrayList<Tarefa> tarefas = null;

        try {

            while (tarefas == null || tarefas.isEmpty()) {
                System.out.print("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                tarefas = listarTarefas(termo);
                System.out.println("Tarefas: " + tarefas.size());
                if (tarefas == null || tarefas.isEmpty()) {
                    System.out.println("Tarefas nao encontradas");
                    return;
                }
            }
            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o ID DA TAREFA que deseja atualizar (0 para cancelar)");
                numeroTarefa = sc.nextInt();
                if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                    System.out.println("Tarefa não encontrada, tente novamente");
                } else if (numeroTarefa == 0) {
                    return;
                }
            }
            old = tarefas.get(numeroTarefa - 1);
            System.out.println("Tarefa Selecionada: " + old.getNome());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Definindo Rotulos
        int newRotulo = 1;
        ArrayList<Rotulo> Rotulos = new ArrayList<>();
        ArrayList<Integer> posRotulosLista = new ArrayList<>();

        System.out.println("Deseja remover Rotulos ? (1 para sim, 0 para não)");
        newRotulo = sc.nextInt();
        while (newRotulo == 1) {
            System.out.println("Digite o índice da Rotulo que deseja remover dessa tarefa");
            System.out.println();
            Rotulos = arqRotulos.listar();

            System.out.println();
            posRotulosLista.add(sc.nextInt() - 1);

            System.out.println("Deseja remover mais Rotulos? (1 para sim, 0 para não)");
            newRotulo = sc.nextInt();
        }

        ArrayList<Integer> removed = new ArrayList<>();
        for (int i = 0; i < posRotulosLista.size(); i++) {
            removed.add(Rotulos.get(posRotulosLista.get(i)).getId());
        }
        posRotulosLista.clear();
        System.out.println("Deseja adicionar Rotulos ? (1 para sim, 0 para não)");

        newRotulo = sc.nextInt();
        while (newRotulo == 1) {
            System.out.println("Digite o índice da Rotulo que deseja adicionar dessa tarefa");
            System.out.println();

            Rotulos = arqRotulos.listar();
            System.out.println();
            posRotulosLista.add(sc.nextInt() - 1);

            System.out.println("Deseja adicionar mais Rotulos? (1 para sim, 0 para não)");
            newRotulo = sc.nextInt();
        }
        ArrayList<Integer> added = new ArrayList<>();
        for (int i = 0; i < posRotulosLista.size(); i++) {
            added.add(Rotulos.get(posRotulosLista.get(i)).getId());
        }

        if (arqTarefas.updateRotulos(old, removed, added)) {
            System.out.println("Rotulos atualizadas com sucesso");
        } else {
            System.out.println("Erro ao atualizar as Rotulos");
        }
    }

    // Método para listar os status disponíveis
    private static void listaStatus() {
        System.out.println("\nEscolha o status:"
                + "\n0 - Pendente"
                + "\n1 - Em Progresso"
                + "\n2 - Concluída"
                + "\nStatus: ");
    }

    // Método para listar as prioridades disponíveis
    private static void listaPrioridades() {
        System.out.println("\nEscolha a prioridade:"
                + "\n0 - Baixa"
                + "\n1 - Média"
                + "\n2 - Alta"
                + "\n3 - Urgente"
                + "\nOpção: ");
    }
}
