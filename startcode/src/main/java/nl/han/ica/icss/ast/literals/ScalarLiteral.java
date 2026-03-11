package nl.han.ica.icss.ast.literals;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.NumericLiteral;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class ScalarLiteral extends NumericLiteral {
    public int value;

    public ScalarLiteral(int value) {
        this.value = value;
    }
    public ScalarLiteral(String text) {
        this.value = Integer.parseInt(text);
    }
    @Override
    public String getNodeLabel() {
        return "Scalar literal (" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScalarLiteral that = (ScalarLiteral) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int getNumericValue() {
        return value;
    }

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables) {
        return new Result.Success<>(this);
    }

    @Override
    public NumericLiteral fromNumericValue(int number) {
        return new ScalarLiteral(number);
    }
}
