package nl.han.ica.icss.ast.literals;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.NumericLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.InvalidOperation;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class ColorLiteral extends NumericLiteral {
    public String value;

    public ColorLiteral(String value) {
        this.value = value;
    }
    @Override
    public String getNodeLabel() {
        return "Color literal (" + value + ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorLiteral that = (ColorLiteral) o;
        return Objects.equals(value, that.value);
    }
    @Override
    public int hashCode() {

        return Objects.hash(value);
    }

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables) {
        return new Result.Success<>(this);
    }

    @Override
    public int getNumericValue() {
        return Integer.parseInt(value.substring(1), 16);
    }

    @Override
    public NumericLiteral fromNumericValue(int number) {
        // string format used to pad the string to 6 characters
        String paddedHexNumber = String.format("%1$" + 6 + "s",  Integer.toString(number, 16))
                .replace(' ', '0');

        return new ColorLiteral("#" + paddedHexNumber);
    }

    @Override
    public void addGeneratedCss(StringBuilder builder) {
        builder.append(value);
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.COLOR;
    }
}
