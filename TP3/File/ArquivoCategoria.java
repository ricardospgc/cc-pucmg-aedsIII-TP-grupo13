package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoCategoria extends Arquivo<Categoria> {
    ArvoreBMais<ParNomeId> indiceNomeId; // Índice B+ para armazenar associações entre nome da categoria e seu ID

    /* 
     * Construtor para inicializar o arquivo de categorias e o índice B+ associado.
     */
    public ArquivoCategoria() throws Exception {
        super("Categorias", Categoria.class.getConstructor());
        indiceNomeId = new ArvoreBMais<>(
                ParNomeId.class.getConstructor(),
                5,
                "./BaseDeDados/indiceNomeId.btree.db"
        );
    }

    /* 
     * Método para criar uma nova categoria e atualizar o índice associado.
     */
    @Override
    public int create(Categoria categoria) throws Exception {
        int id = super.create(categoria); // Cria a categoria no arquivo e obtém o ID
        indiceNomeId.create(new ParNomeId(categoria.getNome(), id)); // Adiciona o nome e ID ao índice
        return id;
    }

    /* 
     * Método para ler uma categoria com base no nome dela.
     */
    public Categoria read(String nomeCategoria) throws Exception {
        ArrayList<ParNomeId> pares = indiceNomeId.read(new ParNomeId(nomeCategoria, -1)); // Busca o par no índice
        return super.read(pares.get(0).getId()); // Obtém a categoria usando o ID encontrado
    }

    /* 
     * Método para excluir uma categoria usando o nome como referência.
     */
    public boolean delete(String nomeCategoria) throws Exception {
        return delete(read(nomeCategoria).getId()); // Encontra o ID da categoria e a remove
    }

    /* 
     * Método para excluir uma categoria usando o ID.
     */
    @Override
    public boolean delete(int idCategoria) throws Exception {
        boolean sucesso = false;
        Categoria categoria = super.read(idCategoria); // Lê os dados da categoria
        if (categoria != null) {
            if (indiceNomeId.delete(new ParNomeId(categoria.getNome(), categoria.getId()))) // Remove do índice
            {
                sucesso = super.delete(categoria.getId()); // Remove do arquivo
            }
        }
        return sucesso;
    }

    /* 
     * Método para listar todas as categorias armazenadas.
     */
    public void listarCategorias() {
        ArrayList<Categoria> listaCategorias = new ArrayList<>();
        listaCategorias = super.readAll();

        if (listaCategorias.isEmpty()) {
            System.out.println("Não existem categorias criadas.");
        } else {
            System.out.println("\nCategorias criadas:");

            for (int i = 0; i < listaCategorias.size(); i++) {
                System.out.println((i + 1) + ") " + listaCategorias.get(i).getNome());
            }
        }
    }

    /* 
     * Método para atualizar uma categoria e ajustar o índice caso o nome seja alterado.
     */
    @Override
    public boolean update(Categoria novaCategoria) throws Exception {
        Categoria categoriaAtual = read(novaCategoria.getId()); // Obtém a categoria antes da atualização
        if (super.update(novaCategoria)) // Atualiza os dados no arquivo
        {
            if (novaCategoria.getId() == categoriaAtual.getId()) // Se o ID não mudou
            {
                indiceNomeId.delete(new ParNomeId(categoriaAtual.getNome(), categoriaAtual.getId())); // Remove a entrada antiga
                indiceNomeId.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId())); // Adiciona a nova entrada
            }
            return true;
        }
        return false;
    }
}
