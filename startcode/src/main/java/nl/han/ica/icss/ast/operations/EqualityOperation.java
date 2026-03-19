package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.parser.ICSSParser;
import nl.han.ica.icss.transforms.EvaluationError;
import org.antlr.v4.runtime.Token;

import java.util.Optional;

public class EqualityOperation extends Operation {
    private final EqualityOperationType type;
    private final Token operatorToken;

    public EqualityOperation(Expression lhs, Expression rhs, Token token) {
        super(lhs, rhs);

        this.type = switch (token.getType()) {
            case ICSSParser.EQ -> EqualityOperationType.EQ;
            case ICSSParser.NE -> EqualityOperationType.NE;
            case ICSSParser.GE -> EqualityOperationType.GE;
            case ICSSParser.GT -> EqualityOperationType.GT;
            case ICSSParser.LT -> EqualityOperationType.LT;
            case ICSSParser.LE -> EqualityOperationType.LE;
            default -> throw new IllegalArgumentException("Invalid token type as equality operation: '" + token.getText() + "'.");
        };

        operatorToken = token;
    }

    public enum EqualityOperationType {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE;

        public <T extends Comparable<T>> boolean evaluate(T lhs, T rhs) {
            int result = lhs.compareTo(rhs);
            return switch (this) {
                case EQ -> result == 0;
                case NE -> result != 0;
                case GT -> result > 0;
                case GE -> result >= 0;
                case LT -> result < 0;
                case LE -> result <= 0;
            };
        }
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.compare(b, type);
    }

    @Override
    protected Optional<SemanticError> validateExpressionInternal(ExpressionType lhs, ExpressionType rhs) {
        if (lhs != rhs) {
            return Optional.of(new SemanticError("Cannot compare expression of type: '" + lhs + "' with type: '" + rhs + "'"));
        }

        if (type != EqualityOperationType.EQ && type != EqualityOperationType.NE) {
            return switch (lhs) {
                case ExpressionType.COLOR, ExpressionType.BOOL
                        -> Optional.of(new SemanticError("Cannot compare expression of type: '" + lhs + "' with type: '" + rhs + "' using the '" + operatorToken.getText() + "' operator"));
                default -> Optional.empty();
            };
        }

        return Optional.empty();
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        return new Result.Success<>(ExpressionType.BOOL); // comparison always returns a boolean
    }
}
