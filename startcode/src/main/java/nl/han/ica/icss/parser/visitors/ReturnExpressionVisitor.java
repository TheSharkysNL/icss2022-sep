package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.function.ExpressionReturnStatement;
import nl.han.ica.icss.ast.function.ReturnStatement;
import nl.han.ica.icss.ast.function.StyleReturnStatement;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

public class ReturnExpressionVisitor extends ICSSBaseVisitor<ReturnStatement> {
    @Override
    public ReturnStatement visitReturnExpr(ICSSParser.ReturnExprContext ctx) {
        return new ExpressionReturnStatement(ctx.accept(new ExpressionVisitor()));
    }

    @Override
    public ReturnStatement visitReturnStyle(ICSSParser.ReturnStyleContext ctx) {
        ASTNode node = ctx.accept(new StatementVisitor());
        if (!(node instanceof Stylerule rule)) {
            throw new RuntimeException("Should never happen as anltr syntax doesn't allow any other statement than the style rule statement");
        }

        return new StyleReturnStatement(rule);
    }
}
