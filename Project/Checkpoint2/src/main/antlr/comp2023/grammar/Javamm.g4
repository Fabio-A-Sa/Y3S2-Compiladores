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
    | value='void'        #Void
    | value=ID            #Id
    ;

returnType
    : type
    ;

argument
    : type argName=ID
    ;

returnStmt
    : 'return' expression ';'
    ;

methodDeclaration
    : 'public'? (isStatic='static')? returnType methodName=ID '(' argument? (',' argument)* ')' '{' (varDeclaration | statement)* returnStmt '}'
    | 'public'? 'static' 'void' 'main' '(' 'String' '['']' argName=ID ')' '{' (varDeclaration | statement)* '}'
    ;

statement
    : expression ';' #ExprStmt
    | '{' ( statement )* '}' #Brackets
    | ifExpr (elseifExpr)* (elseExpr)? #IfStmt
    | 'for' '(' statement expression ';' expression ')' statement #ForStmt
    | 'while' '(' expression ')' statement #WhileStmt
    | expression '=' expression ';' #Assignment
    ;

ifExpr
    : 'if' '(' expression ')' statement;

elseifExpr
    : 'else if' '(' expression ')' statement;

elseExpr
    : 'else' statement;

parameter
    : expression (',' expression) *
    ;


expression
    : '(' expression ')' #Parentesis
    | 'new' 'int' '[' expression ']' #ArrayDeclaration
    | 'new' classname=ID '(' (expression (',' expression) *)? ')' #NewClass
    | expression '[' expression ']' #ArrayAccess
    | name=ID '(' parameter? ')' #FunctionCall
    | expression '.' name=ID '(' parameter? ')' #FunctionCall
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