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

Quando existem duas ou mais derivações formalmente correctas para o mesmo input. A iliminação da ambiguidade é importante para não levar a erros ou não determinismos. Muitas vezes a solução passa por introduzir na gramática livre do contexto outros símbolos não terminais para forçar a associação à direita ou à esquerda.

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

É necessário também usar *predictive parsing* para minimizar o número de backtracking devido a erros na escolha das produções da gramática.

<TODO> até 32

### Bottom-Up Parsers

Começamos nas folhas (símbolos terminais) e comprime de acordo com as produções da gramática, também da esquerda para a direita. É um algoritmo mais complexo mas também consegue processar mais gramáticas.