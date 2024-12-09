# cc-pucmg-aedsIII-TP-grupo13
Esse é o repositório do Grupo 13 para os Trabalhos Práticos da disciplina de Algoritmos e Estruturas de Dados III da PUC Minas, realizado no segundo semestre de 2024.

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

> O CRUD realiza as 4 operações (inclusão, leitura, atualização e remoção) de forma genérica, podendo manipular qualquer entidade que implemente a interface Registro. Nesse trabalho, a entidade escolhida foram Tarefas, que possuem como atributos Id (somente para operações e inalterável), Nome, Data de Criação e Conclusão, Status e Prioridade.

> O arquivo de base de dados possui um cabeçalho que indica a quantidade de registros armazenados. Cada registro possui um byte de lápide, indicando se é válido ou foi removido, um short indicando o seu tamanho e o vetor de bytes que descrevem a entidade.

> A hash extensível direta relaciona o Id da entidade com o endereço do registro correspondente na base de dados, deixando mais eficiente operações de pesquisa.

---

### Descrição das Classes e Métodos

### Classes:
### Principal
A classe `Principal` gerencia todo o fluxo do programa. Ela possui apenas o método **`public static void main(String[] args)`**, que realiza testes de todos os casos no CRUD;

---

### Arquivo

A classe `Arquivo` gerencia a manipulação de objetos genéricos `T` (que devem estender `Registro`) em um arquivo. Ela é responsável por operações CRUD (Create, Read, Update, Delete) e utiliza uma estrutura de índice para acesso eficiente.

#### Componentes da Classe

1. **Campos**
   - **`String nomeArquivo`**: Nome do arquivo onde os objetos são armazenados.
   - **`RandomAccessFile arq`**: Arquivo de acesso aleatório para leitura e escrita dos registros.
   - **`HashExtensivel<ParIDEndereco> indice`**: Estrutura de hash extensível para mapear IDs de objetos para endereços no arquivo.
   - **`Constructor<T> construtor`**: Construtor para instanciar objetos do tipo `T`.

2. **Construtor**
   - **`public Arquivo(Constructor<T> construtor, String nomeArquivo)`**: Inicializa o arquivo, recria o diretório de armazenamento, e prepara o índice para o gerenciamento de objetos. Se o diretório "BaseDeDados" já existir, ele é deletado e recriado.

3. **Métodos**
   - **`private void deleteDirectory(File directory)`**: Remove um diretório e seu conteúdo de forma recursiva.
     
   - **`public int create(T objeto) throws Exception`**: Adiciona um novo objeto ao arquivo. Atualiza o ID do objeto, escreve o objeto no arquivo, e adiciona uma entrada no índice para mapeamento de ID para endereço no arquivo. Retorna o ID atribuído ao objeto.

   - **`public T read(int id) throws Exception`**: Lê um objeto do arquivo com base no seu ID. Utiliza o índice para encontrar o endereço do objeto no arquivo e retorna o objeto correspondente, ou `null` se não encontrado.

   - **`public boolean update(T novoObjeto) throws Exception`**: Atualiza um objeto existente no arquivo. Se o novo objeto tiver um tamanho maior, marca o antigo como excluído e adiciona o novo objeto ao final do arquivo, atualizando o índice.

   - **`public boolean delete(int id) throws Exception`**: Marca um objeto como excluído no arquivo com base no seu ID. A exclusão é feita marcando o registro com um caractere de lapide ('*').

5. **Estrutura do Arquivo**
   - **Cabeçalho**: Contém um inteiro que representa o número de registros (o cabeçalho é de 4 bytes).
   - **Registro**: Cada registro é composto por:
     - **Caractere de lapide**: Um caractere que indica se o registro está excluído.
     - **Tamanho do objeto**: Um valor `short` que indica o tamanho do objeto em bytes.
     - **Objeto**: O próprio objeto `T`.

A classe utiliza um índice de hash extensível (`HashExtensivel<ParIDEndereco>`) para gerenciar mapeamentos entre IDs e endereços no arquivo, o que permite operações de leitura e escrita eficientes.

---

### HashExtensivel
A classe `HashExtensivel<T extends RegistroHashExtensivel<T>>` implementa uma estrutura de hash extensível para gerenciamento de registros em arquivos. A estrutura é projetada para armazenar e recuperar registros e usando diretórios e cestos que podem se expandir dinamicamente conforme necessário.

#### Componentes da Classe

1. **Campos**
   - **`String nomeArquivoDiretorio`**: Nome do arquivo para o diretório.
   - **`String nomeArquivoCestos`**: Nome do arquivo para os cestos.
   - **`RandomAccessFile arqDiretorio`**: Arquivo de acesso aleatório para o diretório.
   - **`RandomAccessFile arqCestos`**: Arquivo de acesso aleatório para os cestos.
   - **`int quantidadeDadosPorCesto`**: Número máximo de elementos que cada cesto pode armazenar.
   - **`Diretorio diretorio`**: Objeto que representa o diretório da estrutura de hash.
   - **`Constructor<T> construtor`**: Construtor para instanciar objetos do tipo `T`.

2. #### **Classe Interna `Cesto`**
   - **Campos**:
     - `short quantidadeMaxima`: Máximo de elementos que o cesto pode conter.
     - `short bytesPorElemento`: Tamanho de cada elemento em bytes.
     - `short bytesPorCesto`: Tamanho total do cesto em bytes.
     - `byte profundidadeLocal`: Profundidade local do cesto.
     - `short quantidade`: Número atual de elementos no cesto.
     - `ArrayList<T> elementos`: Lista de elementos armazenados no cesto.

   - **Construtores**:
     - `public Cesto(Constructor<T> ct, int qtdmax)` e `public Cesto(Constructor<T> ct, int qtdmax, int pl)`: Inicializam um cesto com um construtor de elementos, a quantidade máxima de elementos e a profundidade local opcional.

   - **Métodos**:
     - `public byte[] toByteArray()`: Serializa o cesto em um array de bytes.
     - `public void fromByteArray(byte[] ba)`: Desserializa o cesto a partir de um array de bytes.
     - `public boolean create(T elem)`: Adiciona um elemento ao cesto, se não estiver cheio.
     - `public T read(int chave)`: Busca um elemento no cesto baseado na chave.
     - `public boolean update(T elem)`: Atualiza um elemento no cesto.
     - `public boolean delete(int chave)`: Remove um elemento do cesto.
     - `public boolean empty()`: Verifica se o cesto está vazio.
     - `public boolean full()`: Verifica se o cesto está cheio.
     - `@Override public String toString()`: Retorna uma representação textual do cesto.
     - `public int size()`: Retorna o tamanho do cesto em bytes.

3. #### **Classe Interna `Diretorio`**
   - **Campos**:
     - `byte profundidadeGlobal`: Profundidade global do diretório.
     - `long[] enderecos`: Array de endereços de cestos.

   - **Construtores**:
     - `public Diretorio()`: Inicializa o diretório com profundidade global de 0 e um único endereço.

   - **Métodos**:
     - `public boolean atualizaEndereco(int p, long e)`: Atualiza o endereço de um índice específico.
     - `public byte[] toByteArray()`: Serializa o diretório em um array de bytes.
     - `public void fromByteArray(byte[] ba)`: Desserializa o diretório a partir de um array de bytes.
     - `@Override public String toString()`: Retorna uma representação textual do diretório.
     - `protected long endereco(int p)`: Retorna o endereço para um índice específico.
     - `protected boolean duplica()`: Duplica o diretório, aumentando sua profundidade global.
     - `protected int hash(int chave)`: Calcula o índice do cesto para uma chave.
     - `protected int hash2(int chave, int pl)`: Calcula o índice do cesto para uma chave considerando uma profundidade local específica.

