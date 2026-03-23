package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TupleLiteral extends Literal {
    public final List<Literal> literals;

    public TupleLiteral(List<Literal> literals) {
        this.literals = literals;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.TUPLE;
    }

    @Override
    public String getNodeLabel() {
        return "Tuple";
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(literals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TupleLiteral that = (TupleLiteral) o;
        return Objects.equals(literals, that.literals);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(literals);
    }
}
