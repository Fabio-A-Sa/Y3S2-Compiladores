# Introdução

## Compilador vs. Interpretador

O compilador transforma um executável escrito numa linguagem em outro executável noutra linguagem. Nessa transformação consegue às vezes melhorar o programa. C é um exemplo de linguagem compilada. <br>
Já o interpretador apenas lê um executável e produz os resultados da sua execução. Python é um exemplo de linguagem interpretada.

Java usa as duas versões, numa `Just-in-time compilation`: compila as partes do código mais utilizadas, para aumento de performance.

## Funcionamento de um Compilador

O `Front End` é responsável por transformar o código de raiz numa representação intermédia (IR), sinalizando os erros de sintaxe que possam estar contidos.

![Compilador](../Images/Compiler.png)

A representação interna serve para termos uma zona comum em todas as linguagens, permitindo generalizar o problema por motivos de eficiência e compatibilidade.

O `Back End` é responsável por transformar a representação intermédia em código máquina, sinalizando os erros de semântica que possam estar contidos.