4. **Construtor Principal**
   - **`public HashExtensivel(Constructor<T> ct, int n, String nd, String nc)`**: Inicializa a estrutura de hash extensível com o construtor dos elementos, a quantidade de dados por cesto e os nomes dos arquivos para o diretório e os cestos.

5. **Métodos de Manipulação**
   - **`public boolean create(T elem)`**: Adiciona um novo elemento ao hash. Se necessário, duplica o diretório e redistribui os elementos.
   - **`public T read(int chave)`**: Lê um registro com base na chave.
   - **`public boolean update(T elem)`**: Atualiza um registro existente.
   - **`public boolean delete(int chave)`**: Remove um registro com base na chave.
   - **`public void print()`**: Imprime informações sobre o diretório e os cestos.

A classe `HashExtensivel` é uma implementação de um hash extensível que pode se expandir conforme a quantidade de dados cresce. Isso é alcançado através da duplicação do diretório e redistribuição dos registros entre os cestos quando um cesto se torna cheio.

---

### ParIDEndereco

A classe `ParIDEndereco` representa um par de chave-valor usado em uma estrutura de hash extensível. Ela implementa a interface `RegistroHashExtensivel<ParIDEndereco>`.

#### Componentes da Classe

1. **Campos**
   - **`int id`**: A chave identificadora do par.
   - **`long endereco`**: O valor associado à chave.
   - **`final short TAMANHO`**: O tamanho fixo do registro, em bytes (12 bytes).

2. **Construtores**
   - **`public ParIDEndereco()`**: Construtor padrão que inicializa `id` e `endereco` com `-1`.
   - **`public ParIDEndereco(int id, long endereco)`**: Construtor que inicializa `id` e `endereco` com os valores fornecidos.

3. **Métodos**
   - **`public int getId()`**: Retorna o ID do par.
   - **`public long getEndereco()`**: Retorna o endereço associado ao ID.
   - **`@Override public int hashCode()`**: Retorna o hash code baseado no ID.
   - **`@Override public short size()`**: Retorna o tamanho fixo do registro (12 bytes).
   - **`@Override public String toString()`**: Retorna uma representação em string do par, no formato `(id; endereco)`.
   - **`@Override public byte[] toByteArray()`**: Converte o registro em um array de bytes para persistência.
   - **`@Override public void fromByteArray(byte[] ba)`**: Preenche o registro a partir de um array de bytes.

---

###  Tarefa 

A classe `Tarefa` representa uma tarefa com atributos relacionados ao gerenciamento e status de uma tarefa. Ela implementa a interface `Registro`.

#### Componentes da Classe

1. **Campos**
   - **`int id`**: Identificador único da tarefa.
   - **`String nome`**: Nome da tarefa.
   - **`LocalDateTime dataCriacao`**: Data e hora de criação da tarefa.
   - **`LocalDateTime dataConclusao`**: Data e hora de conclusão da tarefa (se concluída).
   - **`Status status`**: Status atual da tarefa (`PENDENTE`, `CONCLUIDO`, etc.).
   - **`Prioridade prioridade`**: Prioridade da tarefa (`BAIXA`, `MEDIA`, `ALTA`).

2. **Construtores**
   - **`public Tarefa()`**: Construtor padrão que inicializa com valores padrão e status `PENDENTE`.
   - **`public Tarefa(String nome, Status status, Prioridade prioridade)`**: Construtor que inicializa com `nome`, `status`, e `prioridade` com a data de criação atual.
   - **`public Tarefa(String nome, LocalDateTime dataCriacao, Status status, Prioridade prioridade)`**: Construtor completo que permite inicializar todos os campos, incluindo a data de conclusão.

3. **Métodos**
   - **`@Override public void setId(int id)`**: Define o ID da tarefa.
   - **`@Override public int getId()`**: Retorna o ID da tarefa.
   - **`public void setNome(String nome)`**: Define o nome da tarefa.
   - **`public String getNome()`**: Retorna o nome da tarefa.
   - **`public void setDataCriacao(LocalDateTime d)`**: Define a data de criação da tarefa.
   - **`public LocalDateTime getDataCriacao()`**: Retorna a data de criação da tarefa.
   - **`public void setDataConclusao(LocalDateTime d)`**: Define a data de conclusão da tarefa.
   - **`public LocalDateTime getDataConclusao()`**: Retorna a data de conclusão da tarefa.
   - **`public void setStatus(Status s)`**: Define o status da tarefa e atualiza a data de conclusão se o status for `CONCLUIDO`.
   - **`public Status getStatus()`**: Retorna o status da tarefa.
   - **`public void setPrioridade(Prioridade p)`**: Define a prioridade da tarefa.
   - **`public Prioridade getPrioridade()`**: Retorna a prioridade da tarefa.
   - **`@Override public String toString()`**: Retorna uma representação em string da tarefa, com todos os atributos formatados.
   - **`@Override public byte[] toByteArray()`**: Converte a tarefa em um array de bytes para persistência.
   - **`@Override public void fromByteArray(byte[] b)`**: Preenche a tarefa a partir de um array de bytes.
   - **`public long LocalDateTimeToLong(LocalDateTime d)`**: Converte um `LocalDateTime` em `long` (representando milissegundos desde a época UTC).
   - **`public LocalDateTime LongToLocalDateTime(long d)`**: Converte um `long` em `LocalDateTime`.
     
---

### Interface Registro

A interface `Registro` define os métodos obrigatórios para todos os registros que serão armazenados em memória secundária e manipulados pelo nosso CRUD.

#### Métodos
- **`public void setId(int i)`**: Define o identificador único para o registro.
- **`public int getId()`**: Retorna o identificador único do registro.
- **`public byte[] toByteArray() throws IOException`**: Converte o registro em um array de bytes para persistência. Pode lançar uma exceção `IOException` se ocorrer um erro de I/O.
- **`public void fromByteArray(byte[] b) throws IOException`**: Preenche o registro a partir de um array de bytes. Pode lançar uma exceção `IOException` se ocorrer um erro de I/O.

---

### Interface RegistroHashExtensivel<T>

A interface `RegistroHashExtensivel<T>` é uma extensão da interface `Registro` e define métodos adicionais específicos para registros que serão utilizados em uma estrutura de hash extensível.

#### Métodos
- **`@Override public int hashCode()`**: Retorna um código de hash numérico para ser usado como chave no diretório de hash.
- **`public short size()`**: Retorna o tamanho fixo do registro em bytes.
- **`public byte[] toByteArray() throws IOException`**: Converte o registro em um array de bytes para persistência. Pode lançar uma exceção `IOException` se ocorrer um erro de I/O.
- **`public void fromByteArray(byte[] ba) throws IOException`**: Preenche o registro a partir de um array de bytes. Pode lançar uma exceção `IOException` se ocorrer um erro de I/O.

---

### Enum Prioridade

O enum `Prioridade` representa os níveis de prioridade que uma tarefa pode ter.

#### Valores
- **`BAIXA((byte) 0)`**: Prioridade baixa.
- **`MEDIA((byte) 1)`**: Prioridade média.
- **`ALTA((byte) 2)`**: Prioridade alta.
- **`URGENTE((byte) 3)`**: Prioridade urgente.

