package Main;

import Entidades.Tarefa;
import Enums.Prioridade;
import Enums.Status;
import File.Arquivo;

public class Principal {

    public static void main(String[] args) {
        try {
            //Criando os Objetos de Tarefa...
            Tarefa tf1 = new Tarefa("Trabalho Interdisciplinar", Status.CONCLUIDO, Prioridade.BAIXA);
            Tarefa tf2 = new Tarefa("TP 1000 - AEDS III", Status.PENDENTE, Prioridade.URGENTE);
            Tarefa tf3 = new Tarefa("Estudar Cálculo II", Status.EM_PROGRESSO, Prioridade.ALTA);
            //Abrindo o arquivo de tarefas
            Arquivo<Tarefa> arqTarefa = new Arquivo<>(Tarefa.class.getConstructor(), "tarefas.db");
            //Inserção dos registros 
            int id1 = arqTarefa.create(tf1);
            int id2 = arqTarefa.create(tf2);
            int id3 = arqTarefa.create(tf3);
            //Leitura de todos os registros 
            System.out.println(arqTarefa.read(id1));
            System.out.println(arqTarefa.read(id2));
            System.out.println(arqTarefa.read(id3));

            System.out.println("\n-------");
            //Atualiza uma tarefa para um tamanho menor e exibe o resultado
            tf1.setNome("Prova: Banco de Dados");
            tf1.setPrioridade(Prioridade.URGENTE);
            tf1.setStatus(Status.EM_PROGRESSO);
            boolean hasUpdated = arqTarefa.update(tf1);
            if (hasUpdated) {
                System.out.println("\nUpdate bem sucedido na Tarefa de Id " + id1 + "!");
                System.out.println(arqTarefa.read(id1));
            } else {
                System.out.println("Erro ao atualizar");
            }

            System.out.println("\n-------");
            //Atualiza uma tarefa para um tamanho maior e exibe o resultado
            tf3.setNome("Lista de Arquitetura de Computadores");
            tf3.setPrioridade(Prioridade.ALTA);
            tf3.setStatus(Status.CONCLUIDO);
            hasUpdated = arqTarefa.update(tf3);
            if (hasUpdated) {
                System.out.println("\nUpdate bem sucedido na Tarefa de Id " + id3 + "!");
                System.out.println(arqTarefa.read(id3));
            } else {
                System.out.println("Erro ao atualizar");
            }

            System.out.println("\n-------");
            //Operação de deletar uma Tarefa
            boolean hasDeleted = arqTarefa.delete(id2);
            if (hasDeleted) {
                System.out.println("\nDelete bem sucedido!");
            } else {
                System.out.println("Erro ao deletar");
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
}// Principal()
