package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.BinaryOperation;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.Optional;

public class SubtractOperation extends BinaryOperation {

    public SubtractOperation() {
        super();
    }


    public SubtractOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.sub(b);
    }

    @Override
    protected Optional<SemanticError> validateBinaryExpressionInternal(ExpressionType lhs, ExpressionType rhs) {
        if (lhs != rhs) {
            return Optional.of(new SemanticError("Cannot subtract expression of type: '" + lhs + "' with type: '" + rhs + "'"));
        }

        return Optional.empty();
    }

    @Override
    public String getNodeLabel() {
        return "Subtract";
    }
}
