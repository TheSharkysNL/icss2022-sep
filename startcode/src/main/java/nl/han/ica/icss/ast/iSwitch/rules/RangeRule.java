package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RangeRule extends SwitchRule {
    public final Literal min;
    public final Literal max;
    public final ExpressionType expressionType;

    public RangeRule(@Nullable Literal min, @Nullable Literal max) {
        expressionType = min == null ? (max == null ? ExpressionType.scalar() : max.getExpressionType()) : min.getExpressionType();
        this.min = min == null ? expressionType.min() : min;
        this.max = max == null ? expressionType.max() : max;
    }

    @Override
    public Result<Boolean, EvaluationError> matchesRule(Literal value) {
        if (value.getExpressionType() != expressionType) {
            return Result.of(false);
        }

        Result<Literal, EvaluationError> greaterThanMin = value.compare(min, EqualityOperation.EqualityOperationType.GE);
        if (greaterThanMin.isError()) {
            return Result.err(greaterThanMin.error());
        }

        if (!greaterThanMin.value().isTruthy()) {
            return Result.of(false);
        }

        Result<Literal, EvaluationError> lessThanMax = value.compare(max, EqualityOperation.EqualityOperationType.LT);
        if (lessThanMax.isError()) {
            return Result.err(lessThanMax.error());
        }

        return Result.of(lessThanMax.value().isTruthy());
    }

    @Override
    public Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater) {
        if (highWater.getExpressionType() != expressionType) {
            return Result.of(new Tuple<>(highWater, Optional.empty()));
        }

        Result<Literal, EvaluationError> addOne = highWater.add(new ScalarLiteral(1));
        if (addOne.isError()) {
            return Result.err(new SemanticError("Cannot use type: '" + expressionType + "' in a switch range rule."));
        }

        Result<Literal, EvaluationError> lessThanMin = addOne.value().compare(min, EqualityOperation.EqualityOperationType.LT);
        if (lessThanMin.isError()) {
            return Result.err(new SemanticError("Cannot use type: '" + expressionType + "' in a switch range rule."));
        }

        if (lessThanMin.value().isTruthy()) {
            String message = highWater.getExhaustiveRange(min);
            return Result.of(new Tuple<>(max, Optional.of(message)));
        }

        return Result.of(new Tuple<>(max, Optional.empty()));
    }

    @Override
    public Optional<SemanticError> validateRule(Checker checker) {
        if (min.getExpressionType() != max.getExpressionType()) {
            return Optional.of(new SemanticError("The switch range rule must have the same min and max expression type. Got: min: '" + min.getExpressionType() + "' and max: '" + max.getExpressionType() + "'."));
        }

        Result<Literal, EvaluationError> result = min.compare(max, EqualityOperation.EqualityOperationType.LT);
        if (!result.isError() && result.value().isTruthy()) {
            return Optional.of(new SemanticError("The maximum value of the range: '" + max.getStringDisplay() + "' is less than the minimum value: '" + min.getStringDisplay() + "'."));
        }

        return switch (expressionType) {
            case ExpressionType.Undefined ignored -> Optional.of(new SemanticError("Cannot create a switch range rule for '" + expressionType + "'."));
            case ExpressionType.Bool ignored -> Optional.of(new SemanticError("Cannot create a switch range rule for '" + expressionType + "'."));
            case ExpressionType.Tuple ignored -> Optional.of(new SemanticError("Cannot create a switch range rule for '" + expressionType + "'."));
            case ExpressionType.Color ignored -> Optional.of(new SemanticError("Cannot create a switch range rule for '" + expressionType + "'."));
            default -> Optional.empty();
        };
    }

    @Override
    protected Literal compareLiteral() {
        return min;
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>(2);

        nodes.add(min);
        nodes.add(max);

        return nodes;
    }

    @Override
    public String getNodeLabel() {
        return "RangeRule";
    }
}
