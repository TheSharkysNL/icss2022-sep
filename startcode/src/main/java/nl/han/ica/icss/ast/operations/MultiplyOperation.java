package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.transforms.EvaluationError;

public class MultiplyOperation extends Operation {

    public MultiplyOperation() {
        super();
    }


    public MultiplyOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.mul(b);
    }

    @Override
    public String getNodeLabel() {
        return "Multiply";
    }
}
