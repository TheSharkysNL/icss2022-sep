package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.Optional;

public class DefaultRule extends SwitchRule {
    private static final Result<Boolean, EvaluationError> TRUE_RESULT = Result.of(true);

    @Override
    public Result<Boolean, EvaluationError> matchesRule(Literal value) {
        return TRUE_RESULT;
    }

    @Override
    public Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater) {
        return Result.of(new Tuple<>(highWater.getExpressionType().max(), Optional.empty()));
    }

    @Override
    public Optional<SemanticError> validateRule(Checker checker) {
        return Optional.empty();
    }

    @Override
    protected Literal compareLiteral() {
        return ExpressionType.scalar().min();
    }

    @Override
    public String getNodeLabel() {
        return "DefaultRule";
    }
}
