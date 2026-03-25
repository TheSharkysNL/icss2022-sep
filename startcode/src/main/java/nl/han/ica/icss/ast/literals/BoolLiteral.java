package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.NumericLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.Objects;

public class BoolLiteral extends NumericLiteral {
    public boolean value;
    // hacky way to compare booleans used for switch case rules
    private final int underlyingInteger;
    private boolean compareUnderlyingInteger;

    public BoolLiteral(boolean value) {
        this(value ? 1 : 0);
        compareUnderlyingInteger = false;
    }
    public BoolLiteral(String text) {
        this(text.equals("TRUE"));
        compareUnderlyingInteger = false;
    }
    private BoolLiteral(int num) {
        this.value = num > 0;
        this.underlyingInteger = num;
        compareUnderlyingInteger = true; // only compare when it is set by this constructor. Used in the getExhaustiveRange method more explanation there
    }

    public static BoolLiteral getMax() {
        return new BoolLiteral(2);
    }

    @Override
    public String getExhaustiveRange(Literal max) {
        if (!(max instanceof BoolLiteral boolMax)) {
            return super.getExhaustiveRange(max);
        }

        StringBuilder str = new StringBuilder();

        // uses the underlying integer to check whether FALSE and TRUE are matched by the switch case
        // without this integer there would be no way to check if TRUE was found within the switch
        // Because without the integer the max would be TRUE and the min would be FALSE which means it'll only
        // check for FALSE using the interval partition covering algorithm

        for (int i = underlyingInteger; i < Math.min(boolMax.underlyingInteger, 2); i++) {
            if (!str.isEmpty()) {
                str.append(" and ");
            }
            str.append(i > 0 ? "TRUE" : "FALSE");
        }

        return str.toString();
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
        if (compareUnderlyingInteger) {
            return value == that.value && underlyingInteger == that.underlyingInteger;
        }
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
        return new BoolLiteral(number);
    }

    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.bool();
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @Override
    public String getStringDisplay() {
        return value ? "TRUE" : "FALSE";
    }
}
