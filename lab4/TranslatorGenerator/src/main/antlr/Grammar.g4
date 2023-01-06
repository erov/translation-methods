grammar Grammar;


file: (grammar_rule NEWLINE+)*;

grammar_rule: (parser_rule | lexer_rule) (FLPAREN NEWLINE? attr_assign FRPAREN)?;
lexer_rule: non_terminal ARROW terminal;
parser_rule: non_terminal ARROW (non_terminal | terminal | translation_symbol) (non_terminal | terminal | translation_symbol)+;

non_terminal: NON_TERMINAL;
NON_TERMINAL: [a-zA-Z0-9_]+;

terminal: STRING | REGEXP;
STRING: '\'' [a-zA-Z0-9~!@#$%^&*()_+\-=\\/]* '\'';
REGEXP: '[' [a-zA-Z0-9~!@#$%^&*()_+\-=\\/]* ']' ('+' | '*' | '?');

translation_symbol: TRANSLATION_SYMBOL;
TRANSLATION_SYMBOL: '$' [A-Z]+;

attr_assign: (attr_assign_line NEWLINE)+;
attr_assign_line: ATTRIBUTE SKIP_EQ ATTRIBUTE;
ATTRIBUTE: [a-zA-Z0-9_.$]+;


NEWLINE: ('\r'? '\n' | '\r')+;
WHITESPACE: [ \t]+ -> skip;
ARROW: '->';
FLPAREN: '{';
FRPAREN: '}';
SKIP_EQ: '=';


