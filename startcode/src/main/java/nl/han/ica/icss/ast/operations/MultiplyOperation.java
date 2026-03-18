package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.Optional;

public class MultiplyOperation extends Operation {

    public MultiplyOperation() {
        super();
    }


    public MultiplyOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.mul(b);
    }

    @Override
    protected Optional<SemanticError> validateExpressionInternal(ExpressionType lhs, ExpressionType rhs) {
        return switch (new Tuple<>(lhs, rhs)) {
            case Tuple(ExpressionType l, ExpressionType ignored) when l == ExpressionType.SCALAR -> Optional.empty();
            case Tuple(ExpressionType ignored, ExpressionType r) when r == ExpressionType.SCALAR -> Optional.empty();
            default -> Optional.of(new SemanticError("Cannot multiply expression of type: '" + lhs + "' with type: '" + rhs + "'"));
        };
    }

    @Override
    public String getNodeLabel() {
        return "Multiply";
    }
}