#### Métodos
- **`public byte getValue()`**: Retorna o valor em byte associado à prioridade.
- **`public static Prioridade fromByte(byte value)`**: Converte um valor byte em um valor correspondente de `Prioridade`. Lança `IllegalArgumentException` se o valor não for válido.

---

### Enum Status

O enum `Status` representa os estados possíveis para uma tarefa.

#### Valores
- **`PENDENTE((byte) 0)`**: Status de tarefa pendente.
- **`EM_PROGRESSO((byte) 1)`**: Status de tarefa em progresso.
- **`CONCLUIDO((byte) 2)`**: Status de tarefa concluída.

#### Métodos
- **`public byte getValue()`**: Retorna o valor em byte associado ao status.
- **`public static Status fromByte(byte value)`**: Converte um valor byte em um valor correspondente de `Status`. Lança `IllegalArgumentException` se o valor não for válido.

---

### Experiência de Desenvolvimento
"Finalmente, relatem um pouco a experiência de vocês, explicando questões como: Vocês implementaram todos os requisitos? Houve alguma operação mais difícil? 
Vocês enfrentaram algum desafio na implementação? Os resultados foram alcançados?
A ideia, portanto, é relatar como foi a experiência de desenvolvimento do TP. Aqui, a ideia é entender como foi para vocês desenvolver este TP."

> A nossa experiência de desenvolvimento foi bem positiva, sem grandes complicações. Todos os requisitos foram implementados corretamente, sendo a implementação da hash extensível o ponto de maior dificuldade. Entretanto, com o apoio dos materiais e esforço do grupo, conseguimos alcançar os resultados.

> Para facilitar a cooperação, usamos muito a extensão Live Share do Visual Studio Code, que permite aos participantes acompanharem as mudanças em tempo real. Com isso, discutimos as dificuldades e diferentes estratégias.

---

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
---

## TP2

Relatório
"Descrevam um pouco o esforço. Uma descrição do seu trabalho nas suas próprias palavras. Basicamente, vocês devem responder à seguinte pergunta: O que o trabalho de vocês faz?"

> O trabalho 2 implementa no CRUD desenvolvido: apresenta menu de opções no console; um índice indireto para as tarefas ChaveID; introduz a entidade categoria, também com um índice indireto NomeId; relaciona as categorias e tarefas com relação 1:N, através de uma árvore B+.

> O menu de opções permite o usuário, através da inserção de comandos no console, realizar as 4 operações do CRUD para Tarefas e Categorias de forma intuitiva.

> O índice indireto organiza as Tarefas e Categorias por ID, facilitando a criação dos pares de ID e as operações pesquisa na base dados.

> A Árvore B+ apoia a implementação do relacionamento 1:N entre Tarefas e Categorias, organizando-as em ordem alfabética.

---

### Descrição das Classes e Métodos (referentes ao TP2)

### Classes:

### Classe Categoria

A classe `Categoria` representa uma categoria de objetos que implementa a interface `Registro`. É responsável por armazenar um identificador único (`id`) e um nome (`nome`) da categoria, além de fornecer métodos para manipulação, conversão e normalização dos dados.

#### Componentes da Classe

1. **Campos**
   - **`int id`**: Identificador único da categoria.
   - **`String nome`**: Nome da categoria.

2. **Construtores**
   - **`public Categoria()`**: Construtor padrão que inicializa o `id` com -1 e o `nome` como uma string vazia.
   - **`public Categoria(String n)`**: Construtor que inicializa o `id` com -1 e define o `nome` de acordo com o parâmetro `n`.
   - **`public Categoria(int i, String n)`**: Construtor que define o `id` e o `nome` de acordo com os parâmetros `i` e `n`, respectivamente.

3. **Métodos**

   - **`public void setId(int id)`**: Define o valor do campo `id`.

   - **`public int getId()`**: Retorna o valor do campo `id`.
     
   - **`public void setNome(String nome)`**: Define o valor do campo `nome`.
     
   - **`public String getNome()`**: Retorna o valor do campo `nome`.
     
   - **`public String toString()`**: Retorna uma representação em string da categoria no formato `ID..: {id}\nNome: {nome}`.
     
   - **`public byte[] toByteArray() throws IOException`**: Converte o objeto `Categoria` em um array de bytes para serialização.
     
   - **`public void fromByteArray(byte[] b) throws IOException`**: Popula o objeto `Categoria` a partir de um array de bytes.
     
   - **`private static String strnormalize(String str)`**: Normaliza uma string removendo acentos e convertendo os caracteres para minúsculas, utilizando a forma de normalização `NFD`.

---

### MenuCategorias

A classe `MenuCategorias` é responsável por gerenciar o menu de categorias, permitindo operações de busca, inclusão, alteração, exclusão e listagem de categorias. Ela utiliza instâncias de `ArquivoTarefa` e `ArquivoCategoria` para manipulação de tarefas e categorias.

#### Componentes da Classe

1. **Campos**
   - **`ArquivoTarefa arqTarefa`**: Objeto para manipulação de tarefas.
   - **`ArquivoCategoria arqCategoria`**: Objeto para manipulação de categorias.
   - **`Scanner sc`**: Utilizado para leitura de entrada do usuário no menu.

2. **Construtor**
   - **`public MenuCategorias()`**: Inicializa os arquivos de categorias e tarefas, lançando exceção caso haja erro na inicialização.

3. **Métodos**
   - **`public void menu()`**: Método principal que exibe o menu de categorias e captura a entrada do usuário, chamando os métodos correspondentes às opções escolhidas (buscar, incluir, alterar, excluir, listar).
     
   - **`public void buscarCategoria()`**: Exibe o submenu para busca de categorias, solicitando o nome da categoria e exibindo a categoria e suas tarefas associadas, caso encontradas.

   - **`public void incluirCategoria()`**: Exibe o submenu para inclusão de uma nova categoria, solicitando o nome e confirmando a inclusão com o usuário antes de adicioná-la ao arquivo de categorias.

   - **`public void alterarCategoria()`**: Permite a alteração de uma categoria existente, solicitando o nome, realizando a busca e atualizando o novo nome informado pelo usuário.

   - **`public void excluirCategoria()`**: Exibe o submenu para exclusão de uma categoria, realizando a verificação de tarefas associadas e confirmando a exclusão com o usuário antes de remover a categoria.

---

### Classe `MenuTarefas`

A classe `MenuTarefas` gerencia a interface de usuário para manipulação de tarefas. Ela permite ao usuário buscar, incluir, alterar e excluir tarefas, além de associá-las a categorias específicas.

#### Componentes da Classe

1. **Campos**
   - **`ArquivoTarefa arqTarefa`**: Objeto responsável pela manipulação dos dados das tarefas.
   - **`ArquivoCategoria arqCategoria`**: Objeto responsável pela manipulação dos dados das categorias.
   - **`Scanner sc`**: Scanner para entrada de dados do usuário.

2. **Construtor**
   - **`public MenuTarefas()`**: Inicializa as instâncias de `ArquivoTarefa` e `ArquivoCategoria`, que representam os arquivos onde as tarefas e categorias são armazenadas.

