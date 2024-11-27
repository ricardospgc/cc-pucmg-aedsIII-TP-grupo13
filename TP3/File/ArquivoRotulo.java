package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoRotulo extends Arquivo<Rotulo> {
    ArvoreBMais<ParRotuloId> indiceRotulos;

    /* 
     * Construtor que inicializa o arquivo de rótulos e a estrutura de índice B+ associada.
     */
    public ArquivoRotulo() throws Exception {
        super("Rotulos", Rotulo.class.getConstructor());
        try {
            indiceRotulos = new ArvoreBMais<>(
                ParRotuloId.class.getConstructor(),
                5,
                "./BaseDeDados/ArvoresRotulos.db"
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception();
        }
    }

    /* 
     * Método público para criar um novo rótulo. Retorna o ID do rótulo criado.
     */
    public int create(String nomeRotulo) throws Exception {
        Rotulo rotulo = new Rotulo(nomeRotulo);
        return this.createInterno(rotulo);
    }

    /* 
     * Método privado que realiza a criação do rótulo no arquivo base e atualiza o índice.
     */
    private int createInterno(Rotulo rotulo) throws Exception {
        int id = super.create(rotulo); // Cria o rótulo no arquivo base e obtém o ID
        rotulo.setId(id); // Define o ID no objeto rótulo
        try {
            indiceRotulos.create(new ParRotuloId(rotulo.getNome(), rotulo.getId())); // Adiciona o rótulo ao índice
        } catch (Exception ex) {
            System.out.println("Erro ao criar o rótulo.");
            System.out.println(ex.getMessage());
        }
        return id;
    }

    /* 
     * Método para ler tarefas associadas a um rótulo. Retorna uma lista de tarefas.
     */
    public ArrayList<Tarefa> readTarefasPorRotulo(String nomeRotulo) throws Exception {
        ArrayList<Tarefa> tarefasEncontradas = new ArrayList<>();
        ArquivoTarefa arquivoTarefa = new ArquivoTarefa();
        try {
            ArrayList<ParRotuloId> rotulo = indiceRotulos.read(new ParRotuloId(nomeRotulo));
            if (rotulo.isEmpty()) { // Verifica se o rótulo existe
                throw new Exception("Rótulo inexistente.");
            }
            tarefasEncontradas = arquivoTarefa.read(rotulo.get(0)); // Lê as tarefas associadas ao rótulo
        } catch (Exception ex) {
            System.out.println("Erro ao ler tarefas associadas ao rótulo.");
            System.out.println(ex.getMessage());
        }
        return tarefasEncontradas;
    }

    /* 
     * Método para atualizar o nome de um rótulo. Retorna true se a atualização for bem-sucedida.
     */
    public boolean update(String nomeAntigo, String nomeNovo) throws Exception {
        Rotulo novoRotulo = new Rotulo(nomeNovo);
        try {
            ArrayList<ParRotuloId> rotulo = indiceRotulos.read(new ParRotuloId(nomeAntigo));
            if (rotulo.isEmpty()) { // Verifica se o rótulo existe
                throw new Exception("Rótulo inexistente.");
            }
            novoRotulo.setId(rotulo.get(0).getId()); // Define o ID no rótulo atualizado
            if (super.update(novoRotulo)) { // Atualiza o rótulo no arquivo base
                System.out.println("Atualização realizada com sucesso.");
            }
            indiceRotulos.delete(rotulo.get(0)); // Remove o rótulo antigo do índice
            indiceRotulos.create(new ParRotuloId(novoRotulo.getNome(), novoRotulo.getId())); // Adiciona o novo rótulo
        } catch (Exception ex) {
            System.out.println("Erro ao atualizar o rótulo.");
            System.out.println(ex.getMessage());
        }
        return true;
    }

    /* 
     * Método para excluir um rótulo. Retorna true se a exclusão for bem-sucedida.
     */
    public boolean delete(String nomeRotulo) throws Exception {
        try {
            ArrayList<ParRotuloId> rotulo = indiceRotulos.read(new ParRotuloId(nomeRotulo));
            if (rotulo.isEmpty()) { // Verifica se o rótulo existe
                throw new Exception("Rótulo inexistente.");
            }
            ArquivoTarefa arquivoTarefa = new ArquivoTarefa();
            ArrayList<Tarefa> tarefasAssociadas = arquivoTarefa.read(rotulo.get(0));
            if (!tarefasAssociadas.isEmpty()) { // Verifica se existem tarefas associadas ao rótulo
                throw new Exception("Existem tarefas associadas a este rótulo.");
            }
            return super.delete(rotulo.get(0).getId()) ? indiceRotulos.delete(rotulo.get(0)) : false; // Remove do arquivo e do índice
        } catch (Exception ex) {
            System.out.println("Erro ao excluir o rótulo.");
            System.out.println(ex.getMessage());
        }
        return false;
    }

    /* 
     * Método para listar todos os rótulos armazenados. Retorna uma lista de rótulos.
     */
    public ArrayList<Rotulo> listar() throws Exception {
        ArrayList<Rotulo> listaRotulos = new ArrayList<>();
        try {
            listaRotulos = super.readAll(); // Lê todos os rótulos no arquivo base
            if (listaRotulos.isEmpty()) { // Verifica se existem rótulos
                throw new Exception("Nenhum rótulo foi criado.");
            }
            for (int i = 0; i < listaRotulos.size(); i++) { // Exibe os rótulos encontrados
                System.out.println(listaRotulos.get(i).getId() + ") " + listaRotulos.get(i).getNome());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return listaRotulos;
    }
}
