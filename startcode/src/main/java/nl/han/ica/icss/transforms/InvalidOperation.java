package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.ASTNode;

public class InvalidOperation extends EvaluationError {
    private final ASTNode lhs;
    private final ASTNode rhs;
    private final String operation;

    public InvalidOperation(ASTNode lhs, ASTNode rhs, String operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Invalid operation. Cannot perform a " + operation + " between the types: '" + lhs.getNodeLabel() + "' and '" + rhs.getNodeLabel() + "'.";
    }
}
