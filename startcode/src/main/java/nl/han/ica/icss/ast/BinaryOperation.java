package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;

import java.util.Optional;

public abstract class BinaryOperation extends Operation {

    public BinaryOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    public BinaryOperation() {
        super();
    }

    protected abstract Optional<SemanticError> validateBinaryExpressionInternal(ExpressionType lhs, ExpressionType rhs);

    @Override
    protected Optional<SemanticError> validateExpressionInternal(ExpressionType lhs, ExpressionType rhs) {
        if (lhs == ExpressionType.COLOR || rhs == ExpressionType.COLOR) { // can never do a binary expression on colors
            return Optional.of(new SemanticError("Cannot apply a '" + getNodeLabel() + "' operation with a color."));
        }

        return validateBinaryExpressionInternal(lhs, rhs);
    }
}
