package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.parser.ICSSParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ExpressionVisitor extends PrefixExpressionVisitor {
    @FunctionalInterface
    interface BinaryExpressionCreator {
        Expression create(Expression lhs, Expression rhs, Token operator);
    }


    private Expression createBinaryExpression(ParserRuleContext context, BinaryExpressionCreator func) {
        Expression lhs = context.getChild(0)
                .accept(this);
        ParseTree rhs = context.getChild(2);

        if (rhs == null) {
            return lhs;
        }

        Expression rhsExpression = rhs.accept(this);
        Token operatorToken = ((TerminalNode)context.getChild(1)).getSymbol();

        return func.create(lhs, rhsExpression, operatorToken);
    }

    @Override
    public final Expression visitAdditiveExpression(ICSSParser.AdditiveExpressionContext additive) {
        return createBinaryExpression(additive,
            (lhs, rhs, operator) -> switch (operator.getType()) {
                case ICSSParser.PLUS -> new AddOperation(lhs, rhs);
                case ICSSParser.MIN -> new SubtractOperation(lhs, rhs);
                default -> throw new RuntimeException("Should never happen as antlr should prevent this. If this does happen that means the antlr syntax has changed.");
            }
        );
    }

    @Override
    public final Expression visitMultiplicativeExpression(ICSSParser.MultiplicativeExpressionContext multiplicative) {
        return createBinaryExpression(multiplicative,
                (lhs, rhs, operator) -> new MultiplyOperation(lhs, rhs)
        );
    }

    @Override
    public Expression visitComparisonExpression(ICSSParser.ComparisonExpressionContext comparison) {
        return createBinaryExpression(comparison, EqualityOperation::new);
    }

    @Override
    public Expression visitExpression(ICSSParser.ExpressionContext ctx) {
        return ctx.comparisonExpression()
                .accept(this);
    }
}
