# cc-pucmg-aedsIII-TP-grupo13
Esse é o repositório do Grupo 13 para os Trabalhos Práticos da disciplina de Algoritmos e Estruturas de Dados da PUC Minas, realizado no segundo semestre de 2024.

Integrantes:
  - Ricardo Soares Pereira da Gama Cerqueira  - Matrícula: 803833
  - Túlio Gomes Braga                         - Matrícula: 802512
  - Yago Almeida Melo                         - Matrícula: 806454

---
## TP1

### Relatório
"Descrevam um pouco o esforço. Uma descrição do seu trabalho nas suas próprias palavras. 
Basicamente, vocês devem responder à seguinte pergunta: O que o trabalho de vocês faz?"

> O nosso trabalho implementa um CRUD genérico que armazena os dados em um arquivo de base de dados, com pesquisa através de hash extensível de índice direto.

> O CRUD realiza as 4 operações (inclusão, leitura, atualização e remoção) de forma genérica, ou seja, independente da entidade. Nesse trabalho, a entidade escolhida foram Tarefas, que possuem como atributos Id (somente para operações e inalterável), Nome, Data de Criação e Conclusão, Status e Prioridade.

> O arquivo de base de dados possui um cabeçalho que indica a quantidade de registros armazenados. Cada registro possui um byte de lápide, indicando se é válido ou foi removido, um short indicando o seu tamanho e o vetor de bytes que descrevem a entidade.

> A hash extensível direta relaciona o Id da entidade com o endereço do registro correspondente na base de dados, deixando mais eficiente operações de pesquisa.

### Descrição das Classes e Métodos
"Descrevam todas as classes criadas e os seus métodos"

### Experiência de Desenvolvimento
"Finalmente, relatem um pouco a experiência de vocês, explicando questões como: Vocês implementaram todos os requisitos? Houve alguma operação mais difícil? 
Vocês enfrentaram algum desafio na implementação? Os resultados foram alcançados?
A ideia, portanto, é relatar como foi a experiência de desenvolvimento do TP. Aqui, a ideia é entender como foi para vocês desenvolver este TP."

> A nossa experiência de desenvolvimento foi bem positiva, sem grandes complicações. Todos os requisitos foram implementados corretamente, sendo a implementação da hash extensível o ponto de maior dificuldade. Entretanto, com o apoio dos materiais e esforço do grupo, conseguimos alcançar os resultados.

> Para facilitar a cooperação, usamos muito a extensão Live Share do Visual Studio Code, que permite aos participantes acompanharem as mudanças em tempo real.

### Perguntas finais

- O trabalho possui um índice direto implementado com a tabela hash extensível?
  > Sim.
- A operação de inclusão insere um novo registro no fim do arquivo e no índice e retorna o ID desse registro?
  > Sim.
- A operação de busca retorna os dados do registro, após localizá-lo por meio do índice direto?
  > Sim.
- A operação de alteração altera os dados do registro e trata corretamente as reduções e aumentos no espaço do registro?
  > Sim.
- A operação de exclusão marca o registro como excluído e o remove do índice direto?
  > Sim.
- O trabalho está funcionando corretamente?
  > Sim.
- O trabalho está completo?
  > Sim.
- O trabalho é original e não a cópia de um trabalho de outro grupo?
  > Sim.
---
## TP2
---
## TP3
---
## TP4
