package nl.han.ica.icss.ast.function;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylerule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StyleReturnStatement extends ReturnStatement {
    public Stylerule style;

    public StyleReturnStatement(Stylerule style) {
        this.style = style;
    }

    @Override
    public int hashCode() {
        return Objects.hash(style);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StyleReturnStatement that = (StyleReturnStatement) o;
        return Objects.equals(style, that.style);
    }

    @Override
    public List<ASTNode> getChildren() {
        ArrayList<ASTNode> nodes = new ArrayList<>();

        nodes.add(style);

        return nodes;
    }
}
