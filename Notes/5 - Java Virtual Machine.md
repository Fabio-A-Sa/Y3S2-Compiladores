# Java Virtual Machine

Usado para tornar a execução do código independente da máquina, com instruções específicas e regiões de memória. Não é tão eficiente como compilação nativa, pois não permite utilizar registos de forma direta.

Este sistema manipula duas regiões de memória:

- `Stack`, uma pilha para operações aritméticas, onde após uma operação o valor do topo é colocado no próximo index do array de variáveis locais. As variáveis locais são colocadas na stack pela operação *iload*, manipuladas por operações *iadd* e inicializadas na stack pela operação *iconst*;

- `Variáveis locais`, um array que comporta os valores iniciais do código e todos os resultados, através de operações *istore*;