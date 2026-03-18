package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.ArrayList;
import java.util.List;

public class StatementVisitor extends ICSSBaseVisitor<ASTNode> {
    @Override
    public final ASTNode visitStyle(ICSSParser.StyleContext ctx) {
        List<Selector> selectors = ctx.selector()
                .stream()
                .map(selector -> selector.accept(new SelectorVisitor()))
                .toList();

        List<ASTNode> statements = ctx.statement()
                .stream()
                .map(statement -> statement.accept(this))
                .toList();

        return new Stylerule(selectors, statements);
    }

    @Override
    public final ASTNode visitVariableAssignment(ICSSParser.VariableAssignmentContext variable) {
        String name = variable.CAPITAL_IDENT().getText();
        Expression expression = variable.expression()
                .accept(new ExpressionVisitor());

        return new VariableAssignment(new VariableReference(name), expression);
    }

    @Override
    public ASTNode visitIfStatement(ICSSParser.IfStatementContext ifStmt) {
        Expression expression = ifStmt.expression()
                .accept(new ExpressionVisitor());

        List<ASTNode> body = ifStmt.statement()
                .stream()
                .map(stmt -> stmt.accept(this))
                .toList();

        ElseClause elseClause;
        if (ifStmt.elseStatement() != null) {
            List<ASTNode> elseBody = ifStmt.elseStatement().statement()
                    .stream()
                    .map(stmt -> stmt.accept(this))
                    .toList();

            elseClause = new ElseClause(elseBody);
        } else {
            elseClause = new ElseClause(new ArrayList<>());
        }

        return new IfClause(expression, body, elseClause);
    }

    @Override
    public ASTNode visitDeclaration(ICSSParser.DeclarationContext rule) {
        Expression expression = rule.expression()
                .accept(new ExpressionVisitor());

        String property = rule.LOWER_IDENT().getText();

        return new Declaration(property, expression);
    }
}
