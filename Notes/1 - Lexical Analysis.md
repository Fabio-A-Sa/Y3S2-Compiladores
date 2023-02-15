# Análise Lexical

O *Front End* do compilador transforma o *source code* numa representação interna se for um programa válido a nível sintático. É constituído por duas partes:

- o `scanner` faz o reconhecimento de sequências de caracteres para formar palavras **tokens**, ignora espaços e comentários. Necessita de uma especificação, como tabelas com expressões regulares, que implementam funções que reconhece o formato das palavras;
- o `parser` verifica se o programa é válido a nível sintático através dos tokens e de uma CFG (*context-free grammar*).   

## Expressões Regulares

Podem ser usadas para especificar palavras que traduzam partes da linguagem do *source code*. São implementadas por DFAs (autómato determinístico finito) após um passo intermédio: transformação da expressão regular num NFA (autómato não-determinístico finito).

- r+ (uma ou mais ocorrências de r)
- r? (zero ou uma ocorrências de r)
- r* (zero ou mais ocorrências de r, a sua linguagem inclui "e")

É necessário transformar os autómatos NFA em DFA para poderem ser programados. `E-closure` de um estado é o conjunto de estados em que a máquina poderá estar a partir desse seguindo apenas transições "e". No pior dos casos, a tradução de um NFA com N estados pode originar um DFA de 2^N estados.

