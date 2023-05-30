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

Os registos do Caller são voláteis (podem ser modificados por qualquer função chamada), enquanto os registos do Callee são não-voláteis durante o seu *lifetime*.


## Access links

Servem para localizar uma variável numa profundidade de chamadas qualquer a partir de qualquer outra chamada.

- Caso 1: a função A chama uma função B que está numa profundidade superior. O Access Link de B deverá apontar para o Access Link de A.

```c
void a() { 
    b();
}
```

- Caso 2: a função B chama uma função C que está numa profundidade inferior. O Access Link de C deve apontar para o Access Link anterior a A;

```c
void a() { 
    b() {
      c();
    } 
}

void c() {

}
```