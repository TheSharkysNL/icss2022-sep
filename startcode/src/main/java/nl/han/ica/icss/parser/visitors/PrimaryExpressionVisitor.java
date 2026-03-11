package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.parser.ICSSParser;

public class PrimaryExpressionVisitor extends ExpressionLiteralVisitor {
    @Override
    public final Expression visitVariableIdent(ICSSParser.VariableIdentContext ctx) {
        return new VariableReference(ctx.CAPITAL_IDENT().getText());
    }
}
