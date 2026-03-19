package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.function.FunctionDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForStatement extends BodyStatement {
    public final List<VariableAssignment> initialAssignments;
    public final Expression loopExpression;
    public final List<VariableAssignment> loopAssignments;

    public ForStatement(List<VariableAssignment> initialAssignments, Expression loopExpression, List<VariableAssignment> loopAssignments, List<ASTNode> body) {
        this.initialAssignments = initialAssignments;
        this.loopExpression = loopExpression;
        this.loopAssignments = loopAssignments;
        this.body = body;
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>(initialAssignments);
        nodes.add(loopExpression);
        nodes.addAll(loopAssignments);
        nodes.addAll(body);

        return nodes;
    }

    @Override
    public String getNodeLabel() {
        return "For";
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialAssignments, loopAssignments, loopExpression, body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ForStatement that = (ForStatement) o;
        return Objects.equals(initialAssignments, that.initialAssignments) &&
                Objects.equals(loopExpression, that.loopExpression) &&
                Objects.equals(loopAssignments, that.loopAssignments) &&
                Objects.equals(body, that.body);
    }
}