3. **Métodos**

   - **`public void menu()`**: Exibe o menu principal de opções para tarefas e captura a opção escolhida pelo usuário, chamando o método correspondente de acordo com a escolha.

   - **`public void buscarTarefa()`**: Busca uma tarefa específica por nome e categoria. Exibe a tarefa encontrada e seus detalhes, ou uma mensagem de erro caso a tarefa não exista.

   - **`public void incluirTarefa()`**: Inclui uma nova tarefa. Solicita ao usuário as informações necessárias, como nome, categoria, status, prioridade, e datas de criação e conclusão. Em seguida, cria a tarefa e a armazena no arquivo.

   - **`public void alterarTarefa()`**: Altera uma tarefa existente. Permite ao usuário modificar informações da tarefa, como nome, datas, status, e prioridade, confirmando a atualização ao final do processo.

   - **`public void excluirTarefa()`**: Exclui uma tarefa específica. Solicita a categoria e nome da tarefa, confirmando a exclusão ao final. Exibe uma mensagem de erro caso a tarefa não seja encontrada.

   - **`public void listarPorCategoria()`**: Exibe todas as tarefas de uma categoria. Solicita o ID da categoria, verifica se é válido. Verifica se há tarefas na categoria escolhida.

   - **`private static void listaStatus()`**: Exibe uma lista de status possíveis para as tarefas, permitindo ao usuário selecionar o status desejado.

   - **`private static void listaPrioridades()`**: Exibe uma lista de prioridades possíveis para as tarefas, permitindo ao usuário selecionar a prioridade desejada.

---

### Classe `ArquivoCategoria`

A classe `ArquivoCategoria` gerencia a manipulação dos dados das categorias e implementa um índice indireto para acesso eficiente às categorias pelo nome.

#### Componentes da Classe

1. **Campos**
   - **`Arquivo<Categoria> arqCategoria`**: Referência para o arquivo de categorias.
   - **`ArvoreBMais<ParNomeId> indiceIndiretoNome`**: Índice indireto para acessar categorias pelo nome, implementado como uma Árvore B+.

2. **Construtor**
   - **`public ArquivoCategoria()`**: Inicializa o arquivo para armazenar categorias e configura o índice indireto `indiceIndiretoNome`, utilizando uma Árvore B+ para permitir busca eficiente pelo nome da categoria.

3. **Métodos**

   - **`public int create(Categoria c)`**: Sobrescreve o método `create` para incluir a inserção no índice indireto, permitindo acesso pelo nome da categoria. Após criar a categoria, adiciona uma entrada no índice indireto associando o nome ao ID.
   
   - **`public Categoria read(String n)`**: Realiza a leitura de uma categoria com base no nome fornecido. Utiliza o índice indireto para localizar o ID correspondente ao nome e, em seguida, lê a categoria usando o ID.
   
   - **`public boolean delete(String n)`**: Exclui uma categoria com base no nome, utilizando o índice indireto para encontrar o ID correspondente e, em seguida, chamando o método de exclusão com esse ID.

   - **`public boolean delete(int id)`**: Sobrescreve o método `delete` para garantir que a exclusão também aconteça no índice indireto, removendo a entrada referente ao ID do índice e do arquivo.

   - **`public void list()`**: Exibe todas as categorias armazenadas no índice indireto, permitindo uma visualização dos registros.

   - **`public boolean update(Categoria novaCategoria)`**: Atualiza uma categoria e o índice indireto. Remove a entrada antiga do índice e insere a nova entrada caso o ID permaneça o mesmo, assegurando consistência entre o arquivo e o índice.
   - **`public List<Categoria> readAll()`**: Lê todas as categorias do arquivo e armazena em uma lista. Retorna a lista.

---

### Classe `ArquivoTarefa`

A classe `ArquivoTarefa` gerencia a manipulação dos dados das tarefas e implementa um índice indireto para acesso eficiente às tarefas pelo ID da categoria.

#### Componentes da Classe

1. **Campos**
   - **`Arquivo<Tarefa> arq_tarefa`**: Referência para o arquivo de tarefas.
   - **`ArvoreBMais<ParIdId> indice_indireto_id`**: Índice indireto para acessar tarefas pelo ID da categoria, implementado como uma Árvore B+.

2. **Construtor**
   - **`public ArquivoTarefa()`**: Inicializa o arquivo para armazenar tarefas e configura o índice indireto `indice_indireto_id`, utilizando uma Árvore B+ para permitir busca eficiente pelo ID da categoria.

3. **Métodos**

   - **`public int create(Tarefa c)`**: Sobrescreve o método `create` para incluir a inserção no índice indireto, associando o ID da categoria ao ID da tarefa. Após criar a tarefa, adiciona uma entrada no índice indireto.
   
   - **`public ArrayList<Tarefa> readAll(int id)`**: Retorna todas as tarefas associadas a uma categoria específica, com base no ID da categoria. Utiliza o índice indireto para buscar todas as tarefas relacionadas ao ID da categoria fornecido.

   - **`public boolean delete(int id)`**: Sobrescreve o método `delete` para garantir que a exclusão ocorra tanto no índice indireto quanto no arquivo, removendo a entrada referente ao ID da tarefa.

   - **`public boolean update(Tarefa novaTarefa)`**: Atualiza uma tarefa e o índice indireto, removendo a entrada antiga e inserindo uma nova entrada no índice caso o ID permaneça o mesmo, assegurando a integridade entre o índice e o arquivo.

---

### Classe `ParIDEndereco`

A classe `ParIDEndereco` representa um par de valores composto por um `id` e um `endereço`, utilizados como chave e valor, respectivamente, em uma estrutura de hash extensível.

#### Componentes da Classe

1. **Campos**
   - **`int id`**: Chave para o par, representando um identificador único.
   - **`long endereco`**: Valor associado ao `id`, representando um endereço.
   - **`short TAMANHO`**: Tamanho em bytes de um registro `ParIDEndereco`, definido como 12 bytes.

2. **Construtores**
   - **`public ParIDEndereco()`**: Construtor padrão que inicializa o `id` e o `endereco` com valores inválidos (`-1`), indicando que o registro está vazio.
   - **`public ParIDEndereco(int id, long end)`**: Construtor que recebe o `id` e o `endereco` como parâmetros, permitindo a criação de um registro válido com chave e valor específicos.

3. **Métodos**

   - **`public int getId()`**: Retorna o `id` do registro.
   
   - **`public long getEndereco()`**: Retorna o `endereco` do registro.
   
   - **`public int hashCode()`**: Retorna o valor de `id` como o hash code do objeto, utilizado em operações de hash extensível.

   - **`public short size()`**: Retorna o tamanho em bytes do registro, determinado pelo campo `TAMANHO`.

   - **`public String toString()`**: Retorna uma representação em formato de `String` do objeto, no formato `"(id; endereco)"`.

   - **`public byte[] toByteArray()`**: Serializa o objeto `ParIDEndereco` para um array de bytes, permitindo armazenamento em disco. Escreve o `id` e o `endereco` no fluxo de dados.

   - **`public void fromByteArray(byte[] ba)`**: Deserializa um array de bytes para inicializar os campos `id` e `endereco` do objeto, permitindo a recuperação do objeto a partir de um armazenamento em bytes.

A classe `ParIDEndereco` é projetada para ser usada em estruturas de hash extensível, oferecendo métodos de serialização e deserialização para suporte ao armazenamento persistente.

---

### Classe `ParIdId`

A classe `ParIdId` representa um par de identificadores inteiros, com foco em manipulação de registros em uma estrutura de Árvore B+.

#### Componentes da Classe

1. **Campos**
   - **`int id1`**: Primeiro identificador, usado principalmente para classificação e operações de comparação.
   - **`int id2`**: Segundo identificador, utilizado para complementar o primeiro em comparações.
   - **`short TAMANHO`**: Tamanho em bytes do registro, fixado em 8 bytes.

