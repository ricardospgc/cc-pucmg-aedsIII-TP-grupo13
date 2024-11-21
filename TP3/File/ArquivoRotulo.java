package File;

import Entidades.*;
import java.io.File;

import java.util.ArrayList;

public class ArquivoRotulo extends Arquivo<Rotulo> {
    ArvoreBMais<ParRotuloId> arvoreB;

    /* Criando o Arquivo de Rotulo */
    public ArquivoRotulo()throws Exception{
        super(Rotulo.class.getConstructor(), "ArquivoRotulo");
        try{
            arvoreB = new ArvoreBMais<>(ParRotuloId.class.getConstructor(), 5, "./BaseDeDado/ArvoresRotulos");
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new Exception();
        }
    }

    /* CRUD DE Rotulo */

    /* Método Publico para a criação de Rotulo. Retorna a Rotulo criada */
    public int create(String nomeRotulo)throws Exception{
        Rotulo Rotulo = new Rotulo(nomeRotulo);
        return this.create1(Rotulo);
    }
    
    /* Método Privado da criação de Rotulo. Retorna o ID da Rotulo */
    private int create1(Rotulo Rotulo) throws Exception{
        int id = super.create(Rotulo);
        Rotulo.setId(id);
        try{
            arvoreB.create(new ParRotuloId(Rotulo.getNome(),Rotulo.getId()));
        }catch(Exception e){
            System.out.println("Erro na criação de uma nova Rotulo");
            System.out.println(e.getMessage());
        }
        return id;
    } 
    
    /* Método de leitura listando as Tarefas da Rotulo passada como parametro. Retorna as Tarefas */
    public ArrayList<Tarefa> read(String nomeRotulo)throws Exception{
        ArrayList<Tarefa> t = new ArrayList<>();
        ArquivoTarefa tarefas = new ArquivoTarefa();
        try{
            ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
        
            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo inxistente");
            }
            t = tarefas.read(Rotulo.get(0));   
        }catch(Exception e){
            System.out.println("Erro na leitura do Arquivo");
            System.out.println(e.getMessage());
        }
        return t;
    }

    /* Método de atualização do nome de uma Rotulo. Retornando se foi feito com Sucesso ou Não. */
    public boolean update(String nomeRotulo, String novaRotulo)throws Exception{
        Rotulo eti = new Rotulo(novaRotulo);
        
        try{
            ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo Inexistente");
            }
            eti.setId(Rotulo.get(0).getId());
            
            if(super.update(eti)){
                System.out.println("Atualizo");
            }

            
            arvoreB.delete(Rotulo.get(0));
            arvoreB.create(new ParRotuloId(eti.getNome(), eti.getId()));
        }
        catch (Exception e){
            System.out.println("Erro no update do Arquivo");
            System.out.println(e.getMessage());
        }
        
        return true;
    }

    /* Método de Deletar Rotulo. Procura pelo nome da Rotulo e a deleta. Retorna booleano */
    public boolean delete(String nomeRotulo) throws Exception{
        try{
            ArrayList<ParRotuloId> eti = arvoreB.read(new ParRotuloId(nomeRotulo));

            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(eti.isEmpty()){
                throw new Exception("Rotulo Inesistente");
            }

            ArquivoTarefa tarefas = new ArquivoTarefa();
            ArrayList<Tarefa> t = tarefas.read(eti.get(0));
    
            if(!t.isEmpty())
                throw new Exception("Tarefas existentes dentro desta Rotulo");
            
            return super.delete(eti.get(0).getId()) ? arvoreB.delete(eti.get(0)) : false;
        }catch(Exception e){
            System.out.println("Erro em deletar");
            System.out.println(e.getMessage());
        }
        return false;
    }

    /* Listando as Rotulo */
    public ArrayList<Rotulo> listar() throws Exception{
        ArrayList<Rotulo> Rotulos = new ArrayList<>();
        try{
             Rotulos = super.list();

            if(Rotulos.isEmpty())
                throw new Exception("Rotulo ainda não foram criadas");
            
            for(int i = 0; i<Rotulos.size(); i++){
                System.out.println("Indice: " + Rotulos.get(i).getId() + " Nome da Rotulo: " + Rotulos.get(i).getNome());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Rotulos;
    }

}