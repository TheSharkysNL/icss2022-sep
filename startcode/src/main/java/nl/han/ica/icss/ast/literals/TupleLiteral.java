package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.iSwitch.rules.SwitchRule;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TupleLiteral extends Literal {
    public final List<Literal> literals;

    public TupleLiteral(List<Literal> literals) {
        this.literals = literals;
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.tuple(
                literals.stream()
                        .map(Literal::getExpressionType)
                        .toList()
        );
    }

    @Override
    public String getStringDisplay() {
        return "[" + literals.stream().map(Literal::getStringDisplay).collect(Collectors.joining(", ")) + "]";
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

    @Override
    public String getExhaustiveRange(Literal maxLit) {
        if (!(maxLit instanceof TupleLiteral tupleMax) || tupleMax.literals.size() != literals.size()) {
            return super.getExhaustiveRange(maxLit);
        }

        StringBuilder builder = new StringBuilder();

        builder.append("[");

        for (int i = 0; i < literals.size(); i++) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }

            Literal min = literals.get(i);
            Literal max = tupleMax.literals.get(i);

            String display = SwitchRule.getRangeDisplayString(min, max);

            builder.append(display);
        }

        builder.append("]");

        return builder.toString();
    }
}
