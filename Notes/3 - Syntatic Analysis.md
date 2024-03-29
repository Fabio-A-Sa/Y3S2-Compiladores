# Análise Sintática

O `Parser` verifica se a cadeia de tokens estão gramaticalmente correctas, determina se o input é bem formado sob o ponto de vista sintático e ajuda no processo de tradução. Recebe um conjunto de tokens e retorna uma *parse tree*.

## Balanced Parentheses Problem

Não é possível validar o balanço entre a abertura e fecho de parêntises com um autómato ou expressão regular, pois os DFAs e NFAs têm um número finito de estados e não têm memória. Precisamos de algo com maior capacidade de expressão: uma gramática livre de contexto (CFG), que possui uma definição recursiva. Uma solução do problema é a seguinte:

```note
S -> (S)S | e 
```

Uma **gramática livre de contexto** possui símbolos terminais e não terminais. Os símbolos não terminais são os únicos que podem formar por recursão outros símbolos. Como desvantagens temos a implementação mais complexa das CFG, ao contrário das expressões regulares em que poderia ser utilizada uma mecanização como base em tabelas e estados de aceitação.

## Derivação

Processo que permite verificar se o conjunto de tokens pode ser gerado pela gramática livre de contexto associada. O conjunto das derivações são representados numa árvore: os nós intermos são símbolos não terminais e as folhas são símbolos terminais.

### Rightmost Derivation vs. Leftmost Derivation

Procura o símbolo não terminal mais à esquerda ou à direita e expande de acordo com as produções da gramática. 
- A *left most derivation* reflete-se numa derivação **Top-Down**: começamos no símbolo inicial e acabamos nas folhas, os símbolos terminais;
- A *right most derivation* reflete-se numa derivação **Botton-Up**: começamos com os símbolos terminais e contraímos através do inverso das produções da gramática até chegar ao símbolo inicial;

### Gramáticas Ambíguas

Quando existem duas ou mais derivações formalmente correctas para o mesmo input. A eliminação da ambiguidade é importante para não levar a erros ou não determinismos. Muitas vezes a solução passa por introduzir na gramática livre do contexto outros símbolos não terminais para forçar a associação à direita ou à esquerda.

## Implementação de um Parser

A implementação de um parser é um processo muito complexo, pois o sucesso de cada verificação depende da sequência de produções escolhidas que muitas vezes dependem da leitura do input até ao fim. Provar que um input pertence à linguagem ddefinida pela linguagem livre de contexto consiste em mostrar uma derivação possível. O contrário, ou seja, provar que não há nenhuma sequência de derivações de aceitação do input requer testar todas as combinações e sequências, o que torna o problema inviável.

Existem, no entanto, alguns algoritmos e técnicas para implementar um parser. Os algoritmos são identificados por três caracteres:

- L/R: parse left to right (L) or parse right to left (R);
- L/R: left most derivation (L) or right most derivation (R);
- N: parsing with N lookahead tokens;

### Top-Down Parsers

Começa pelo símbolo inicial e tenta acertar com as produções de acordo com a gramática livre de contexto escolhida da esquerda para a direita e com o primeiro token do input não consumido. Se escolher mal, acontece backtracking. 

O algoritmo é mais simples mas não dá para todas as gramáticas, por exemplo as gramáticas recursivas à esquerda pois resulta em ciclos infinitos durante a expansão:

```note
S -> S a | b
```

É necessário nestes casos eliminar a recursão à esquerda, adicionando um novo símbolo terminal e optando pela recursão à direita:

```note
S1 -> b S2
S2 -> a S2 | e
```

É necessário também usar *predictive parsing* para minimizar o número de backtracking devido a erros na escolha das produções da gramática. O algoritmo deve escolher uma produção escolhida no conjunto `First(alpha)+` tal que essa produção possa derivar diretamente (ou indiretamente, no caso da primeira derivação ser epsilon) o mesmo símbolo terminal que a função avista no lookahead. 

A gramática escolhida diz-se LL1 se existir uma derivação:

```note
A -> alpha | beta
```

Tal que não haja interseção entre `First(alpha)+` e `First(beta)+`, ou seja, a escolha do algoritmo será determinística pois só há uma derivação que vai originar o símbolo terminal em *left hand side* igual ao token do lookahead.

A gramática tem de ser `left factoring`, ou seja, com propriedade LL1. Todas as derivações do lado direito têm de ter os primeiros símbolos terminais diferentes (não pode ter prefixos comuns):

```note
# Not LL1
A -> aB | aC | aD | Y

# LL1
A -> aZ | Y
Z -> B | C | D
```

#### Table-driven Top-Down Parsers

