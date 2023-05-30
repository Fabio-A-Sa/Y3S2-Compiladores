# Run-Time Environments

Normalmente um programa não é totalmente sequencial nem estático, pelo que o acesso a determinadas variáveis deixa de ser simples devido a problemas de escopo. 

O normal fluxo de dados pode ser interrompido por:
- variáveis globais;
- chamadas de procedimentos ou funções;
- alocação dinâmica de memória;

## Activation Records

AR é uma estrutura criada em tempo de execução (*runtime*) que mantém o estado em relação à execução de um procedimento. Pode ser alocada:
- estaticamente: se o procedimento não fizer chamadas a outros;
- na stack: se o tempo de vida da AR puder coincidir com o tempo de vida do procedimento e o código executar um retorno previsto;
- na heap: se o procedimento puder viver fora de quem o chamou (threads) ou se o retorno for um objecto que contém o seu estado de execução;

Constutuição:
- Parâmetros do procedimento ou função;
- Conteúdo dos registos;
- Espaço para o valor de retorno, se existir;
- Endereço de retorno ao ponto do *caller* que o chamou, para depois de retornar poder continuar a sua execução geral;
- Access link;
- Caller's AR Pointer, para depois de retornar conseguir reestruturar o AR da procedimento que chamou este;


