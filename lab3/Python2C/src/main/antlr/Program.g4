grammar Program;

// Parser rules

program : indent line (NEWLINE indent line?)* EOF ;

line : (print | if_ | else_ | declaration) ;

indent : WHITESPACE? ;

declaration : (int_declaration
             | boolean_declaration
             | char_declaration
             | string_declaration
             | custom_declaration
             | input_declaration
             | int_input_declaration
             | int_expression_declaration
             | boolean_expression_declaration
             | swap_declaration ) ;

int_declaration : variable_assignment INT WHITESPACE? ;
char_declaration : variable_assignment CHAR WHITESPACE? ;
boolean_declaration : variable_assignment BOOLEAN WHITESPACE? ;
string_declaration : variable_assignment STRING WHITESPACE? ;
custom_declaration : variable_assignment VARIABLE WHITESPACE? ;
input_declaration : input_variable_declaration input_call WHITESPACE? ;
int_input_declaration : input_variable_declaration int_input_call WHITESPACE? ;
int_expression_declaration : variable_assignment int_e WHITESPACE? ;
boolean_expression_declaration : variable_assignment boolean_b WHITESPACE? ;
swap_declaration : declared_variable
                    WHITESPACE?
                    (',' WHITESPACE? declared_variable WHITESPACE?)+
                    '='
                    WHITESPACE?
                    declared_variable
                    WHITESPACE?
                    (',' WHITESPACE? declared_variable WHITESPACE?)+ ;


variable_assignment : VARIABLE WHITESPACE? '=' WHITESPACE? ;
input_variable_declaration : VARIABLE WHITESPACE? '=' WHITESPACE? ;

input_call : 'input' WHITESPACE? '(' WHITESPACE? ')'  ;
int_input_call : 'int' WHITESPACE? '(' WHITESPACE? 'input' WHITESPACE? '(' WHITESPACE? ')' WHITESPACE? ')' ;

int_e : int_t WHITESPACE? int_e_prime ;
int_e_prime : (('+' | '-') WHITESPACE? int_t WHITESPACE? int_e_prime WHITESPACE? | );
int_t : int_f WHITESPACE? int_t_prime ;
int_t_prime : (('*' | '//') WHITESPACE? int_f WHITESPACE? int_t_prime | );
int_f : (int_e_parentheses  | VARIABLE | INT );
int_e_parentheses : '(' WHITESPACE? int_e WHITESPACE? ')' ;
int_variable : VARIABLE ;


boolean_b : boolean_d WHITESPACE? boolean_b_prime ;
boolean_b_prime : ('or' WHITESPACE? boolean_d WHITESPACE? boolean_b_prime | );
boolean_d : boolean_c WHITESPACE? boolean_d_prime ;
boolean_d_prime : ('and' WHITESPACE? boolean_c WHITESPACE? boolean_d_prime | );
boolean_c : (boolean_b_parentheses
           | BOOLEAN
           | boolean_variable
           | 'not' WHITESPACE? boolean_b
           | comparison ) ;
boolean_b_parentheses : '(' WHITESPACE? boolean_b WHITESPACE?  ')' ;
boolean_variable : VARIABLE ;

any : ( VARIABLE | int_e | BOOLEAN | CHAR | STRING ) ;
comparison : any WHITESPACE? COMPARISON WHITESPACE? any ;



print : 'print' WHITESPACE? '(' WHITESPACE? (printTail | ')') ;
printTail  : (printAny WHITESPACE? ')' | (printAny WHITESPACE? ',' WHITESPACE? printTail)) ;
printAny : (declared_variable | INT | CHAR | BOOLEAN | STRING) ;
declared_variable : VARIABLE ;



if_ : 'if' WHITESPACE? if_statement WHITESPACE? ':' ; // update indent size
if_statement : boolean_b | any | comparison ; // for "(...) {"
else_ : 'else' WHITESPACE? ':'; //update indent size

// Lexer rules

NEWLINE : ('\r'? '\n' | '\r') ;
WHITESPACE : [ \t]+;

COMPARISON : '<=' | '<' | '>=' | '>' | '==' | '!=' ;

INT : ('+' | '-')? [0-9]+ ;
CHAR : '\'' [a-zA-Z0-9_\-+] '\'' ;
BOOLEAN : 'True' | 'False' ;
STRING : '"' [a-zA-Z0-9_\-+ !?.,:]* '"' ;

VARIABLE : [a-zA-Z_] [a-zA-Z_0-9]* ;