2. **Construtores**
   - **`public ParIdId()`**: Construtor padrão que inicializa ambos os identificadores com `-1`, indicando um registro vazio.
   - **`public ParIdId(int n1)`**: Construtor que recebe apenas o `id1` e inicializa `id2` com `-1`.
   - **`public ParIdId(int n1, int n2)`**: Construtor que recebe os dois identificadores como parâmetros, permitindo a criação de um par completo.

3. **Métodos**

   - **`public void setId1(int id)`**: Define o valor do primeiro identificador.
   - **`public int getId1()`**: Retorna o valor do primeiro identificador.
   
   - **`public void setId2(int id)`**: Define o valor do segundo identificador.
   - **`public int getId2()`**: Retorna o valor do segundo identificador.
   
   - **`public ParIdId clone()`**: Cria uma cópia do objeto `ParIdId`.
   
   - **`public short size()`**: Retorna o tamanho do registro em bytes.
   
   - **`public int compareTo(ParIdId a)`**: Compara dois objetos `ParIdId` primeiro pelo `id1`, e se forem iguais, pelo `id2`. Se `id2` for `-1`, considera os objetos como iguais no segundo identificador.
   
   - **`public String toString()`**: Retorna uma representação em formato `String` no estilo `"id1;id2"`.

   - **`public byte[] toByteArray()`**: Serializa o objeto para um array de bytes, com `id1` e `id2`.
   
   - **`public void fromByteArray(byte[] ba)`**: Deserializa um array de bytes, atribuindo os valores lidos aos campos `id1` e `id2`.

---

### Classe `ParNomeId`

A classe `ParNomeId` associa um `nome` a um identificador `id`, otimizando o acesso aos registros por nome em uma Árvore B+.

#### Componentes da Classe

1. **Campos**
   - **`String nome`**: Nome associado ao identificador.
   - **`int id`**: Identificador numérico único.
   - **`short TAMANHO`**: Tamanho em bytes do registro, definido como 30 bytes.

2. **Construtores**
   - **`public ParNomeId()`**: Construtor padrão que inicializa o `nome` com uma string vazia e `id` com `-1`.
   - **`public ParNomeId(String n)`**: Construtor que recebe um `nome` e define `id` como `-1`.
   - **`public ParNomeId(String n, int i)`**: Construtor que recebe `nome` e `id`. Verifica o tamanho do nome, lançando exceção se exceder 26 bytes.

3. **Métodos**

   - **`public void setId(int id)`**: Define o valor de `id`.
   - **`public int getId()`**: Retorna o valor de `id`.
   
   - **`public void setNome(String s)`**: Define o valor de `nome`.
   - **`public String getNome()`**: Retorna o valor de `nome`.
   
   - **`public ParNomeId clone()`**: Cria uma cópia do objeto `ParNomeId`.
   
   - **`public short size()`**: Retorna o tamanho do registro em bytes.
   
   - **`public int compareTo(ParNomeId a)`**: Compara dois objetos `ParNomeId` pelo `nome`, usando uma versão normalizada que remove acentuação e converte para minúsculas.
   
   - **`public String toString()`**: Retorna uma representação em `String` no formato `"id) nome"`.

   - **`public byte[] toByteArray()`**: Serializa o objeto para um array de bytes, armazenando `nome` em um array fixo de 26 bytes e o `id`.
   
   - **`public void fromByteArray(byte[] ba)`**: Deserializa um array de bytes para preencher os campos `nome` e `id`.
   
   - **`private static String strnormalize(String str)`**: Método privado para normalizar `nome`, removendo acentuação e convertendo para minúsculas, facilitando comparações insensíveis a acentos.

---

### Experiência de Desenvolvimento
"Finalmente, relatem um pouco a experiência de vocês, explicando questões como: Vocês implementaram todos os requisitos? Houve alguma operação mais difícil? 
Vocês enfrentaram algum desafio na implementação? Os resultados foram alcançados?
A ideia, portanto, é relatar como foi a experiência de desenvolvimento do TP. Aqui, a ideia é entender como foi para vocês desenvolver este TP."

> A nossa experiência de desenvolvimento dessa etapa foi postivia, apesar do grau mais elevado de dificuldade. Tivemos problemas em adaptar nossa implementação para a Árvore B+ disponibilizada, além de demorarmos mais do que o previsto para concluir totalmente o projeto. Percebemos que com o aumento da escala do projeto, organização e planejamento são essenciais. Por fim, conseguimos entregar todos os requisitos.

> Para facilitar a cooperação, usamos muito a extensão Live Share do Visual Studio Code, que permite aos participantes acompanharem as mudanças em tempo real. Com isso, discutimos as dificuldades e diferentes estratégias.

---

### Perguntas finais

- O CRUD (com índice direto) de categorias foi implementado?
  > Sim.
- Há um índice indireto de nomes para as categorias?
  > Sim.
- O atributo de ID de categoria, como chave estrangeira, foi criado na classe Tarefa?
  > Sim.
- Há uma árvore B+ que registre o relacionamento 1:N entre tarefas e categorias?
  > Sim.
- É possível listar as tarefas de uma categoria?
  > Sim.
- A inclusão da categoria em uma tarefa se limita às categorias existentes?
  > Sim.
- O trabalho está funcionando corretamente?
  > Sim.
- O trabalho está completo?
  > Sim.
- O trabalho é original e não a cópia de um trabalho de outro grupo?
  > Sim.

---
---
## TP3

Relatório
"Descrevam um pouco o esforço. Uma descrição do seu trabalho nas suas próprias palavras. Basicamente, vocês devem responder à seguinte pergunta: O que o trabalho de vocês faz?"

> O trabalho 3 implementa uma Lista Invertida, que permite uma pesquisa de Tarefas baseada nas palavras do seu nome. Além disso, também foi implementada uma relação N:N entre uma nova entidade Rótulo e as Tarefas. Rótulos também receberam o seu próprio CRUD.

> A Lista Invertida recebe palavras chave e retorna uma sequência de respostas possíveis, baseada na frequência de cada palavra, ordenada por proximidade às chaves.

> Os Rótulos são inseridos em uma Árvore B+, que permite operações eficientes. Um Rótulo pode estar associado a diversas tarefas e uma tarefa pode estar relacionada a diversos rótulos, acontecendo então uma relação N:N.

---

### Descrição das Classes e Métodos (referentes ao TP3)

### Classe `ParNomeId`

A classe `ParNomeId` associa um `nome` a um identificador `id`, otimizando o acesso aos registros por nome em uma Árvore B+.

#### Componentes da Classe

1. **Campos**
   - **`String nome`**: Nome associado ao identificador.
   - **`int id`**: Identificador numérico único.
   - **`short TAMANHO`**: Tamanho em bytes do registro, definido como 30 bytes.

2. **Construtores**
   - **`public ParNomeId()`**: Construtor padrão que inicializa o `nome` com uma string vazia e `id` com `-1`.
   - **`public ParNomeId(String n)`**: Construtor que recebe um `nome` e define `id` como `-1`.
   - **`public ParNomeId(String n, int i)`**: Construtor que recebe `nome` e `id`. Verifica o tamanho do nome, lançando exceção se exceder 26 bytes.

3. **Métodos**
   - **`public void setId(int id)`**: Define o valor de `id`.
   - **`public int getId()`**: Retorna o valor de `id`.
   - **`public void setNome(String s)`**: Define o valor de `nome`.
   - **`public String getNome()`**: Retorna o valor de `nome`.
   - **`public ParNomeId clone()`**: Cria uma cópia do objeto `ParNomeId`.
   - **`public short size()`**: Retorna o tamanho do registro em bytes.
   - **`public int compareTo(ParNomeId a)`**: Compara dois objetos `ParNomeId` pelo `nome`, usando uma versão normalizada que remove acentuação e converte para minúsculas.
   -