Precisamos de uma linha para cada símbolo não terminal e uma coluna para cada símbolo terminal. A tabela preenche-se com as produções de acordo com o First e/ou Follow dos símbolos não terminais. Entradas na tabela vazias correspondem a erro na árvore de *parsing*.

Uma entrada (Símbolo Não Terminal, Símbolo Terminal) define-se:
- Com a regra Símbolo Não Terminal ->  Beta, se Símbolo Terminal pertencer ao First(Beta)
- Com a regra Símbolo Não Terminal -> Epsilon, se Símbolo Terminal pertencer ao Follow(Beta) e a gramática contiver o passo Símbolo Não Terminal -> Epsilon
- Erro, célula deixada em branco, se as primeiras duas regras não puderem ser cumpridas

Se alguma entrada for definida muitas vezes, a gramática não goza da propriedade LL1.

### Bottom-Up Parsers - LR

Começamos nas folhas (símbolos terminais) e comprime de acordo com as produções da gramática, também da esquerda para a direita (`shift-reduce parser`). É um algoritmo mais complexo mas também consegue processar mais gramáticas. É uma abordagem *Left to Right* e *Right Most Derivation*.

- `shift`: mudar o apontador para os tokens seguintes;
- `reduce`: retirar os símbolos da parte direita das produções e colocar no topo da pilha o símbolo não terminal da esquerda;

O input é aceite quando chegamos com sucesso ao símbolo inicial da gramática com todos os tokens consumidos. Nesta construção não nos precisamos de preocupar com problemas de *left recursion* da gramática nem com o seu *left factoring*. No entanto ainda temos alguns problemas:
- fazer o match dos tokens com as produções;
- pode haver mais do que um match;
- há casos onde podemos fazer um shift ou um reduce;

Uma solução possível é usar uma tabela LR(K), que consegue processar gramáticas mais complicadas, mas a sua execução é mais complexa e recorre a duas pilhas: uma dos estados e uma dos símbolos.

#### Handle

Substring do input inicial (*right-sentential*) que coincide com a parte direita de uma produção da gramática. É usada para fazer a próxima redução. É necessário usar uma forma eficiente para detectar os possíveis handles na *sentence* inicial: `handle pruning`. O algoritmo faz preferencialmente reduções, mas há gramáticas que num determinado ponto permitem fazer reduções ou shifts, levando a um provável erro.

#### Items

São formados por cada produção da gramática. Codifica que parte é que já vimos / que parte é que está na stack e que parte é que poderá vir a seguir.

```note
X -> (X) # produção da gramática
X -> *(X) # primeiro item, nada na stack
X -> (*X) # segundo item, '(' está na stack, poderá vir acompanhado de 'X)'
X -> (X*) # terceiro item, '(X' está na stack, poderá vir acompanhado de ')'
X -> (X)* # quarto item, parte direita da produção, fazer redução
```

### SLR

O algoritmo preferencialmente reduz o input na stack, mas como podem existir vários conflitos *shift/reduce* apenas vai reduzir quando o que vier a seguir estiver no conjunto Follow dos símbolos.

## Syntax-Directed Definitions

Há necessidade de fazer testes de correção nos programas que não podem ser verificados apenas com a gramática. Precisa de saber o tipo das variáveis, como podem ser usadas, variáveis atribuídas mas não declaradas, acesso a zonas fora do array, número de argumentos em cada função, tipos de retorno, entre outros.

### Attribute Grammars

Gramática livre de contexto aumentada com um conjunto de regras. Cada símbolo (terminal e não terminal) possui um conjunto de valores e atributos. As regras explicitam como computar o valor para cada atributo e implicitamente definem um grafo de dependências das operações.
- Atributos herdados: usam regras e valores dos nós superiores (pais). Pode haver dependências laterais, o que torna mais complexo.
- Atributos sintetizados: usam regras e valores dos nós inferiores (filhos). Funcionam com *LR parsing* porque conseguem fazer a análise bottom-up.

Permite calcular valores com base na sintaxe, realizar computação com os valores atribuídos, inserir testes lógicos. O grafo de dependências das regras deve ser **acíclico**.<br>
Como a complexidade aumenta rapidamente com as restrições de memória e custo associados a cada operação load e store, esta técnica só é usado para `type checking`.

## Syntax-Directed Translations

Processo de tradução guiado pela gramática livre de contexto, associando atributos e regras de semântica com produções que usam valores desses mesmos atributos. Os atributos podem ser:
- `Herdados`, se os seus valores se propagarem de cima para baixo, ou seja, dos pais para os filhos;
- `Sintetizados`, se os seus valores se propagarem de baixo para cima, ou seja, dos filhos para os pais;