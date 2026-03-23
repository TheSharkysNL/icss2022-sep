package nl.han.ica.icss.ast.iSwitch;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;

import java.util.*;

public class SwitchStatement extends ASTNode {
    public final Expression caseExpression;
    public final HashMap<Literal, SwitchCase> cases;

    public SwitchStatement(Expression caseExpression, HashMap<Literal, SwitchCase> cases) {
        this.caseExpression = caseExpression;
        this.cases = cases;
    }

    public Optional<SwitchCase> getCase(Literal value) {
        SwitchCase switchCase = cases.get(value);
        if (switchCase == null) {
            return Optional.empty();
        }
        return Optional.of(cases.get(value));
    }

    @Override
    public String getNodeLabel() {
        return "Switch";
    }

    @Override
    public List<ASTNode> getChildren() {
        List<ASTNode> nodes = new ArrayList<>();

        nodes.add(caseExpression);

        for (Map.Entry<Literal, SwitchCase> entry : cases.entrySet()) {
            nodes.add(entry.getKey());
            nodes.add(entry.getValue());
        }

        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwitchStatement that = (SwitchStatement) o;
        return Objects.equals(caseExpression, that.caseExpression) &&
                Objects.equals(cases, that.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseExpression, cases);
    }
}