---

### Classe `ArquivoRotulo`

A classe `ArquivoRotulo` gerencia a criação, leitura, atualização e exclusão de registros de rótulos, utilizando um índice B+ para otimizar o acesso aos rótulos.

#### Componentes da Classe

1. **Campos**
   - **`ArvoreBMais<ParRotuloId> indiceRotulos`**: Índice B+ utilizado para associar rótulos a identificadores (`id`).

2. **Construtores**
   - **`public ArquivoRotulo()`**: Construtor que inicializa o arquivo de rótulos e a estrutura de índice B+ associada. Cria o arquivo e o índice, tratando exceções.

3. **Métodos**
   - **`public int create(String nomeRotulo)`**: Cria um novo rótulo com o nome fornecido e retorna o ID do rótulo criado.
   - **`private int createInterno(Rotulo rotulo)`**: Cria o rótulo no arquivo base e atualiza o índice B+ com o novo rótulo.
   - **`public ArrayList<Tarefa> readTarefasPorRotulo(String nomeRotulo)`**: Retorna uma lista de tarefas associadas ao rótulo especificado.
   - **`public boolean update(String nomeAntigo, String nomeNovo)`**: Atualiza o nome de um rótulo, mantendo o mesmo ID e atualizando o índice B+.
   - **`public boolean delete(String nomeRotulo)`**: Exclui o rótulo com o nome fornecido, removendo-o do arquivo e do índice, caso não haja tarefas associadas.
   - **`public ArrayList<Rotulo> listar()`**: Retorna uma lista de todos os rótulos armazenados e os imprime no formato `"id) nome"`.

   ---

   ### Classe `ArquivoTarefa`

A classe `ArquivoTarefa` gerencia a criação, leitura, atualização e exclusão de tarefas, além de otimizar buscas com o uso de índices B+ e um gerenciador de palavras-chave.

#### Componentes da Classe

1. **Campos**
   - **`ArvoreBMais<ParIdId> indiceCategoriaParaTarefa`**: Índice B+ para associar IDs de categorias com IDs de tarefas.
   - **`ArvoreBMais<ParIDRotulocID> indiceRotuloParaTarefa`**: Índice B+ para associar IDs de rótulos com IDs de tarefas.
   - **`StopWords gerenciadorStopWords`**: Gerenciador de palavras-chave para a busca e processamento de stop words.

2. **Construtores**
   - **`public ArquivoTarefa()`**: Construtor que inicializa os índices B+ para categorias e rótulos, além de configurar o gerenciador de palavras-chave.

3. **Métodos**
   - **`@Override public int create(Tarefa tarefa)`**: Cria uma nova tarefa no arquivo e nos índices, além de adicionar palavras-chave associadas.
   - **`public ArrayList<Tarefa> readAll(int idCategoria)`**: Retorna todas as tarefas associadas a uma categoria específica, usando o índice de categorias.
   - **`public ArrayList<Tarefa> read(ParRotuloId parRotulo)`**: Retorna todas as tarefas associadas a um rótulo específico, usando o índice de rótulos.
   - **`public boolean delete(Tarefa tarefa)`**: Exclui uma tarefa, removendo suas associações nos índices e gerenciador de palavras-chave.
   - **`public boolean update(Tarefa tarefaAntiga, Tarefa tarefaNova)`**: Atualiza uma tarefa e ajusta as palavras-chave no gerenciador.
   - **`public ArrayList<Tarefa> listar(String palavraChave)`**: Lista as tarefas baseadas em palavras-chave, ordenadas por relevância, utilizando o gerenciador de palavras-chave.
   - **`public boolean updateRotulos(Tarefa tarefa, ArrayList<Integer> idsRemovidos, ArrayList<Integer> idsAdicionados)`**: Atualiza os rótulos associados a uma tarefa, adicionando ou removendo rótulos do índice e da tarefa.

---

### Classe `ParIDRotulocID`

A classe `ParIDRotulocID` representa uma estrutura de dados que armazena um par de IDs: um ID de rótulo e um ID de tarefa. Esta estrutura é usada em índices B+ para associar rótulos a tarefas.

#### Componentes da Classe

1. **Campos**
   - **`private int idRotulo`**: ID do rótulo.
   - **`private int idTarefa`**: ID da tarefa.
   - **`private final short TAMANHO = 8`**: Tamanho fixo (em bytes) do objeto `ParIDRotulocID`.

2. **Construtores**
   - **`public ParIDRotulocID()`**: Construtor padrão, inicializa os IDs como -1.
   - **`public ParIDRotulocID(int idRotulo)`**: Construtor que inicializa o ID de rótulo e define o ID de tarefa como -1.
   - **`public ParIDRotulocID(int idRotulo, int idTarefa)`**: Construtor que inicializa ambos os IDs, de rótulo e de tarefa.

3. **Métodos**
   - **`public void setidRotulo(int idRotulo)`**: Define o ID do rótulo.
   - **`public void setidTarefa(int idTarefa)`**: Define o ID da tarefa.
   - **`public int getidRotulo()`**: Retorna o ID do rótulo.
   - **`public int getidTarefa()`**: Retorna o ID da tarefa.

4. **Métodos adicionais**
   - **`public ParIDRotulocID clone()`**: Retorna uma cópia do objeto `ParIDRotulocID`.
   - **`public short size()`**: Retorna o tamanho do objeto em bytes (sempre 8).
   - **`public int compareTo(ParIDRotulocID a)`**: Compara o objeto atual com outro objeto `ParIDRotulocID`. A comparação é feita primeiro pelo ID do rótulo e, se iguais, pelo ID da tarefa.
   - **`public String toString()`**: Retorna uma representação do objeto como string no formato `"idRotulo; idTarefa"`.
   - **`public byte[] toByteArray()`**: Converte o objeto para um array de bytes para armazenamento ou transmissão.
   - **`public void fromByteArray(byte[] ba)`**: Lê os dados do objeto a partir de um array de bytes.

#### Descrição

A classe `ParIDRotulocID` é utilizada em operações de leitura e escrita para persistir e manipular associações entre tarefas e rótulos. Ela permite fácil comparação, clonagem, conversão para bytes e leitura de dados a partir de bytes, sendo ideal para seu uso em índices B+ e sistemas de banco de dados. 

Os métodos de comparação e de conversão para array de bytes são essenciais para garantir a persistência eficiente e a integridade dos dados no armazenamento em disco.

---

### Classe `ParRotuloId`

A classe `ParRotuloId` representa um par de dados composto por um nome de rótulo e um ID associado. Ela é usada principalmente em índices B+ e em sistemas de gerenciamento de dados que necessitam de associações entre rótulos e seus respectivos IDs. Essa classe implementa a interface `RegistroArvoreBMais<ParRotuloId>`, sendo adequada para armazenar e manipular esses pares de dados de forma eficiente.

#### Componentes da Classe

1. **Atributos**
   - **`private String nome`**: O nome do rótulo.
   - **`private int id`**: O ID associado ao rótulo.
   - **`private short TAMANHO = 30`**: O tamanho fixo do registro, em bytes.

2. **Construtores**
   - **`public ParRotuloId()`**: Construtor padrão que inicializa o nome como uma string vazia e o ID como -1.
   - **`public ParRotuloId(String n)`**: Construtor que inicializa o nome do rótulo e o ID como -1.
   - **`public ParRotuloId(String n, int i)`**: Construtor que inicializa o nome e o ID. Lança uma exceção caso o nome tenha mais de 26 caracteres.

