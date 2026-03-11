package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.List;

public class StatementVisitor extends ICSSBaseVisitor<ASTNode> {
    @Override
    public final ASTNode visitStyle(ICSSParser.StyleContext ctx) {
        List<Selector> selectors = ctx.selector()
                .stream()
                .map(selector -> selector.accept(new SelectorVisitor()))
                .toList();

        List<ASTNode> ruleVisitor = ctx.ruleStatement()
                .stream()
                .map(rule -> rule.accept(new StyleRuleVisitor()))
                .toList();

        return new Stylerule(selectors, ruleVisitor);
    }

    @Override
    public final ASTNode visitVariableAssignment(ICSSParser.VariableAssignmentContext variable) {
        String name = variable.CAPITAL_IDENT().getText();
        Expression expression = variable.expression()
                .accept(new ExpressionVisitor());

        return new VariableAssignment(new VariableReference(name), expression);
    }
}
