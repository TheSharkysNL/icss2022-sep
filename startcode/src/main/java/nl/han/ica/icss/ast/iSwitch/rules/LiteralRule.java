package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LiteralRule extends SwitchRule {
    public final Literal value;

    public LiteralRule(Literal value) {
        this.value = value;
    }

    @Override
    public Result<Boolean, EvaluationError> matchesRule(Literal value) {
        return Result.of(Objects.equals(this.value, value));
    }

    @Override
    public Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater) {
        if (highWater.getExpressionType() != value.getExpressionType()) {
            return Result.of(new Tuple<>(highWater, Optional.empty()));
        }

        Result<Literal, EvaluationError> greaterOrEqual = highWater.compare(value, EqualityOperation.EqualityOperationType.GE);
        if (greaterOrEqual.isError()) {
            return Result.err(new SemanticError("Cannot use type: '" + highWater.getExpressionType() + "' in a switch literal rule."));
        }

        if (greaterOrEqual.value().isTruthy()) {
            return Result.of(new Tuple<>(highWater.add(new ScalarLiteral(1)).value(), Optional.empty()));
        }

        String message = highWater.getExhaustiveRange(value);
        return Result.of(new Tuple<>(highWater, Optional.of(message)));
    }

    @Override
    public Optional<SemanticError> validateRule(Checker checker) {
        return Optional.empty();
    }

    @Override
    protected Literal compareLiteral() {
        return value;
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>(1);

        nodes.add(value);

        return nodes;
    }

    @Override
    public String getNodeLabel() {
        return "LiteralRule";
    }
}