3. **Métodos `GET` e `SET`**
   - **`public String getNome()`**: Retorna o nome do rótulo.
   - **`public int getId()`**: Retorna o ID do rótulo.
   - **`public void setNome(String nome)`**: Define o nome do rótulo.
   - **`public void setId(int id)`**: Define o ID do rótulo.

4. **Métodos adicionais**
   - **`@Override public ParRotuloId clone()`**: Cria uma cópia do objeto atual.
   - **`public short size()`**: Retorna o tamanho do objeto em bytes (sempre 30).
   - **`public int compareTo(ParRotuloId a)`**: Compara dois objetos `ParRotuloId` com base no nome, removendo acentuação e tornando as letras minúsculas.
   - **`public String toString()`**: Retorna uma representação do objeto como string no formato `"nome; id"`.
   - **`public byte[] toByteArray()`**: Converte o objeto em um array de bytes para armazenamento ou transmissão. O nome é ajustado para ter exatamente 26 caracteres.
   - **`public void fromByteArray(byte[] ba)`**: Lê os dados do objeto a partir de um array de bytes.
   - **`public static String transforma(String str)`**: Transforma uma string para minúsculas e remove os acentos (diacríticos), utilizando normalização.

#### Descrição

A classe `ParRotuloId` serve para representar um rótulo associado a um ID, sendo fundamental para organizar e manipular dados em sistemas de armazenamento como índices B+ ou bancos de dados. Seu principal objetivo é permitir a ordenação, leitura, escrita e comparação de rótulos de forma eficiente, garantindo também que o nome do rótulo seja bem formatado para armazenamento (limitando a 26 caracteres e normalizando acentuação). Ela também oferece suporte para conversão para e de arrays de bytes, facilitando a persistência dos dados em arquivos binários.

A transformação dos nomes para minúsculas e a remoção de acentos tornam a classe mais robusta em relação à busca e comparação de rótulos, garantindo que diferentes formas de escrita não influenciem nas operações.

---

### Classe `StopWords`

A classe `StopWords` gerencia palavras consideradas irrelevantes (stop words) para análise de textos, removendo-as de títulos e mantendo uma estrutura de dados invertida para armazenar as palavras processadas e suas frequências.

#### Componentes da Classe

1. **Campos**
   - **`ArrayList<String> listaStopWords`**: Lista contendo as palavras classificadas como stop words.
   - **`ListaInvertida listaInvertida`**: Estrutura de dados para gerenciar índices invertidos, armazenando palavras não consideradas stop words.

2. **Construtores**
   - **`public StopWords()`**: Construtor que inicializa a lista de stop words a partir de um arquivo e configura a estrutura da lista invertida.

3. **Métodos**

     - **`public String[] processarStopWords(String titulo)`**: 
       - Divide o título em palavras.
       - Converte cada palavra para minúsculas.
       - Remove palavras que constam na lista de stop words.
       - Retorna um array de palavras processadas.

     - **`public void contarPalavras(ArrayList<ElementoLista> elementos, String[] palavras, int idElemento)`**: 
       - Conta as palavras válidas (não vazias) em um título.
       - Calcula a frequência relativa de cada palavra.
       - Adiciona os dados à lista de elementos associados a uma tarefa.

     - **`public void inserirTitulo(String titulo, int idTarefa)`**: 
       - Processa o título removendo as stop words.
       - Calcula a frequência relativa das palavras.
       - Insere as palavras válidas e suas frequências na lista invertida.

     - **`public void executarTeste(String[] args)`**: 
       - Método para teste da funcionalidade da classe.
       - Permite a entrada de títulos pelo console, adicionando-os à lista invertida.

---


### Experiência de Desenvolvimento
"Finalmente, relatem um pouco a experiência de vocês, explicando questões como: Vocês implementaram todos os requisitos? Houve alguma operação mais difícil? 
Vocês enfrentaram algum desafio na implementação? Os resultados foram alcançados?
A ideia, portanto, é relatar como foi a experiência de desenvolvimento do TP. Aqui, a ideia é entender como foi para vocês desenvolver este TP."

> Nessa etapa do desenvolvimento, tivemos muito mais dificuldade em relação às anteriores. Unir a Lista Invertida com o restante das estruturas para pesquisar a partir dos termos foi o ponto que achamos mais complicado. Também não conseguimos implementar completamente as relações N:N para todas as operações.

> Investimos menos tempo nesse trabalho do que nos anteriores, e por isso o resultado não foi o desejado.

> Para facilitar a cooperação, usamos muito a extensão Live Share do Visual Studio Code, que permite aos participantes acompanharem as mudanças em tempo real. Com isso, discutimos as dificuldades e diferentes estratégias.

---

### Perguntas finais

- O índice invertido com os termos das tarefas foi criado usando a classe ListaInvertida?
  > Sim.
- O CRUD de rótulos foi implementado?
  > Sim.
- No arquivo de tarefas, os rótulos são incluídos, alterados e excluídos em uma árvore B+?
  > Parcialmente.
- É possível buscar tarefas por palavras usando o índice invertido?
  > Não.
- É possível buscar tarefas por rótulos usando uma árvore B+?
  > Não.
- O trabalho está completo?
  > Não.
- O trabalho é original e não a cópia de um trabalho de um colega?
  > Sim.

---
---
## TP4

Relatório
"Descrevam um pouco o esforço. Uma descrição do seu trabalho nas suas próprias palavras. Basicamente, vocês devem responder à seguinte pergunta: O que o trabalho de vocês faz?"

## Descrição do Trabalho

> O Trabalho Prático 4 (TP4) implementa uma rotina de backup compactado para todos os arquivos de dados e índices do sistema, utilizando o algoritmo de compressão **LZW**. O objetivo é armazenar todos os arquivos de forma eficiente, em um único arquivo compactado, permitindo tanto a criação do backup quanto a recuperação dos dados.

### Funcionalidades Implementadas

1. **Compactação de Dados**:
   - Todos os arquivos de dados e índices são lidos como vetores de bytes.
   - Utiliza o algoritmo **LZW** com dicionário de 12 bits para compactar os dados.
   - Compacta múltiplos arquivos em um único arquivo de backup.

2. **Armazenamento no Arquivo Compactado**:
   - Para cada arquivo, o backup inclui:
     1. Nome do arquivo original.
     2. Tamanho do vetor de bytes compactado.
     3. Vetor de bytes compactados.

3. **Criação de Pastas de Backup**:
   - Cada backup é armazenado em uma pasta nomeada com a data/hora da criação, permitindo a organização e versionamento.

4. **Recuperação de Dados**:
   - O sistema permite ao usuário selecionar a versão do backup para descompactação.
   - Recria os arquivos de dados e índices a partir do arquivo compactado.

---

---

### Descrição das Classes e Métodos (referentes ao TP4)

# Descrição das Classes e Métodos

## Classe `Backup`

A classe **`Backup`** é responsável por realizar operações de backup e restauração de arquivos, utilizando o algoritmo de compressão LZW para compactação dos dados. Além disso, ela fornece ferramentas para listar e gerenciar backups.

### Atributos

- **`backupDir`**: Caminho do diretório onde os backups são armazenados.
- **`dataDir`**: Caminho do diretório onde os dados estão localizados.

### Métodos

#### 1. `Backup()`
- **Descrição**: Construtor da classe que inicializa os diretórios de backup e dados, criando-os caso não existam.
- **Retorno**: Nenhum.

