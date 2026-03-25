package nl.han.ica.icss.ast.iSwitch;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.iSwitch.rules.DefaultRule;
import nl.han.ica.icss.ast.iSwitch.rules.SwitchRule;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.*;
import java.util.stream.Collectors;

/*
 * TODO: add rules like:
 *
 * 1 -> literals already working for switch case but make it work within this rule list
 * 1..8 -> matches number 1, 2, 3, 4, 5, 6, 7
 * ..8 -> matches any number less than 8
 * 8.. -> matches any number greater than or equal to 8
 * 8px.. -> same as above but then for pixels this can be done for any of these number types
 * Bool b -> matches boolean this can also be done for other types like: Integer, etc
 * [Variable, Variable2] -> unwrap tuple type
 * [1..8, TRUE] -> matches a tuple where the first value matches  1, 2, 3, 4, 5, 6, 7 and the second TRUE. have match rule within tuple
 * default -> default case
 * Variable -> matching to a variable
 *
 * For the checker we can make sure that every value is matched for the ExpressionType of the condition
 * If not show an error to the user that a specific value is not matched.
 */
public class SwitchRuleList extends ASTNode implements Iterable<Tuple<SwitchRule, SwitchCase>> {
    public final List<Tuple<SwitchRule, SwitchCase>> cases;
    public final List<SwitchRule> sortedCases;

    public SwitchRuleList(List<Tuple<SwitchRule, SwitchCase>> cases) {
        this.cases = cases;
        sortedCases = cases
                .stream()
                .map(Tuple::first)
                .sorted(SwitchRule::compare)
                .toList();
    }

    public Optional<Tuple<SwitchRule, SwitchCase>> getCase(Literal value) {
        for (Tuple<SwitchRule, SwitchCase> switchCase : cases) {
            Result<Boolean, EvaluationError> matches = switchCase.first().matchesRule(value);
            if (!matches.isError() && matches.value()) {
                return Optional.of(switchCase);
            }
        }

        return Optional.empty();
    }

    public Optional<SemanticError> checkRuleExhaustiveness(ExpressionType expressionType) {
        if (expressionType.mustHaveDefaultCase()) {
            boolean hasDefault = cases
                    .stream()
                    .anyMatch(x -> x.first() instanceof DefaultRule);
            if (!hasDefault) {
                return Optional.of(new SemanticError("A switch statement when trying to match a " + expressionType + " expression must have a default case."));
            }
            return Optional.empty();
        }

        Literal currentMinValue = expressionType.min();
        Literal maxValue = expressionType.max();

        StringBuilder builder = new StringBuilder();

        for (SwitchRule rule : sortedCases) {
            Result<Tuple<Literal, Optional<String>>, SemanticError> result = rule.getExhaustiveness(currentMinValue);
            if (result.isError()) {
                return Optional.of(result.error());
            }

            currentMinValue = result.value().first();
            if (Objects.equals(currentMinValue, maxValue)) {
                break;
            }

            addToBuilder(result.value().second().orElse(null), builder);
        }

        if (!Objects.equals(currentMinValue, maxValue)) {
            addToBuilder(currentMinValue.getExhaustiveRange(maxValue), builder);
        }

        if (!builder.isEmpty()) {
            return Optional.of(new SemanticError("The switch cases aren't exhaustive enough and don't cover the following patterns: " + builder));
        }

        return Optional.empty();
    }

    private void addToBuilder(String value, StringBuilder builder) {
        if (value != null) {
            if (!builder.isEmpty()) {
                builder.append(" and ");
            }
            builder.append(value);
        }
    }

    @Override
    public String getNodeLabel() {
        return "SwitchRuleList";
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>(cases.size() * 2);

        for (Tuple<SwitchRule, SwitchCase> switchCase : cases) {
            nodes.add(switchCase.first());
            nodes.add(switchCase.second());
        }

        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwitchRuleList that = (SwitchRuleList) o;
        return Objects.equals(cases, that.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cases);
    }

    @Override
    public Iterator<Tuple<SwitchRule, SwitchCase>> iterator() {
        return cases.iterator();
    }
}
