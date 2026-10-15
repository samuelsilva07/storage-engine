# Adaptative Storage Engine

## Etapa 1

### Objetivo

- Implementar operações de PUT/GET/DELETE na engine
- Garantir a persistência do disco após encerramento e reinicialização
- Possibilitar a inserção de valores de tamanho variável
- Desenvolver uma verificação básica de integridade
- Promover uma recuperação básica após interrupção abrupta

### Conteúdo do disco

#### Registro

Para garantir a flexibilidade do tamanho dos valores, a representação de cada registro deve conter:

- Status do registro **(ATIVO/INATIVO)**
- Valor da chave do registro (uint64 - long com alguns métodos em Java)
- Tamanho do valor do registro, em bytes **(indica quantos bytes serão lidos em seguida, garantindo o tamanho variável dos valores do registro)**
- Valor do registro

Esta representação auxilia na leitura após interrupções, pois apenas os registros com status ATIVOS serão considerados na construção da estrutura da RAM, diminuindo a quantidade de operações na sua reconstrução em eventuais erros na engine.

#### Histórico das operações

Para manter a persistência do disco, deve-se gerar dois arquivos: **armazenar as operações realizadas pela engine em ordem cronológica, com os dados do registro envolvido**. Deste modo, em casos de interrupção inesperada, o processo de reinicialização apenas precisará ler o arquivo para restaurar o estado atual na estrutura auxiliar na memória RAM, através da leitura das operações necessárias.

A cada operação realizada, ela deve ser registrada no final do arquivo em disco, para manter os registros das operações válidos e formando uma linha do tempo da engine. Dessa forma, a reconstrução da estrutura na RAM será feita com fidelidade ao histórico relacionado às requisições realizadas.

Como resultado, a modelagem das operações + registros no disco deve ser baseada na estrutura abaixo: 

- Valor da operação **(implementar enum - PUT/GET/DELETE - associando cada uma a um valor inteiro, que será armazenado no disco)**
- Valor da chave do registro (uint64 - long com alguns métodos em Java)
- Tamanho do valor do registro, em bytes **(indica quantos bytes serão lidos em seguida, garantindo o tamanho variável dos valores do registro)**
- Valor do registro

### Utilização da memória RAM

Esta estrutura será utilizada somente como um recurso temporário, para representar os endereços dos registros manipulados pela engine no momento da execução. Conforme as operações são realizadas, ela será utilizada para localizar os dados do disco em tempo constante. 

Desta forma, a modelagem da memória principal deve utilizar uma tabela hash, contendo a chave e o endereço do registro no arquivo - assim, utilizamos o valor da chave para obter a posição dos registros de forma imediata, facilitando o seu acesso no disco. Em caso de encerramento da engine ou da ocorrência de algum problema, a ordem das operações em disco indica o estado atual da engine. Na reinicialização, devemos reconstruir a estrutura da RAM por meio da leitura do arquivo, e adicionando os endereços de cada registro na tabela.  

### Classes utilizadas

#### Storage Engine

Representa a fachada da engine, contendo as operações fornecidas por ela, em comunicação com o disco. 

#### DiskManager

Tem o papel de controlar o estado dos registros no disco, manipulando o arquivo de forma direta e realizando as operações propriamente ditas.

#### OperationManager

Responsável por manter o histórico das operações da engine no disco, para manter a integridade do progresso da engine

#### MapManager

Contém a tabela hash da engine, utilizada para obter os endereços dos registros através das suas chaves

#### Record

Classe que contém os dados do registro, como status, chave, tamanho do valor e o valor armazenado

#### Operation

Armazena uma operação realizada pela engine, e é essencial para guardar o histórico do trabalho realizado.

### Inicialização da engine

- 1: DiskManager acessa os registros em disco e retorna uma tabela hash, contendo apenas as chaves e endereços dos registros ativos
- 2: MapManager recebe a nova tabela para armazená-la na RAM

### Operação PUT

- 1: Usuário informa a chave e o valor do novo registro para a engine
- 2: LogManager cria o registro da operação e adiciona-o no histórico de operações, com o id, tipo da operação, chave do registro e o valor armazenado
- 3: DiskManager obtém o registro da operação realizada e adiciona-o no fim do disco, armazenando o seu status (ACTIVE), a sua chave, o tamanho do seu valor e o seu valor correspondente, e retorna o endereço do registro no arquivo
  - obs: caso exista um arquivo com a mesma chave, o seu valor no disco deve ser substituído pelo novo valor
- 4: MapManager recebe a chave e o endereço do registro, para adicioná-los na tabela hash
- 5: StorageEngine retorna uma resposta em JSON Lines, contendo o id e o status da operação 


### Operação DELETE

- 1: Usuário informa a chave e o valor do novo registro para a engine
- 2: LogManager cria o registro da operação e adiciona-o no histórico de operações, com o id, tipo da operação e chave do registro
- 3: MapManager recebe a chave informada e retorna o endereço do registro, através da tabela hash na RAM
- 4: DiskManager acessa o endereço do registro e altera o seu status para INACTIVE
- 5: MapManager retira a chave informada da tabela hash
- 6: StorageEngine retorna uma resposta em JSON Lines, contendo o id e o status da operação

### Operação GET

- 1: Usuário informa a chave e o valor do novo registro para a engine
- 2: LogManager cria o registro da operação e adiciona-o no histórico de operações, com o id, tipo da operação e chave do registro
- 3: MapManager recebe a chave informada e retorna o endereço do registro, através da tabela hash na RAM
- 4: DiskManager acessa o endereço do registro e retorna o seu valor, em String
- 5: StorageEngine retorna uma resposta em JSON Lines, contendo o id, status da operação e o valor requisitado

