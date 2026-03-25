package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.TupleLiteral;
import nl.han.ica.icss.ast.literals.UndefinedLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TupleRule extends SwitchRule {
    public final List<SwitchRule> tupleRules;

    public TupleRule(List<SwitchRule> tupleRules) {
        this.tupleRules = tupleRules;
    }

    @Override
    public Result<Boolean, EvaluationError> matchesRule(Literal value) {
        if (!(value instanceof TupleLiteral tupleLiteral)) {
            return Result.of(false);
        }

        if (tupleLiteral.literals.size() != tupleRules.size()) {
            return Result.of(false);
        }

        return checkLiteralsMatch(tupleLiteral);
    }

    private Result<Boolean, EvaluationError> checkLiteralsMatch(TupleLiteral tupleLiteral) {
        for (int i = 0; i < tupleRules.size(); i++) {
            SwitchRule rule = tupleRules.get(i);
            Literal literal = tupleLiteral.literals.get(i);

            Result<Boolean, EvaluationError> result = rule.matchesRule(literal);
            if (result.isError()) {
                return result;
            }

            if (!result.value()) {
                return Result.of(false);
            }
        }

        return Result.of(true);
    }

    @Override
    public Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater) {
        if (!(highWater instanceof TupleLiteral tuple) || tuple.literals.size() != tupleRules.size()) {
            return Result.of(new Tuple<>(highWater, Optional.empty()));
        }

        StringBuilder builder = new StringBuilder();
        ArrayList<Literal> newTupleLiterals = new ArrayList<>(tupleRules.size());

        for (int i = 0; i < tupleRules.size(); i++) {
            SwitchRule rule = tupleRules.get(i);
            Literal literal = tuple.literals.get(i);

            Result<Tuple<Literal, Optional<String>>, SemanticError> result = rule.getExhaustiveness(literal);
            if (result.isError()) {
                return result;
            }

            if (result.value().second().isPresent()) {
                if (!builder.isEmpty()) {
                    builder.append(" and ");
                }

                builder.append("[");

                for (int j = 0; j < tupleRules.size(); j++) {
                    if (j > 0) {
                        builder.append(", ");
                    }

                    if (j == i) {
                        builder.append(result.value().second().get());
                    } else {
                        builder.append(tuple.literals.get(j).getStringDisplay());
                    }
                }

                builder.append("]");
            }
            newTupleLiterals.add(result.value().first());
        }

        return Result.of(new Tuple<>(new TupleLiteral(newTupleLiterals), Optional.ofNullable(builder.isEmpty() ? null : builder.toString())));
    }

    @Override
    public Optional<SemanticError> validateRule(Checker checker) {
        for (SwitchRule rule : tupleRules) {
            Optional<SemanticError> error = rule.validateRule(checker);
            if (error.isPresent()) {
                return error;
            }
        }

        return Optional.empty();
    }

    @Override
    public List<VariableAssignment> getVariableAssignments(Literal expression) {
        List<Literal> literals = List.of();
        if (expression instanceof TupleLiteral tupleLiteral) {
            literals = tupleLiteral.literals;
        }

        ArrayList<VariableAssignment> variables = new ArrayList<>();
        for (int i = 0; i < tupleRules.size(); i++) {
            SwitchRule rule = tupleRules.get(i);
            Literal literal = i >= literals.size() ? UndefinedLiteral.UNDEFINED : literals.get(i);

            variables.addAll(rule.getVariableAssignments(literal));
        }
        return variables;
    }

    @Override
    public List<Tuple<VariableReference, ExpressionType>> getVariableReferences(ExpressionType expressionType) {
        List<ExpressionType> types = List.of();
        if (expressionType instanceof ExpressionType.Tuple(List<ExpressionType> expressionTypes)) {
            types = expressionTypes;

        }

        ArrayList<Tuple<VariableReference, ExpressionType>> variables = new ArrayList<>();
        for (int i = 0; i < tupleRules.size(); i++) {
            SwitchRule rule = tupleRules.get(i);
            ExpressionType type = i >= types.size() ? ExpressionType.undefined() : types.get(i);

            variables.addAll(rule.getVariableReferences(type));
        }
        return variables;
    }

    @Override
    public int compare(SwitchRule b) {
        if (!(b instanceof TupleRule tupleRule)) {
            return 1; // always place tuple rules last
        }

        if (tupleRules.size() != tupleRule.tupleRules.size()) {
            return 0; // doesn't matter because they won't overlap with exhaustiveness
        }

        int comparison = 0;
        for (int i = 0; i < tupleRules.size(); i++) {
            SwitchRule ownRule = tupleRules.get(i);
            SwitchRule otherRule = tupleRule.tupleRules.get(i);

            comparison += ownRule.compare(otherRule);
        }

        return comparison;
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(tupleRules);
    }

    @Override
    public String getNodeLabel() {
        return "TupleRule";
    }
}
