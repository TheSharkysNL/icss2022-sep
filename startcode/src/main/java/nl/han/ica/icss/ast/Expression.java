package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.HashMap;
import java.util.Optional;

public abstract class Expression extends ASTNode {
    @Override
    public boolean hasError() {
        if (this.getChildren()
                .stream()
                .anyMatch(node -> !(node instanceof Expression))) { // children must all be of an expression type

            setError("Invalid expression.");
            return true;
        }

        return false;
    }

    public abstract Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables);
}
