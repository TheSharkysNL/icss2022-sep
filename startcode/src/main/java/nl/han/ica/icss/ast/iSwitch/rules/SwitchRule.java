package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.literals.TupleLiteral;
import nl.han.ica.icss.ast.operations.EqualityOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;

import java.util.List;
import java.util.Optional;

public abstract class SwitchRule extends ASTNode {

    public abstract Result<Boolean, EvaluationError> matchesRule(Literal value);

    /**
     * Checks for exhaustiveness. Based on high water algorithm
     * @param highWater The current value based on tbe previous one given
     * @return Either a success result containing the next literal and a error if applicable or a error result containing a semantic error
     */
    public abstract Result<Tuple<Literal, Optional<String>>, SemanticError> getExhaustiveness(Literal highWater);

    public abstract Optional<SemanticError> validateRule(Checker checker);

    public int compare(SwitchRule b) {
        Literal aLiteral = compareLiteral();
        Literal bLiteral = b.compareLiteral();

        Result<Literal, EvaluationError> eq = aLiteral.compare(bLiteral, EqualityOperation.EqualityOperationType.EQ);
        if (eq.isError()) {
            return 0;
        }

        if (eq.value().isTruthy()) {
            return 0;
        }

        Result<Literal, EvaluationError> lt = aLiteral.compare(bLiteral, EqualityOperation.EqualityOperationType.LT);
        if (lt.isError()) {
            return 0;
        }

        if (lt.value().isTruthy()) {
            return -1;
        }

        return 1;
    }

    public static String getRangeDisplayString(Literal min, Literal max) {
        return min.getExhaustiveRange(max);
    }

    protected Literal compareLiteral() {
        return new ScalarLiteral(0);
    }

    public List<VariableAssignment> getVariableAssignments(Literal expression) {
        return List.of();
    }

    public List<Tuple<VariableReference, ExpressionType>> getVariableReferences(ExpressionType expressionType) {
        return List.of();
    }

    @Override
    public String getNodeLabel() {
        return "SwitchRule";
    }
}
