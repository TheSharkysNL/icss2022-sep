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

// For support:
FOR: 'for';

// import support:
IMPORT: 'import';
FROM: 'from';
FUNCTIONS: 'functions';
STYLES: 'styles';
STRING: '"' .*? '"';
AND: 'and';

// switch support
CASE: 'case';
ARROW: '->';

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

GT: '>';
LT: '<';
GE: '>=';
LE: '<=';
EQ: '==';
NE: '!=';
NEGATE: '!';

COMMENT: '//' ~( '\r' | '\n' )* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

//--- PARSER: ---
selector: LOWER_IDENT #tagSelector | ID_IDENT #idSelector | CLASS_IDENT #classSelector;

declaration: LOWER_IDENT COLON expression SEMICOLON;

ifStatement: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE statement* CLOSE_BRACE elseStatement?;
elseStatement: ELSE OPEN_BRACE statement* CLOSE_BRACE;

style: selector+ OPEN_BRACE statement* CLOSE_BRACE;

// function declaration
returnExpression: expression #returnExpr | style #returnStyle;
return: RETURN returnExpression SEMICOLON;
parameter: CAPITAL_IDENT;
parameterList: parameter | parameterList COMMA parameter;
functionDeclaration: FN CAPITAL_IDENT BOX_BRACKET_OPEN parameterList? BOX_BRACKET_CLOSE OPEN_BRACE statement* CLOSE_BRACE;

// for loop
variableAssignmentList: variableAssignment | variableAssignmentList COMMA variableAssignment;
for: FOR BOX_BRACKET_OPEN init=variableAssignmentList? SEMICOLON expression SEMICOLON loop=variableAssignmentList? BOX_BRACKET_CLOSE OPEN_BRACE statement* CLOSE_BRACE;

// import
importItem: CAPITAL_IDENT #namedImportItem | MUL #wildcardImportItem;
importList: importItem | importList COMMA importItem;
importItems: OPEN_BRACE importList CLOSE_BRACE;
importType: FUNCTIONS importItems? #functionsImportType | STYLES #stylesImportType | MUL #bothImportType;
importTypeList: importType | importTypeList AND importType;
iImport: FROM STRING IMPORT importTypeList SEMICOLON;

// switch
switchCaseExpression: statement #singleStatementSwitchCase | OPEN_BRACE statement* CLOSE_BRACE #statementSwitchCase;
switchCase: expressionLit ARROW switchCaseExpression;
switchCaseList: switchCase | switchCaseList COMMA switchCase;
switch: CASE BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE switchCaseList CLOSE_BRACE;

expressionLit
    : COLOR           #colorLiteral
    | PIXELSIZE       #pixelLiteral
    | PERCENTAGE      #percentageLiteral
    | SCALAR          #scalarLiteral
    | TRUE            #trueLiteral
    | FALSE           #falseLiteral
    | BOX_BRACKET_OPEN expressionLitList BOX_BRACKET_CLOSE #tupleLiteral
    ;
expressionLitList: expressionLit | expressionLitList COMMA expressionLit;
primaryExpression: CAPITAL_IDENT #variableIdent | expressionLit #expressionLiteral | (OPEN_BRACE expression CLOSE_BRACE) #braceExpression | BOX_BRACKET_OPEN expressionList BOX_BRACKET_CLOSE #tupleExpression;

// function call
expressionList: expression | expressionList COMMA expression;
postfixExpression
    : primaryExpression #normalPrimaryExpression
    | CAPITAL_IDENT BOX_BRACKET_OPEN expressionList? BOX_BRACKET_CLOSE #functionCallExpression;
prefixExpression
    : postfixExpression #normalPostfixExpression
    | NEGATE postfixExpression #negateExpression;

multiplicativeExpression
    : prefixExpression
    | multiplicativeExpression MUL prefixExpression;
additiveExpression
    : multiplicativeExpression
    | additiveExpression PLUS multiplicativeExpression
    | additiveExpression MIN multiplicativeExpression;

comparisonExpression
    : additiveExpression
    | comparisonExpression EQ additiveExpression
    | comparisonExpression NE additiveExpression
    | comparisonExpression GT additiveExpression
    | comparisonExpression GE additiveExpression
    | comparisonExpression LT additiveExpression
    | comparisonExpression LE additiveExpression;

expression: comparisonExpression | switch;
variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression;

statement: style | variableAssignment SEMICOLON | ifStatement | declaration | return | functionDeclaration | expression SEMICOLON | for | iImport | switch;

stylesheet: statement+|EOF;