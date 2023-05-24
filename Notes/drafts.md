Data-Flow Analysis: Iterative Frameworks and Examples Ficheiro 
slide 47 -> pergunta no teste

SSA -> só no exame de recurso

Loop optimization -> vai sair no teste

loop invariant porque as definições são fora do loop
todos os operandos são definidos fora do loop então dá para passar para trás

não olhar à redundancy elimination

não precisamos saber os algoritmos de cor

----------------------


activation records, por causa da recursão, porque podemos ter mais que uma instancia da mesma função ao mesmo tempo

não sai sobre instruction scheduling, mas há global and local allocation algoritmo
as duas definições de interfer~encias, acaba do lado direito e começa do lado esquerdo à partida não interferem

argumentar que o BB1 domina BB2 porque ...


live variables analises, importante

10-20-30-20-20, 2 horas

o que é um ARecord, como se acede, como funcionam

registos limpos e sujos, vários BBs, 3 ou 4 blocos básicos,
fazer alocação de memória com registos

vai dar um código, temos de determinar 
dominator e dominator tree (é uma árvore, não um grafo: não colocar setas paea trás)
DU chains, grafo de interferência, dizer quantas cores são precisas. 2 definições de interferências

problema para a frente, para trás, inicialização
união, interseção, 
pergunta sobre a velocidade

