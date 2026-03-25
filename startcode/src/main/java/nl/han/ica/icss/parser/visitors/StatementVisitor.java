package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
import nl.han.ica.icss.ast.iImport.ImportItem;
import nl.han.ica.icss.ast.iImport.ImportStatement;
import nl.han.ica.icss.ast.iSwitch.SwitchCase;
import nl.han.ica.icss.ast.iSwitch.SwitchRuleList;
import nl.han.ica.icss.ast.iSwitch.SwitchStatement;
import nl.han.ica.icss.ast.iSwitch.rules.SwitchRule;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class StatementVisitor extends ICSSBaseVisitor<ASTNode> {
    @Override
    public final ASTNode visitStyle(ICSSParser.StyleContext ctx) {
        List<Selector> selectors = ctx.selector()
                .stream()
                .map(selector -> selector.accept(new SelectorVisitor()))
                .toList();

        List<ASTNode> statements = StatementVisitor.getStatements(ctx.statement());

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

        List<ASTNode> body = StatementVisitor.getStatements(ifStmt.statement());

        ElseClause elseClause = null;
        if (ifStmt.elseStatement() != null) {
            List<ASTNode> elseBody = StatementVisitor.getStatements(ifStmt.elseStatement().statement());

            elseClause = new ElseClause(elseBody);
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

        List<ASTNode> body = StatementVisitor.getStatements(ctx.statement());

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

        List<ASTNode> body = StatementVisitor.getStatements(forContext.statement());

        return new ForStatement(initialVariableAssignments, loopExpression, loopVariableAssignments, body);
    }

    @Override
    public ASTNode visitIImport(ICSSParser.IImportContext iImport) {
        String location = iImport.STRING().getText();
        String locationWithoutQuotes = location.substring(1, location.length() - 1);

        List<ImportStatement.ImportType> importTypes = getList(
                iImport.importTypeList(),
                ICSSParser.ImportTypeListContext::importTypeList,
                i -> i.importType().accept(new ImportTypeVisitor())
        );

        return new ImportStatement(locationWithoutQuotes, importTypes);
    }

    @Override
    public ASTNode visitSwitch(ICSSParser.SwitchContext ctx) {
        Expression expression = ctx.expression()
                .accept(new ExpressionVisitor());

        List<Tuple<SwitchRule, SwitchCase>> casesList = getList(
                ctx.switchCaseList(),
                ICSSParser.SwitchCaseListContext::switchCaseList,
                s -> {
                    SwitchRule rule = s.switchCase().switchCaseRule()
                            .accept(new SwitchRuleVisitor());

                    SwitchCase switchCase = s.switchCase().switchCaseExpression()
                            .accept(new SwitchCaseVisitor());

                    return new Tuple<>(rule, switchCase);
                }
        );

        SwitchRuleList ruleList = new SwitchRuleList(casesList.reversed());

        return new SwitchStatement(expression, ruleList);
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

    public static List<ASTNode> getStatements(List<ICSSParser.StatementContext> statements) {
        ArrayList<ASTNode> stmts = new ArrayList<>(statements.size());
        StatementVisitor visitor = new StatementVisitor();

        for (int i = 0; i < statements.size(); i++) {
            ASTNode statement = statements.get(i)
                    .accept(visitor);

            stmts.add(statement);
        }

        return stmts;
    }

    @Override
    protected ASTNode aggregateResult(ASTNode aggregate, ASTNode nextResult) {
        return nextResult != null ? nextResult : aggregate;
    }
}
