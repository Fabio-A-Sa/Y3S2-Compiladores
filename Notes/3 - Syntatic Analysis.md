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

