package nl.han.ica.icss.ast.literals;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.NumericLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class BoolLiteral extends NumericLiteral {
    public boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }
    public BoolLiteral(String text) {
        this.value = text.equals("TRUE");
    }

    @Override
    public String getNodeLabel() {
        String textValue = value ? "TRUE" : "FALSE";
        return "Bool Literal (" + textValue + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoolLiteral that = (BoolLiteral) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int getNumericValue() {
        return value ? 1 : 0;
    }

    @Override
    public NumericLiteral fromNumericValue(int number) {
        return new BoolLiteral(number >= 1);
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
}