#### 2. `getBackupDir()`
- **Descrição**: Retorna o caminho do diretório de backups.
- **Retorno**: `String` - Caminho do diretório de backup.

#### 3. `getDataDir()`
- **Descrição**: Retorna o caminho do diretório de dados.
- **Retorno**: `String` - Caminho do diretório de dados.

#### 4. `createBackup(String backupFileName)`
- **Descrição**: Cria um backup compactado dos dados presentes no diretório de dados. Os dados são serializados e comprimidos utilizando o algoritmo LZW, sendo armazenados no diretório de backups.
- **Parâmetros**: `backupFileName` - Nome do arquivo de backup.
- **Retorno**: Nenhum.

#### 5. `restoreBackup(String backupFileName)`
- **Descrição**: Restaura os dados de um arquivo de backup compactado. Os dados são descompactados e desserializados, recriando os arquivos originais no diretório de dados.
- **Parâmetros**: `backupFileName` - Nome do arquivo de backup a ser restaurado.
- **Retorno**: Nenhum.

#### 6. `listBackups()`
- **Descrição**: Lista todos os backups disponíveis no diretório de backup.
- **Retorno**: `ArrayList<String>` - Lista de nomes dos backups.

#### 7. `calculateCompressionRate(byte[] originalData, byte[] compressedData)`
- **Descrição**: Calcula a taxa de compressão entre os dados originais e comprimidos.
- **Parâmetros**: `originalData` - Dados originais; `compressedData` - Dados comprimidos.
- **Retorno**: `double` - Taxa de compressão em porcentagem.

#### 8. `serializeFiles(File[] files)`
- **Descrição**: Serializa os arquivos de um diretório em um array de bytes.
- **Parâmetros**: `files` - Array de arquivos a serem serializados.
- **Retorno**: `byte[]` - Dados serializados.

#### 9. `clearDirectory(String directoryPath)`
- **Descrição**: Remove todos os arquivos e subdiretórios de um diretório especificado.
- **Parâmetros**: `directoryPath` - Caminho do diretório a ser limpo.
- **Retorno**: Nenhum.

#### 10. `createDirectory(String directoryPath)`
- **Descrição**: Cria um diretório no caminho especificado, se não existir.
- **Parâmetros**: `directoryPath` - Caminho do diretório a ser criado.
- **Retorno**: Nenhum.

#### 11. `readFile(File file)`
- **Descrição**: Lê os dados de um arquivo e os retorna como um array de bytes.
- **Parâmetros**: `file` - Arquivo a ser lido.
- **Retorno**: `byte[]` - Dados do arquivo.

#### 12. `writeFile(String filePath, byte[] data)`
- **Descrição**: Escreve um array de bytes em um arquivo especificado.
- **Parâmetros**: `filePath` - Caminho do arquivo de destino; `data` - Dados a serem escritos.
- **Retorno**: Nenhum.

---

## Classe `LZW`

A classe **`LZW`** implementa a codificação e decodificação de uma mensagem usando o algoritmo LZW. Essa classe transforma strings em sequências de índices e converte essas sequências em vetores de bits para compactação.

### Métodos

#### 1. `main(String[] args)`
- **Descrição**: Método principal para teste de codificação e decodificação, exibindo a eficiência da compressão.
- **Retorno**: Nenhum.

#### 2. `codifica(byte[] msgBytes)`
- **Descrição**: Codifica uma mensagem em um vetor de índices usando o algoritmo LZW.
- **Parâmetros**: `msgBytes` - Mensagem em bytes a ser codificada.
- **Retorno**: `byte[]` - Mensagem codificada em forma de vetor de bits.

#### 3. `decodifica(byte[] msgCodificada)`
- **Descrição**: Decodifica uma mensagem codificada em um vetor de bits e retorna a mensagem original.
- **Parâmetros**: `msgCodificada` - Mensagem codificada em vetor de bits.
- **Retorno**: `byte[]` - Mensagem original decodificada.

---

## Classe `VetorDeBits`

A classe **`VetorDeBits`** gerencia operações com vetores de bits, suportando a conversão de bytes para vetores de bits e vice-versa.

### Atributos

- **`vetor`**: Objeto `BitSet` que armazena os bits.

### Métodos

#### 1. `VetorDeBits()`
- **Descrição**: Construtor padrão que inicializa um vetor de bits com um bit definido.
- **Retorno**: Nenhum.

#### 2. `VetorDeBits(int n)`
- **Descrição**: Construtor que inicializa um vetor de bits com um tamanho específico.
- **Parâmetros**: `n` - Tamanho do vetor.
- **Retorno**: Nenhum.

#### 3. `VetorDeBits(byte[] v)`
- **Descrição**: Construtor que cria um vetor de bits a partir de um array de bytes.
- **Parâmetros**: `v` - Array de bytes.
- **Retorno**: Nenhum.

#### 4. `toByteArray()`
- **Descrição**: Converte o vetor de bits em um array de bytes.
- **Retorno**: `byte[]` - Array de bytes representando o vetor de bits.

#### 5. `set(int i)`
- **Descrição**: Define um bit específico no vetor.
- **Parâmetros**: `i` - Índice do bit a ser definido.
- **Retorno**: Nenhum.

#### 6. `clear(int i)`
- **Descrição**: Limpa um bit específico no vetor.
- **Parâmetros**: `i` - Índice do bit a ser limpo.
- **Retorno**: Nenhum.

#### 7. `get(int i)`
- **Descrição**: Retorna o valor de um bit específico.
- **Parâmetros**: `i` - Índice do bit.
- **Retorno**: `boolean` - Valor do bit.

#### 8. `length()`
- **Descrição**: Retorna o comprimento do vetor de bits.
- **Retorno**: `int` - Comprimento do vetor de bits.

#### 9. `size()`
- **Descrição**: Retorna o tamanho do vetor de bits.
- **Retorno**: `int` - Tamanho do vetor de bits.

#### 10. `toString()`
- **Descrição**: Retorna uma representação em string do vetor de bits.
- **Retorno**: `String` - Representação em string do vetor de bits.


---


### Experiência de Desenvolvimento
"Finalmente, relatem um pouco a experiência de vocês, explicando questões como: Vocês implementaram todos os requisitos? Houve alguma operação mais difícil? 
Vocês enfrentaram algum desafio na implementação? Os resultados foram alcançados?
A ideia, portanto, é relatar como foi a experiência de desenvolvimento do TP. Aqui, a ideia é entender como foi para vocês desenvolver este TP."

> Durante o desenvolvimento do TP4, encontramos desafios relacionados à compactação em fluxo de bytes, evitando o carregamento de arquivos inteiros na memória. Além disso, a integração do algoritmo **LZW** com a estrutura de arquivos do sistema exigiu uma boa organização do código.

> Embora todas as funcionalidades principais tenham sido implementadas, a otimização da lógica de compressão e descompressão, especialmente em arquivos maiores, foi o ponto mais desafiador.

> Utilizamos ferramentas como **Visual Studio Code** (com a extensão Live Share) para trabalho colaborativo em tempo real, o que facilitou a identificação e resolução de problemas.

---

### Perguntas finais

- Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos?
  > Sim.
- Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos?
  > Sim.
- O usuário pode escolher a versão a recuperar?
  > Sim.
- Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais)
  > 46,5%.
- O trabalho está funcionando corretamente?
  > Sim.
- O trabalho está completo?
  > Sim.
- O trabalho é original e não a cópia de um trabalho de um colega?  
  > Sim.
