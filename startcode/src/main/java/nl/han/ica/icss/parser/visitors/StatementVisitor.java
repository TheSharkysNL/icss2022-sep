package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    @Override
    public ASTNode visitReturn(ICSSParser.ReturnContext ctx) {
        return ctx.returnExpression()
                .accept(new ReturnExpressionVisitor());
    }

    @Override
    public ASTNode visitFunctionDeclaration(ICSSParser.FunctionDeclarationContext ctx) {
        String name = ctx.CAPITAL_IDENT().getText();

        List<String> parameters = getList(
                ctx.parameterList(),
                ICSSParser.ParameterListContext::parameterList,
                p -> p.parameter().CAPITAL_IDENT().getText()
        );

        List<ASTNode> body = ctx.statement()
                .stream()
                .map(stmt -> stmt.accept(this))
                .toList();

        return new FunctionDeclaration(name, parameters, body);
    }

    @Override
    public ASTNode visitExpression(ICSSParser.ExpressionContext ctx) {
        return ctx.comparisonExpression().accept(new ExpressionVisitor());
    }

    @Override
    public ASTNode visitFor(ICSSParser.ForContext forContext) {
        List<VariableAssignment> initialVariableAssignments = getList(
                forContext.init,
                ICSSParser.VariableAssignmentListContext::variableAssignmentList,
                // must be of type variable assignment here
                v -> (VariableAssignment)v.variableAssignment().accept(this)
        );

        Expression loopExpression = forContext.expression()
                .accept(new ExpressionVisitor());

        List<VariableAssignment> loopVariableAssignments = getList(
                forContext.loop,
                ICSSParser.VariableAssignmentListContext::variableAssignmentList,
                // must be of type variable assignment here
                v -> (VariableAssignment)v.variableAssignment().accept(this)
        );

        List<ASTNode> body = forContext
                .statement()
                .stream()
                .map(stmt -> stmt.accept(this))
                .toList();

        return new ForStatement(initialVariableAssignments, loopExpression, loopVariableAssignments, body);
    }

    public static <TIn, TOut> List<TOut> getList(TIn in, Function<TIn, TIn> next, Function<TIn, TOut> output) {
        TIn current = in;
        ArrayList<TOut> out = new ArrayList<>();

        while (current != null) {
            TOut value = output.apply(current);
            out.add(value);

            current = next.apply(current);
        }

        return out;
    }

    @Override
    protected ASTNode aggregateResult(ASTNode aggregate, ASTNode nextResult) {
        return nextResult != null ? nextResult : aggregate;
    }
}
