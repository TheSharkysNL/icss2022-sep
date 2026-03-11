package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.transforms.EvaluationError;

public class SubtractOperation extends Operation {

    public SubtractOperation() {
        super();
    }


    public SubtractOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.sub(b);
    }

    @Override
    public String getNodeLabel() {
        return "Subtract";
    }
}
