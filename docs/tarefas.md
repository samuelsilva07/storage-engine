# Adaptative Storage Engine

## Etapa 1

### Objetivo

- Implementar operações de PUT/GET/DELETE na engine
- Garantir a persistência do disco após encerramento e reinicialização
- Possibilitar a inserção de valores de tamanho variável
- Desenvolver uma verificação básica de integridade
- Promover uma recuperação básica após interrupção abrupta

### Conteúdo do disco

Para manter a persistência do disco, deve-se **armazenar as operações realizadas pela engine.** Deste modo, a reinicialização apenas precisará ler o arquivo para restaurar o estado atual na estrutura auxiliar na memória RAM.

A cada operação realizada, ela deve ser registrada no final do arquivo em disco, para manter o histórico das operações válido e sequencial.  

### Modelagem das operações no disco 

- Valor da operação **(implementar enum associando cada uma a um valor inteiro, que será armazenado no disco)**
- Valor da chave do registro (uint64/long em Java)
- Tamanho do valor do registro, em bytes **(indica quantos bytes serão lidos em seguida, garantindo o tamanho variável dos valores do registro)**
- Valor do registro

### Utilização da memória RAM

Esta estrutura será utilizada somente como um recurso temporário, para representar o estado da engine no momento da execução atual. Conforme as operações são realizadas, ela também deve ser alterada de acordo com os seus comandos, juntamente com o arquivo em disco.

Em caso de encerramento da engine ou da ocorrência de alguma intercorrência, a ordem das operações em disco indica o estado atual da engine. Na reinicialização, devemos reconstruir a estrutura da RAM por meio da leitura do arquivo.  

### Operação PUT

### Operação DELETE

### Operação GET
