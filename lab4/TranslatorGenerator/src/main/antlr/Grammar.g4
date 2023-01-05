grammar Grammar;


file: (grammar_rule NEWLINE+)*;

grammar_rule: parser_rule | lexer_rule;
lexer_rule: non_terminal ARROW terminal;
parser_rule: non_terminal ARROW (non_terminal | terminal) (non_terminal | terminal)+;

non_terminal: NON_TERMINAL;
NON_TERMINAL: [a-z0-9_]+;

terminal: STRING | REGEXP;
STRING: '\'' [a-zA-Z0-9~!@#$%^&*()_+\-=\\/]* '\'';
REGEXP: '[' [a-zA-Z0-9~!@#$%^&*()_+\-=\\/]* ']' ('+' | '*' | '?');


NEWLINE: ('\r'? '\n' | '\r');
WHITESPACE: [ \t]+ -> skip;
ARROW: '->';
