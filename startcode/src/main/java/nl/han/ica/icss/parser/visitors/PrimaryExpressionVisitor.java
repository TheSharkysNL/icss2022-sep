package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.TupleExpression;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.TupleLiteral;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.List;

public class PrimaryExpressionVisitor extends ExpressionLiteralVisitor {
    @Override
    public final Expression visitVariableIdent(ICSSParser.VariableIdentContext ctx) {
        return new VariableReference(ctx.CAPITAL_IDENT().getText());
    }

    @Override
    public final Expression visitBraceExpression(ICSSParser.BraceExpressionContext ctx) {
        return ctx.expression()
                .accept(new ExpressionVisitor());
    }

    @Override
    public Expression visitTupleExpression(ICSSParser.TupleExpressionContext ctx) {
        List<Expression> expressions = StatementVisitor.getList(
                ctx.expressionList(),
                ICSSParser.ExpressionListContext::expressionList,
                e -> e.expression().accept(new ExpressionVisitor())
        );

        return new TupleExpression(expressions);
    }
}
