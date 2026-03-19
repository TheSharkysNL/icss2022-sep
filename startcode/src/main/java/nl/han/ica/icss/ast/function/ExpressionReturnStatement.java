package nl.han.ica.icss.ast.function;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpressionReturnStatement extends ReturnStatement {
    public Expression expression;

    public ExpressionReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExpressionReturnStatement that = (ExpressionReturnStatement) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>();

        nodes.add(expression);

        return nodes;
    }
}
