grammar Program;

// Parser rules

program : declaration (NEWLINE declaration)* EOF ;

declaration : (/*int_declaration | */boolean_declaration/* | char_declaration | string_declaration*/ ) ;

int_declaration : variable_assignment INT WHITESPACE? ;
char_declaration : variable_assignment CHAR WHITESPACE? ;
boolean_declaration : variable_assignment BOOLEAN WHITESPACE? ;
string_declaration : variable_assignment STRING WHITESPACE? ;

variable_assignment : VARIABLE WHITESPACE? ASSIGN WHITESPACE? ;


// Lexer rules

NEWLINE : ('\r'? '\n' | '\r')+ ;
WHITESPACE : [ \t]+ -> skip ;


VARIABLE : [a-z_] [a-zA-Z_0-9]* ;

ASSIGN : '=' ;

INT : ('+' | '-')? [0-9]+ ;
CHAR : '\'' [a-zA-Z0-9_\-+] '\'' ;
BOOLEAN : 'True' | 'False' ;
STRING : '"' [a-zA-Z0-9_\-+]* '"' ;
