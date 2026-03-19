package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.operations.NegateOperation;
import nl.han.ica.icss.parser.ICSSParser;

public class PrefixExpressionVisitor extends PostFixExpressionVisitor {
    @Override
    public final Expression visitNormalPostfixExpression(ICSSParser.NormalPostfixExpressionContext ctx) {
        return ctx.postfixExpression()
                .accept(this);
    }

    @Override
    public final Expression visitNegateExpression(ICSSParser.NegateExpressionContext ctx) {
        Expression expression = ctx.postfixExpression().accept(new ExpressionVisitor());

        return new NegateOperation(expression);
    }
}
