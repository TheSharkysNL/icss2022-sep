package nl.han.ica.icss.ast;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.ast.operations.LogicalOperation;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.Optional;
import java.util.function.BiFunction;

public abstract class NumericLiteral extends Literal {
    public abstract int getNumericValue();
    public abstract NumericLiteral fromNumericValue(int number);

    private Optional<Literal> applyOperation(Literal rhs, BiFunction<Integer, Integer, Integer> operation) {
        if (!(rhs instanceof NumericLiteral rhsNumericLit)) {
            return Optional.empty();
        }

        int rhsNumeric = rhsNumericLit.getNumericValue();
        int selfNumeric = getNumericValue();
        int afterOperation = operation.apply(selfNumeric, rhsNumeric);

        return Optional.of(fromNumericValue(afterOperation));
    }

    @Override
    public Result<Literal, EvaluationError> add(Literal rhs) {
        return applyOperation(rhs, Integer::sum)
                .map(lit -> (Result<Literal, EvaluationError>)new Result.Success<Literal, EvaluationError>(lit))
                .orElse(super.add(rhs));
    }

    @Override
    public Result<Literal, EvaluationError> sub(Literal rhs) {
        return applyOperation(rhs, (a, b) -> a - b)
                .map(lit -> (Result<Literal, EvaluationError>)new Result.Success<Literal, EvaluationError>(lit))
                .orElse(super.sub(rhs));
    }

    @Override
    public Result<Literal, EvaluationError> mul(Literal rhs) {
        return applyOperation(rhs, (a, b) -> a * b)
                .map(lit -> (Result<Literal, EvaluationError>)new Result.Success<Literal, EvaluationError>(lit))
                .orElse(super.mul(rhs));
    }

    @Override
    public Result<Literal, EvaluationError> compare(Literal rhs, EqualityOperation.EqualityOperationType operation) {
        if (!(rhs instanceof NumericLiteral rhsNumericLit)) {
            return super.compare(rhs, operation);
        }

        int rhsNumeric = rhsNumericLit.getNumericValue();
        int selfNumeric = getNumericValue();
        Literal afterOperation = new BoolLiteral(operation.evaluate(selfNumeric, rhsNumeric));

        return Result.of(afterOperation);
    }

    @Override
    public Result<Literal, EvaluationError> logicalComparison(Literal rhs, LogicalOperation.LogicalOperationType operation) {
        if (!(rhs instanceof NumericLiteral rhsNumericLit)) {
            return super.logicalComparison(rhs, operation);
        }

        int rhsNumeric = rhsNumericLit.getNumericValue();
        int selfNumeric = getNumericValue();
        Literal afterOperation = new BoolLiteral(operation.evaluate(selfNumeric, rhsNumeric));

        return Result.of(afterOperation);
    }

    @Override
    public Result<Literal, EvaluationError> negate() {
        int selfNumeric = getNumericValue();

        return Result.of(fromNumericValue(~selfNumeric));
    }

    @Override
    public String getStringDisplay() {
        return Integer.toString(getNumericValue());
    }

    @Override
    public boolean isTruthy() {
        return getNumericValue() > 0;
    }
}
