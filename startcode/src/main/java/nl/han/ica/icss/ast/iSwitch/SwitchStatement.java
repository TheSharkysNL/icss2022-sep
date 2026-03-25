package nl.han.ica.icss.ast.iSwitch;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.iSwitch.rules.SwitchRule;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;

import java.util.*;

public class SwitchStatement extends ASTNode implements IMultipleBodyStatements {
    public final Expression caseExpression;
    public final SwitchRuleList ruleList;

    public SwitchStatement(Expression caseExpression, SwitchRuleList ruleList) {
        this.caseExpression = caseExpression;
        this.ruleList = ruleList;
    }

    public Optional<Tuple<SwitchRule, SwitchCase>> getCase(Literal value) {
        return ruleList.getCase(value);
    }

    public Optional<SemanticError> validateSwitchRules(Checker checker) {
        Optional<SemanticError> error = caseExpression.validateExpression(checker);
        if (error.isPresent()) {
            return error;
        }

        return checkSwitchExhaustiveness(checker);
    }

    private Optional<SemanticError> checkSwitchExhaustiveness(Checker checker) {
        Result<ExpressionType, SemanticError> type = caseExpression.getExpressionType(checker);
        if (type.isError()) {
            return Optional.of(type.error());
        }

        return ruleList.checkRuleExhaustiveness(type.value());
    }

    @Override
    public String getNodeLabel() {
        return "Switch";
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> nodes = new ArrayList<>();

        nodes.add(caseExpression);
        nodes.add(ruleList);

        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwitchStatement that = (SwitchStatement) o;
        return Objects.equals(caseExpression, that.caseExpression) &&
                Objects.equals(ruleList, that.ruleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseExpression, ruleList);
    }

    @Override
    public List<BodyStatement> getBodyStatements() {
        return ruleList.cases
                .stream()
                .map(c -> (BodyStatement)c.second())
                .toList();
    }
}
