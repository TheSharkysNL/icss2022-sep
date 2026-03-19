package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.function.FunctionCall;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.ArrayList;
import java.util.List;

public class PostFixExpressionVisitor extends PrimaryExpressionVisitor {
    @Override
    public final Expression visitFunctionCallExpression(ICSSParser.FunctionCallExpressionContext ctx) {
        String name = ctx.CAPITAL_IDENT().getText();

        List<Expression> arguments = StatementVisitor.getList(
                ctx.expressionList(),
                ICSSParser.ExpressionListContext::expressionList,
                e -> e.expression().accept(new ExpressionVisitor())
        );

        return new FunctionCall(name, arguments);
    }

    @Override
    public final Expression visitNormalPrimaryExpression(ICSSParser.NormalPrimaryExpressionContext ctx) {
        return ctx.primaryExpression()
                .accept(this);
    }
}
