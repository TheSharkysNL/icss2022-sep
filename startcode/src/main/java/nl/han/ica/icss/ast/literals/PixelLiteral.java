package nl.han.ica.icss.ast.literals;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.NumericLiteral;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class PixelLiteral extends NumericLiteral {
    public int value;

    public PixelLiteral(int value) {
        this.value = value;
    }
    public PixelLiteral(String text) {
        this.value = Integer.parseInt(text.substring(0, text.length() - 2));
    }
    @Override
    public String getNodeLabel() {
        return "Pixel literal (" + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PixelLiteral that = (PixelLiteral) o;
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
        return new PixelLiteral(number);
    }

    @Override
    public void addGeneratedCss(StringBuilder builder) {
        builder.append(value);
        builder.append("px");
    }
}
