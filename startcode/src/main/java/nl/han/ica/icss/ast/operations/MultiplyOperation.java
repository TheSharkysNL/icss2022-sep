package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;

public class MultiplyOperation extends Operation {

    public MultiplyOperation() {
        super();
    }


    public MultiplyOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }
    @Override
    public String getNodeLabel() {
        return "Multiply";
    }
}
