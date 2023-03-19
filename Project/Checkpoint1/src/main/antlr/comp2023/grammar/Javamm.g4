grammar Javamm;

@header {
    package pt.up.fe.comp2023;
}

INTEGER : [0] | ([1-9][0-9]*);
ID : [a-zA-Z_$]([a-zA-Z_0-9$])* ;

SINGLE_COMMENT : '//' .*? '\n' -> skip ;
MULTI_COMMENT : '/*' .*? '*/' -> skip ;
WS : [ \t\n\r\f]+ -> skip ;

program
    : statement + EOF
    | (importDeclaration)* classDeclaration EOF
    ;

importDeclaration
    : 'import' value+=ID ('.' value+=ID)* ';' #ImportStmt
    ;

classDeclaration
    : 'class' className=ID ('extends' extendedClass=ID)? '{' (varDeclaration)* (methodDeclaration)* '}' #ClassStmt
    ;

varDeclaration
    : type name=ID ';'
    | type name=ID op='[' op=']' ';'
    ;

type
    : type '[' ']'        #Array
    | value='double'      #Double
    | value='float'       #Float
    | value='boolean'     #Boolean
    | value='int'         #Int
    | value='String'      #String
    | value=ID            #Id
    ;

returnType
    : type
    ;

argument
    : type argName=ID (',' argument)*
    ;

returnStmt
    : 'return' expression ';'
    ;

methodDeclaration
    : ('public')? returnType methodName=ID '(' (argument)* ')' '{' (varDeclaration)* (statement)* returnStmt '}'
    | ('public')? 'static' 'void' 'main' '(' 'String' '['']' argName=ID ')' '{' (varDeclaration)* (statement)* '}'
    ;

statement
    : expression ';' #ExprStmt
    | '{' ( statement )* '}' #Brackets
    | ifExpr (elseifExpr)* (elseExpr)? #IfStmt
    | 'for' '(' statement expression ';' expression ')' statement #ForStmt
    | 'while' '(' expression ')' statement #WhileStmt
    | var=ID '=' expression ';' #Assignment
    | var=ID '[' expression ']' '=' expression ';' #ArrayAssign
    ;

ifExpr
    : 'if' '(' expression ')' statement;

elseifExpr
    : 'else if' '(' expression ')' statement;

elseExpr
    : 'else' statement;

expression
    : '(' expression ')' #Parentesis
    | 'new' 'int' '[' expression ']' #ArrayDeclaration
    | 'new' classname=ID '(' (expression (',' expression) *)? ')' #NewClass
    | expression '[' expression ']' #ArraySubscript
    | className=ID expression   #ClassInstantiation
    | expression '.' value=ID '(' (expression (',' expression) *)? ')' #FunctionCall
    | expression '.' 'length' #Length
    | value = 'this' #Object
    | value = '!' expression #Negation
    | expression op=('*' | '/') expression #BinaryOp
    | expression op=('+' | '-') expression #BinaryOp
    | expression op=('<' | '>') expression #BinaryOp
    | expression op=('<=' | '>=' | '==' | '!=' | '+=' | '-=' | '*=' | '/=') expression #BinaryOp
    | expression op='&&' expression #BinaryOp
    | expression op='||' expression #BinaryOp
    | value=INTEGER #Integer
    | value = 'true' #Identifier
    | value = 'false' #Identifier
    | value=ID #Identifier
    | value=ID op=('++' | '--') #Increment
    ;