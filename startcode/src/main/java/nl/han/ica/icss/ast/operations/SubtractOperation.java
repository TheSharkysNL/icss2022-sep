package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;

public class SubtractOperation extends Operation {

    public SubtractOperation() {
        super();
    }


    public SubtractOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }
    @Override
    public String getNodeLabel() {
        return "Subtract";
    }
}
