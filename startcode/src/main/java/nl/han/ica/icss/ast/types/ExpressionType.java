package nl.han.ica.icss.ast.types;

import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.literals.*;

import java.util.List;


public sealed interface ExpressionType {

    record Pixel()      implements ExpressionType {
        @Override
        public Literal max() {
            return new PixelLiteral(Integer.MAX_VALUE);
        }

        @Override
        public Literal min() {
            return new PixelLiteral(Integer.MIN_VALUE);
        }

        @Override
        public String toString() {
            return "pixel";
        }
    }
    record Percentage() implements ExpressionType {
        @Override
        public Literal max() {
            return new PercentageLiteral(Integer.MAX_VALUE);
        }

        @Override
        public Literal min() {
            return new PercentageLiteral(Integer.MIN_VALUE);
        }

        @Override
        public String toString() {
            return "percentage";
        }
    }
    record Color()      implements ExpressionType {
        @Override
        public Literal max() {
            return new ColorLiteral("#" + Integer.toString(Integer.MAX_VALUE, 16));
        }

        @Override
        public Literal min() {
            return new ColorLiteral("#" + Integer.toString(Integer.MIN_VALUE, 16));
        }

        @Override
        public String toString() {
            return "color";
        }
    }
    record Scalar()     implements ExpressionType {
        @Override
        public Literal max() {
            return new ScalarLiteral(Integer.MAX_VALUE);
        }

        @Override
        public Literal min() {
            return new ScalarLiteral(Integer.MIN_VALUE);
        }

        @Override
        public String toString() {
            return "scalar";
        }
    }
    record Undefined()  implements ExpressionType {
        @Override
        public String toString() {
            return "undefined";
        }
    }
    record Bool()       implements ExpressionType {
        @Override
        public Literal max() {
            return BoolLiteral.getMax();
        }

        @Override
        public Literal min() {
            return new BoolLiteral(false);
        }

        @Override
        public String toString() {
            return "bool";
        }
    }

    record Tuple(List<ExpressionType> types) implements ExpressionType {
        public Tuple(List<ExpressionType> types) {
            this.types = List.copyOf(types); // immutable
        }

        @Override
        public Literal max() {
            return new TupleLiteral(
                    types.stream()
                    .map(ExpressionType::max)
                    .toList()
            );
        }

        @Override
        public Literal min() {
            return new TupleLiteral(
                    types.stream()
                    .map(ExpressionType::min)
                    .toList()
            );
        }

        @Override
        public String toString() {
            return "tuple";
        }
    }

    default Literal max() {
        return null;
    }
    default Literal min() {
        return null;
    }

    Pixel PIXEL_CONST = new Pixel();
    Percentage PERCENTAGE_CONST = new Percentage();
    Color COLOR_CONST = new Color();
    Scalar SCALAR_CONST = new Scalar();
    Undefined UNDEFINED_CONST = new Undefined();
    Bool BOOL_CONST = new Bool();

    static Pixel pixel() {
        return PIXEL_CONST;
    }

    static Percentage percentage() {
        return PERCENTAGE_CONST;
    }

    static Color color() {
        return COLOR_CONST;
    }

    static Scalar scalar() {
        return SCALAR_CONST;
    }

    static Undefined undefined() {
        return UNDEFINED_CONST;
    }

    static Bool bool() {
        return BOOL_CONST;
    }

    static Tuple tuple(List<ExpressionType> expressionTypes) {
        return new Tuple(expressionTypes);
    }
}

