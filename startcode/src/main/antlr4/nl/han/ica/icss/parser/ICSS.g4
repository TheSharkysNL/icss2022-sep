grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';

// Function support:
FN: 'fn';
RETURN: 'return';
COMMA: ',';

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

ifStatement: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE statement* CLOSE_BRACE elseStatement?;
elseStatement: ELSE OPEN_BRACE statement* CLOSE_BRACE;

style: selector+ OPEN_BRACE statement* CLOSE_BRACE;

// function declaration
//return: RETURN expression SEMICOLON;
//paramater: CAPITAL_IDENT;
//parameter_list: paramater | parameter_list COMMA paramater;
//functionDeclaration: FN CAPITAL_IDENT BOX_BRACKET_OPEN parameter_list BOX_BRACKET_CLOSE OPEN_BRACE statement* CLOSE_BRACE;

expressionLit
    : COLOR           #colorLiteral
    | PIXELSIZE       #pixelLiteral
    | PERCENTAGE      #percentageLiteral
    | SCALAR          #scalarLiteral
    | TRUE            #trueLiteral
    | FALSE           #falseLiteral
    ;
primaryExpression: CAPITAL_IDENT #variableIdent | expressionLit #expressionLiteral | (OPEN_BRACE expression CLOSE_BRACE) #braceExpression;

// function call
//postfixExpression
//    : primaryExpression
//    | postfixExpression BOX_BRACKET_OPEN expression_list BOX_BRACKET_CLOSE;

multiplicativeExpression
    : primaryExpression
    | multiplicativeExpression MUL primaryExpression;
additiveExpression
    : multiplicativeExpression
    | additiveExpression PLUS multiplicativeExpression
    | additiveExpression MIN multiplicativeExpression;

expression: additiveExpression;
//expression_list: expression | expression_list COMMA expression;
variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;

statement: style | variableAssignment | ifStatement | rule;

stylesheet: statement+|EOF;