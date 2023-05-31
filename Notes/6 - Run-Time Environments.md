# Run-Time Environments

Normalmente um programa não é totalmente sequencial nem estático, pelo que o acesso a determinadas variáveis deixa de ser simples devido a problemas de escopo. 

O normal fluxo de dados pode ser interrompido por:
- variáveis globais;
- chamadas de procedimentos ou funções;
- alocação dinâmica de memória;

## Activation Records

AR é uma estrutura criada em tempo de execução (*runtime*) que mantém o estado em relação à execução de um procedimento. ARs podem ser armazenados em uma área definida em tempo de compilação estática, desde que não haja chamadas recursivas. Dentro das linguagens imperativas com recursão como Pascal ou C, precisam ser alocadas na pilha, pois existem várias instâncias ativas das variáveis ​​locais em cada função. Para o caso de linguagens funcionais, onde a vida de algumas das variáveis ​​da função sobrevive à função em que foram criadas, elas precisam ser alocado na heap.
Constutuição:

- Parâmetros do procedimento ou função;
- Espaço para o valor de retorno, se existir;
- Endereço de retorno ao ponto do *caller* que o chamou, para depois de retornar poder continuar a sua execução geral (*frame pointer*);
- Access link, para identificar a localização das variáveis não locais;
- Espaço para variáveis locais, se existirem;

Os registos do Caller são voláteis (podem ser modificados por qualquer função chamada), enquanto os registos do Callee são não-voláteis durante o seu *lifetime*.

O `Frame-Pointer` (FP) ou Stack-Pointer é o campo no AR que aponta para a anterior função ou procedimento invocado. A cadeia de valores FP destaca, assim, a cadeia de chamadas atualmente ativa na call-tree. 

O `Access Link` é um campo no AR que indica onde encontrar as variáveis ​​não locais do código da função que o AR correspondente usa para acessá-los. É usado apenas em linguagens que possuem escopos aninhados lexicais, como Pascal. Em C há apenas dois níveis de escopo, local e global e este campo não é necessário.

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