grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
selector: LOWER_IDENT #tagSelector | ID_IDENT #idSelector | CLASS_IDENT #classSelector;

rule: LOWER_IDENT COLON expression SEMICOLON;

ifStatement: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE ruleStatement* CLOSE_BRACE elseStatement?;
elseStatement: ELSE OPEN_BRACE ruleStatement* CLOSE_BRACE;

ruleStatement: rule | ifStatement;

style: selector+ OPEN_BRACE ruleStatement* CLOSE_BRACE;

expressionLit
    : COLOR           #colorLiteral
    | PIXELSIZE       #pixelLiteral
    | PERCENTAGE      #percentageLiteral
    | SCALAR          #scalarLiteral
    | TRUE            #trueLiteral
    | FALSE           #falseLiteral
    ;
primaryExpression: CAPITAL_IDENT #variableIdent | expressionLit #expressionLiteral;
multiplicativeExpression
    : primaryExpression
    | multiplicativeExpression MUL primaryExpression;
additiveExpression
    : multiplicativeExpression
    | additiveExpression PLUS multiplicativeExpression
    | additiveExpression MIN multiplicativeExpression;

expression: additiveExpression;
variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;

statement: style | variableAssignment;

stylesheet: statement+|EOF;

