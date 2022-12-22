grammar Program;

// Parser rules

program : line (NEWLINE line)* EOF ;

line : (declaration | print) ;

declaration : (int_declaration
             | boolean_declaration
             | char_declaration
             | string_declaration
             | custom_declaration
             | input_declaration
             | int_input_declaration ) ;

int_declaration : variable_assignment INT WHITESPACE? ;
char_declaration : variable_assignment CHAR WHITESPACE? ;
boolean_declaration : variable_assignment BOOLEAN WHITESPACE? ;
string_declaration : variable_assignment STRING WHITESPACE? ;
custom_declaration : variable_assignment VARIABLE WHITESPACE? ;
input_declaration : string_variable_declaration input_call WHITESPACE? ;
int_input_declaration : int_variable_declaration int_input_call WHITESPACE? ;

variable_assignment : VARIABLE WHITESPACE? '=' WHITESPACE? ;
string_variable_declaration : VARIABLE WHITESPACE? '=' WHITESPACE? ;
int_variable_declaration : VARIABLE WHITESPACE? '=' WHITESPACE? ;
input_call : 'input()'  ;
int_input_call : 'int(input())' ;

print : 'print(' printTail ;
printTail  : (')' | (printAny ',' printTail)) ;
printAny : (VARIABLE | INT | CHAR | BOOLEAN | STRING) ;

// Lexer rules

NEWLINE : ('\r'? '\n' | '\r')+ ;
WHITESPACE : [ \t]+ -> skip ;


INT : ('+' | '-')? [0-9]+ ;
CHAR : '\'' [a-zA-Z0-9_\-+] '\'' ;
BOOLEAN : 'True' | 'False' ;
STRING : '"' [a-zA-Z0-9_\-+]* '"' ;

VARIABLE : [a-zA-Z_] [a-zA-Z_0-9]* ;
