package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.function.FunctionCall;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.ArrayList;

public class PostFixExpressionVisitor extends PrimaryExpressionVisitor {
    @Override
    public Expression visitFunctionCallExpression(ICSSParser.FunctionCallExpressionContext ctx) {
        String name = ctx.CAPITAL_IDENT().getText();

        ArrayList<Expression> arguments = new ArrayList<>();
        ICSSParser.ExpressionListContext current = ctx.expressionList();
        while (current != null) {
            Expression expression = current.expression()
                    .accept(new ExpressionVisitor());
            arguments.add(expression);

            current = current.expressionList();
        }

        return new FunctionCall(name, arguments);
    }

    @Override
    public Expression visitNormalPrimaryExpression(ICSSParser.NormalPrimaryExpressionContext ctx) {
        return ctx.primaryExpression()
                .accept(this);
    }
}
