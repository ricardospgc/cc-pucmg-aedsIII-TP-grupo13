import java.io.File;

public class Principal {
    public static void main(String[] args) {

        
        try {
            new File(".//BaseDeDados//tarefas.db").delete();
            Tarefa tf1 = new Tarefa("teste", Status.PENDENTE, Prioridade.BAIXA);
            Tarefa tf2 = new Tarefa("teste22", Status.CONCLUIDO, Prioridade.ALTA);

            Arquivo<Tarefa> arqTarefa = new Arquivo<>(Tarefa.class.getConstructor(), "tarefas.db");
            
            int id1 = arqTarefa.create(tf1);
            System.out.println(arqTarefa.read(id1));

            int id2 = arqTarefa.create(tf2);
            System.out.println(arqTarefa.read(id2));

            System.out.println("\n-------");

            /* UPDATE DANDO ERRRO! Objeto printa null, pode ser erro de leitura!
             * Erro acontece tanto com nome maior, igual ou menor.
             * Nome maior cria um registro mal feito no fim
            */
            Tarefa tf3 = new Tarefa("testandoAAAA", Status.EM_PROGRESSO, Prioridade.URGENTE);
            boolean hasUpdated = arqTarefa.update(id1, tf3);

            if(hasUpdated){
                System.out.println("\nUpdate bem sucedido!");
                System.out.println(arqTarefa.read(id1));
            } else {
                System.out.println("Erro ao atualizar");
            }

            System.out.println(arqTarefa.read(id1));

            boolean hasDeleted = arqTarefa.delete(id2);
            if(hasDeleted){
                System.out.println("\nDelete bem sucedido!");
            } else {
                System.out.println("Erro ao deletar");
            }

                
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
