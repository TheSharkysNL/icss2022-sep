package nl.han.ica.icss.ast.function;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;

public class ReturnStatement extends ASTNode {
    @Override
    public String getNodeLabel() {
        return "ReturnStatement";
    }
}
