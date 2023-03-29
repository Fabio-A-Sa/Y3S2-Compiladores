# Intermediate representation and code generation

Transformação de uma AST numa representação intermédia com algoritmos de complexidade O(n). A representação intermédia é suportada por uma stack com memória infinita ou uma árvore com infinitos registos. Se os recursos fossem finitos, o algoritmo seria NP-Hard.

Usa-se uma representação intermédia para que seja comum a várias linguagens e a várias máquinas. Preserva a semântica do programa original. Existem vários níveis de representação intermédia, de baixo nível e de alto nível.

## Three-Address Instructions IR

Existe uma pilha para passar os argumentos pela função. Os saltos podem ser condicionais ou incodicionais. Exemplo:

```c
y = p(a, b+1)

int p(x, z) {
    return x + z;
}

do {
    i = i + 1;
} while (a[i] < V);
```

O código em C é equivalente a este código com *tree-address instructions*:

```note
t1 = a
t2 = b + 1
putparam t1 // colocar na pilha os parâmetros por ordem
putparam t2
y = call p, 2 // chamar a função p com os dois primeiros argumentos na pilha

p:
getparam z // ir buscar à pilha os parâmetros pela ordem inversa
getparam x
t3 = x + z
return t3
return 

L: 
t1 = i + 1
t2 = i * 8 // cada elemento pode ter 8 bytes, offset
t3 = a[t2]
if t3 < V goto L
```