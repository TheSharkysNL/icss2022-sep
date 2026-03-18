package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import org.checkerframework.checker.nullness.Opt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Operation extends Expression {

    public Expression lhs;
    public Expression rhs;

    public Operation() {
        lhs = null;
        rhs = null;
    }

    public Operation(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if(lhs != null)
            children.add(lhs);
        if(rhs != null)
            children.add(rhs);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if(lhs == null) {
            lhs = (Expression) child;
        } else if(rhs == null) {
            rhs = (Expression) child;
        }
        return this;
    }

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables) {
        Result<Literal, EvaluationError> lhsLiteral = lhs.tryEvaluate(variables);
        Result<Literal, EvaluationError> rhsLiteral = rhs.tryEvaluate(variables);

        return switch (new Tuple<>(lhsLiteral, rhsLiteral)) {
            case Tuple(Result.Success(Literal lhsValue), Result.Success(Literal rhsValue)) ->
                    applyOperation(lhsValue, rhsValue);
            case Tuple(Result.Error(EvaluationError error), Result.Success(Literal ignored)) ->
                    new Result.Error<>(error);
            case Tuple(Result.Success(Literal ignored), Result.Error(EvaluationError error)) ->
                    new Result.Error<>(error);
            case Tuple(Result.Error(EvaluationError error), Result.Error(EvaluationError ignored)) ->
                    new Result.Error<>(error); // currently always returning left hand side error
        };
    }

    protected abstract Result<Literal, EvaluationError> applyOperation(Literal a, Literal b);

    private Result<ExpressionType, SemanticError> withExpressionType(IHANLinkedList<HashMap<String, ExpressionType>> variables, BiFunction<ExpressionType, ExpressionType, Result<ExpressionType, SemanticError>> function) {
        return withExpressionType(variables, function, result -> result);
    }

    private <T> T withExpressionType(IHANLinkedList<HashMap<String, ExpressionType>> variables, BiFunction<ExpressionType, ExpressionType, T> function, Function<Result<ExpressionType, SemanticError>, T> mapper) {
        Result<ExpressionType, SemanticError> lhsResult = lhs.getExpressionType(variables);
        if (lhsResult.isError()) {
            return mapper.apply(lhsResult);
        }

        Result<ExpressionType, SemanticError> rhsResult = rhs.getExpressionType(variables);
        if (rhsResult.isError()) {
            return mapper.apply(lhsResult);
        }

        return function.apply(lhsResult.value(), rhsResult.value());
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(IHANLinkedList<HashMap<String, ExpressionType>> variables) {
        return withExpressionType(variables, (lhs, rhs) -> new Result.Success<>(switch (new Tuple<>(lhs, rhs)) {
            case Tuple(ExpressionType first, ExpressionType second) when first == ExpressionType.SCALAR -> second;
            case Tuple(ExpressionType first, ExpressionType second) when second == ExpressionType.SCALAR -> first;
            case Tuple(ExpressionType first, ExpressionType ignored) -> first;
        }));
    }

    protected abstract Optional<SemanticError> validateExpressionInternal(ExpressionType lhs, ExpressionType rhs);

    @Override
    public Optional<SemanticError> validateExpression(IHANLinkedList<HashMap<String, ExpressionType>> variables) {
        return withExpressionType(variables, (lhs, rhs) -> {
            if (lhs == ExpressionType.COLOR || rhs == ExpressionType.COLOR) { // can never add colors together
                return Optional.of(new SemanticError("Cannot apply a '" + getNodeLabel() + "' operation with a color."));
            }

            return validateExpressionInternal(lhs, rhs);
        }, Result::ok);
    }
}
