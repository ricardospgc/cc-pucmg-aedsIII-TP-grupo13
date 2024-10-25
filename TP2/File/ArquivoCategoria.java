package File;

import Entidades.Categoria;
import java.util.ArrayList;
import java.util.Scanner;


public class ArquivoCategoria extends Arquivo<Categoria> {
    ArvoreBMais<ParNomeId> indiceIndiretoNome;
    protected Scanner sc = new Scanner(System.in);

    public ArquivoCategoria() throws Exception {
        super(Categoria.class.getConstructor(),"Categorias.db");
        indiceIndiretoNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "BaseDeDados//IndiceIndiretoNome.btree.db");
    } 

    @Override
    public int create(Categoria obj) throws Exception {
        int id = super.create(obj);
        boolean indiceCriado = indiceIndiretoNome.create(new ParNomeId(obj.getNome(), id));
        if(!indiceCriado) {System.err.println("ERRO AO CRIAR INDICE");}
        return id;
    } 

    public void buscaCategorias() throws Exception {
        ArrayList<ParNomeId> list = indiceIndiretoNome.read(null);
        System.out.print("Digite o nome da categoria buscada: ");
        String nome = sc.nextLine();
        boolean encontrou = false;
        for (ParNomeId item : list) {
            if (nome.equals(item.nome) && !encontrou) {
                System.out.println("Categoria encontrada: " + item.nome);
                encontrou = true;
            }
        }
        if(!encontrou) { System.out.println("Categoria não encontrada"); }
    } 

    public int leCategoriaPorNome() throws Exception {
        int idCategoria = 0;
        ArrayList<ParNomeId> list = indiceIndiretoNome.read(null);

        for (ParNomeId item : list) {
            
            System.out.println(item.nome);
        }
        System.out.print("\nDigite o nome da categoria que sera atribuida a alteracao: ");
        String nome = sc.nextLine();
        for (ParNomeId item : list) {
            if (nome.equals(item.nome)) {
                idCategoria = item.id;
                break; 
            }
        }
        return idCategoria;  
    }

    public void mostraCategorias() throws Exception {
        ArrayList<ParNomeId> list = indiceIndiretoNome.read(null);

        System.out.println("\n TODAS AS CATEGORIAS \n" );
        for (ParNomeId item : list) {
            System.out.println(" -> " + item.nome);
        }
        System.out.println();
    }
    
    
    public void delete() throws Exception {
        int idCategoria;
        String nomeCategoria = "";
        System.out.println("Você realmente deseja apagar alguma categoria? (S/N) ");
        char caracter = sc.nextLine().trim().toUpperCase().charAt(0);
        if (caracter == 'S') {
            idCategoria = leCategoriaPorNome();
            ArquivoTarefa arqTarefa = new ArquivoTarefa();
            int quant = arqTarefa.tarefasPorCategoria(idCategoria);

            if (quant != 0) {
                System.out.println("Não é possivel apagar a categoria, pois ela possui tarefas associadas");
            } else {
                boolean resp = super.delete(idCategoria);
                System.out.println("RESP = " + resp);
                if (resp == true) {
                    ArrayList<ParNomeId> ict = indiceIndiretoNome.read(null);
                    for (int i = 0; i < ict.size(); i++) {
                        if (ict.get(i).id == idCategoria) {
                            System.out.println("Categoria: " + nomeCategoria);
                        }
                    }

                    resp = indiceIndiretoNome.delete(new ParNomeId(nomeCategoria, -1));
                    if (resp == true) {
                        System.out.println("Categoria apagada com sucesso");
                    } else {
                        System.out.println("ERRO ao apagar a categoria");
                    }
                }
            }
        }
    }

    public void update() throws Exception {
        int idCategoria;
        Categoria C;
        boolean resp;
        System.out.println("Você deseja alterar alguma categoria? (S/N): ");
        char caracter = sc.nextLine().charAt(0);
        if (caracter == 'S' || caracter == 's') {
            idCategoria = leCategoriaPorNome();
            System.err.println("ID CATEGORIA = " + idCategoria);
            C = super.read(idCategoria);

            if (C != null) {
                System.out.println("Categoria atual: " + C.nome);
                System.out.print("Digite o novo nome da categoria: ");
                String novoNome = sc.nextLine();
                String nome = C.nome;

                C.nome = novoNome;
                System.out.println(C.toString());

                resp = super.update(C);
                indiceIndiretoNome.delete(new ParNomeId(nome, idCategoria));
                indiceIndiretoNome.create(new ParNomeId(novoNome, idCategoria));

                if (resp == true) {
                    System.out.println("atualizado com sucesso");
                } else {
                    System.out.println("Erro ao atualizar");
                }
            }
        }
    }

    public boolean buscarCategoriaNome(String nomeCategoria) throws Exception {
        arquivo.seek(CABECALHO); 
        byte lapide;
        short tam;
        byte[] b;
        Categoria c;

        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            tam = arquivo.readShort();
            b = new byte[tam];
            arquivo.read(b);
            if (lapide != '*') {
                c = new Categoria();
                c.fromByteArray(b);
                if (c.getNome().equals(nomeCategoria)) {
                    return true;
                }
            }
        }
        return false;
    }
} 