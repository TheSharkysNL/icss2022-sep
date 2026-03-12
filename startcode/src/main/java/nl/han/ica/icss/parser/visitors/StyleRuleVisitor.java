package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.ArrayList;
import java.util.List;

public class StyleRuleVisitor extends ICSSBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitRule(ICSSParser.RuleContext rule) {
        Expression expression = rule.expression()
                .accept(new ExpressionVisitor());

        String property = rule.LOWER_IDENT().getText();

        return new Declaration(property, expression);
    }

    @Override
    public ASTNode visitIfStatement(ICSSParser.IfStatementContext ifStmt) {
        Expression expression = ifStmt.expression()
                .accept(new ExpressionVisitor());

        List<ASTNode> body = ifStmt.ruleStatement()
                .stream()
                .map(ruleStmt -> ruleStmt.accept(this))
                .toList();

        ElseClause elseClause;
        if (ifStmt.elseStatement() != null) {
            List<ASTNode> elseBody = ifStmt.elseStatement().ruleStatement()
                    .stream()
                    .map(ruleStmt -> ruleStmt.accept(this))
                    .toList();

            elseClause = new ElseClause(elseBody);
        } else {
            elseClause = new ElseClause(new ArrayList<>());
        }

        return new IfClause(expression, body, elseClause);
    }
}
