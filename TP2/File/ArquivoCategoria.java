package File;

import Entidades.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoCategoria extends Arquivo<Categoria> {

    Arquivo<Categoria> arqCategoria; // Referência para o arquivo de categorias
    ArvoreBMais<ParNomeId> indiceIndiretoNome; // Índice indireto para acessar categorias pelo nome

    // Construtor da classe ArquivoCategoria
    public ArquivoCategoria() throws Exception {
        super("Categorias", Categoria.class.getConstructor());
        // Inicializa o índice indireto utilizando uma Árvore B+
        indiceIndiretoNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5,
                "./BaseDeDados/indiceIndiretoNome.btree.db");
    }

    // Sobrescreve o método create para incluir a inserção no índice indireto
    @Override
    public int create(Categoria c) throws Exception {
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeId(c.getNome(), id)); // Atualiza o índice indireto
        return id;
    }

    // Método para ler um registro a partir do nome
    public Categoria read(String n) throws Exception {
        // Busca no índice indireto a lista de pares com o nome fornecido
        ArrayList<ParNomeId> p = indiceIndiretoNome.read(new ParNomeId(n, -1));
        // Retorna a categoria com o ID encontrado
        return super.read(p.get(0).getId());
    }

    // Método para excluir um registro a partir do nome
    public boolean delete(String n) throws Exception {
        return delete(read(n).getId()); // Exclui a categoria a partir do ID encontrado
    }

    // Sobrescreve o método delete para excluir também do índice indireto
    public boolean delete(int id) throws Exception {
        boolean result = false;
        Categoria obj = super.read(id);
        if (obj != null) {
            // Exclui do índice indireto e marca o registro como excluído no arquivo
            if (indiceIndiretoNome.delete(new ParNomeId(obj.getNome(), obj.getId()))) {
                result = super.delete(obj.getId());
            }
        }
        return result;
    }

    // Método para listar todas as categorias
    public void list() {
        try {
            System.out.println();
            indiceIndiretoNome.show(); // Mostra os registros do índice indireto
        } catch (Exception e) {
            System.err.println("Erro no sistema");
        }
    }

    // Sobrescreve o método update para atualizar também o índice indireto
    @Override
    public boolean update(Categoria novaCategoria) throws Exception {
        Categoria categoriaVelha = read(novaCategoria.getId());
        if (super.update(novaCategoria)) {
            if (novaCategoria.getId() == categoriaVelha.getId()) {
                // Atualiza o índice indireto, removendo o antigo e inserindo o novo
                indiceIndiretoNome.delete(new ParNomeId(categoriaVelha.getNome(), categoriaVelha.getId()));
                indiceIndiretoNome.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId()));
            }
            return true;
        }
        return false;
    }

    // lê todas as categorias e armazena em uma lista
    public List<Categoria> readAll() throws Exception {
        List<Categoria> categorias = new ArrayList<>();

        arquivo.seek(TAM_CABECALHO);
        byte lapide = ' ';
        short tam = 0;
        byte[] b = null;

        Categoria c = null;

        // Lê até o final do arquivo
        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            b = new byte[tam];
            arquivo.read(b);

            if (lapide != '*') {
                c = new Categoria();
                c.fromByteArray(b);
                categorias.add(c);
            }
        }

        return categorias;
    }
}
