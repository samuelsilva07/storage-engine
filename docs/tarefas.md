# Adaptative Storage Engine

## Etapa 1

### Objetivo

- Implementar operações de PUT/GET/DELETE na engine
- Garantir a persistência do disco após encerramento e reinicialização
- Possibilitar a inserção de valores de tamanho variável
- Desenvolver uma verificação básica de integridade
- Promover uma recuperação básica após interrupção abrupta

### Conteúdo do disco

#### Opção 1

Para garantir a flexibilidade do tamanho dos valores, a representação de cada registro deve conter:

- Status do registro **(ATIVO/INATIVO)**
- Valor da chave do registro (uint64 - long com alguns métodos em Java)
- Tamanho do valor do registro, em bytes **(indica quantos bytes serão lidos em seguida, garantindo o tamanho variável dos valores do registro)**
- Valor do registro

Esta representação auxilia na leitura após interrupções, pois apenas os registros com status ATIVOS serão considerados na construção da estrutura da RAM, diminuindo a quantidade de operações na sua reconstrução em eventuais erros na engine.

#### Opção 2

Para manter a persistência do disco, deve-se gerar dois arquivos: **armazenar as operações realizadas pela engine, juntamente com os dados do registro**. Deste modo, em casos de interrupção inesperada, o processo de reinicialização apenas precisará ler o arquivo para restaurar o estado atual na estrutura auxiliar na memória RAM.

A cada operação realizada, ela deve ser registrada no final do arquivo em disco, para manter os registros das operações válidos e distribuídos sequencial. Dessa forma, a reconstrução da estrutura na RAM será feita com fidelidade ao histórico relacionado às requisições realizadas.

Como resultado, a modelagem das operações + registros no disco deve ser baseada na estrutura abaixo: 

- Valor da operação **(implementar enum - PUT/GET/DELETE - associando cada uma a um valor inteiro, que será armazenado no disco)**
- Valor da chave do registro (uint64 - long com alguns métodos em Java)
- Tamanho do valor do registro, em bytes **(indica quantos bytes serão lidos em seguida, garantindo o tamanho variável dos valores do registro)**
- Valor do registro

### Utilização da memória RAM

Esta estrutura será utilizada somente como um recurso temporário, para representar o estado da engine no momento da execução atual. Conforme as operações são realizadas, ela também deve ser alterada de acordo com os seus comandos, juntamente com o arquivo em disco.

Em caso de encerramento da engine ou da ocorrência de alguma intercorrência, a ordem das operações em disco indica o estado atual da engine. Na reinicialização, devemos reconstruir a estrutura da RAM por meio da leitura do arquivo.  

### Classes utilizadas

A definir.

### Operação PUT

A definir.

### Operação DELETE

A definir.

### Operação GET

A definir.