package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.types.ExpressionType;

public class UndefinedLiteral extends Literal {
    public final static UndefinedLiteral UNDEFINED = new UndefinedLiteral();

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.undefined();
    }

    @Override
    public String getStringDisplay() {
        return "undefined";
    }
}
