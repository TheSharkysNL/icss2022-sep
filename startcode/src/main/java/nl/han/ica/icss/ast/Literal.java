package nl.han.ica.icss.ast;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.InvalidOperation;

import java.util.Optional;

public abstract class Literal extends Expression {
    public Result<Literal, EvaluationError> add(Literal rhs) {
        return new Result.Error<>(new InvalidOperation(this, rhs, "add"));
    }
    public Result<Literal, EvaluationError> sub(Literal rhs){
        return new Result.Error<>(new InvalidOperation(this, rhs, "sub"));
    }
    public Result<Literal, EvaluationError> mul(Literal rhs) {
        return new Result.Error<>(new InvalidOperation(this, rhs, "add"));
    }
}
