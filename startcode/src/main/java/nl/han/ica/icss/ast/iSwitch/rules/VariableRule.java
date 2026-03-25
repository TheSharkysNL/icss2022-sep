package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.types.ExpressionType;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VariableRule extends DefaultRule {
    @Nullable
    public final VariableReference variableName;

    public VariableRule(@Nullable VariableReference variableName) {
        this.variableName = variableName;
    }

    @Override
    public List<VariableAssignment> getVariableAssignments(Literal expression) {
        if (variableName == null) {
            return List.of();
        }
        return List.of(new VariableAssignment(variableName, expression));
    }

    @Override
    public List<Tuple<VariableReference, ExpressionType>> getVariableReferences(ExpressionType expressionType) {
        if (variableName == null) {
            return List.of();
        }
        return List.of(new Tuple<>(variableName, expressionType));
    }

    @Override
    public List<ASTNode> getChildren() {
        if (variableName == null) {
            return List.of();
        }
        return List.of(variableName);
    }

    @Override
    public String getNodeLabel() {
        return "VariableRule";
    }
}
