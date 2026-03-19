package nl.han.ica.icss.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class BodyStatement extends ASTNode {
    public List<ASTNode> body = new ArrayList<>();

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(body);
    }
}
