package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;
import nl.han.ica.icss.transforms.InvalidOperation;

import java.util.HashMap;
import java.util.Optional;

public abstract class Literal extends Expression {
    public Result<Literal, EvaluationError> add(Literal rhs) {
        return Result.err(new InvalidOperation(this, rhs, "add"));
    }
    public Result<Literal, EvaluationError> sub(Literal rhs){
        return Result.err(new InvalidOperation(this, rhs, "sub"));
    }
    public Result<Literal, EvaluationError> mul(Literal rhs) {
        return Result.err(new InvalidOperation(this, rhs, "mul"));
    }
    public Result<Literal, EvaluationError> compare(Literal rhs, EqualityOperation.EqualityOperationType operation) {
        return Result.err(new InvalidOperation(this, rhs, operation.name()));
    }

    public Result<Literal, EvaluationError> negate() {
        return Result.err(new InvalidOperation(this, null, "negate"));
    }

    protected abstract ExpressionType getExpressionType();

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator) {
        return Result.of(this);
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        return Result.of(getExpressionType());
    }

    @Override
    public Optional<SemanticError> validateExpression(Checker checker) {
        return Optional.empty();
    }
}
