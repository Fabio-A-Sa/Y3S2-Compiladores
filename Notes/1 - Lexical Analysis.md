# Análise Lexical

O *Front End* do compilador transforma o *source code* numa representação interna se for um programa válido a nível sintático. É constituído por duas partes:

- o `scanner` faz o reconhecimento de sequências de caracteres para formar palavras **tokens**, ignora espaços e comentários. Necessita de uma especificação, como tabelas com expressões regulares, que implementam funções que reconhece o formato das palavras;
- o `parser` verifica se o programa é válido a nível sintático através dos tokens e de uma CFG (*context-free grammar*).   

## Expressões Regulares

Podem ser usadas para especificar palavras que traduzam partes da linguagem do *source code*. São implementadas por DFAs (autómato determinístico finito).

