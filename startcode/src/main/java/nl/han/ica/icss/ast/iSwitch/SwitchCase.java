package nl.han.ica.icss.ast.iSwitch;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.BodyStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SwitchCase extends BodyStatement {
    public SwitchCase(List<ASTNode> body) {
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "SwitchCase";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwitchCase that = (SwitchCase) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
