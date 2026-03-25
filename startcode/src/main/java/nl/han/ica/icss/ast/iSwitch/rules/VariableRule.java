package nl.han.ica.icss.ast.iSwitch.rules;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.Tuple;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.List;

public class VariableRule extends DefaultRule {
    public final VariableReference variableName;

    public VariableRule(VariableReference variableName) {
        this.variableName = variableName;
    }

    @Override
    public List<VariableAssignment> getVariableAssignments(Literal expression) {
        return List.of(new VariableAssignment(variableName, expression));
    }

    @Override
    public List<Tuple<VariableReference, ExpressionType>> getVariableReferences(ExpressionType expressionType) {
        return List.of(new Tuple<>(variableName, expressionType));
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>(1);

        nodes.add(variableName);

        return nodes;
    }

    @Override
    public String getNodeLabel() {
        return "VariableRule";
    }
}
