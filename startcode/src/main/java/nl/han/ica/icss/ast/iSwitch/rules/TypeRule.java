package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

public class TypeRule extends VariableRule {
    public final ExpressionType type;

    public TypeRule(@Nullable VariableReference variableName, ExpressionType type) {
        super(variableName);
        this.type = type;
    }

    @Override
    public Result<Boolean, EvaluationError> matchesRule(Literal value) {
        return Result.of(value.getExpressionType() == type);
    }

    @Override
    public Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater) {
        if (highWater.getExpressionType() != type) {
            return Result.of(new Tuple<>(highWater, Optional.empty()));
        }
        return super.getExhaustiveness(highWater);
    }

    @Override
    public List<Tuple<VariableReference, ExpressionType>> getVariableReferences(ExpressionType expressionType) {
        return super.getVariableReferences(expressionType)
                .stream()
                .map(x -> new Tuple<>(x.first(), type))
                .toList();
    }
}
