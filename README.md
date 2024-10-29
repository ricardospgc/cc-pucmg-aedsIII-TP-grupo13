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
## TP3
---
## TP4
