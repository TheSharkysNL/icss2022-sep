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

public class LogicalOperation extends Operation {
    public final LogicalOperationType type;
    private final Token token;

    public LogicalOperation(Expression lhs, Expression rhs, Token token) {
        super(lhs, rhs);

        type = switch (token.getType()) {
            case ICSSParser.AND -> LogicalOperationType.AND;
            case ICSSParser.OR -> LogicalOperationType.OR;
            default -> throw new IllegalArgumentException("Invalid token type as logical operation: '" + token.getText() + "'.");
        };
        this.token = token;
    }

    public enum LogicalOperationType {
        AND,
        OR;

        public <T extends Comparable<T>> boolean evaluate(int lhs, int rhs) {
            return switch (this) {
                case AND -> (lhs > 0) && (rhs > 0);
                case OR -> (lhs > 0) || (rhs > 0);
            };
        }
    }

    @Override
    protected Result<Literal, EvaluationError> applyOperation(Literal a, Literal b) {
        return a.logicalComparison(b, type);
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        return Result.of(ExpressionType.bool()); // logical expression always boolean
    }

    @Override
    protected Optional<SemanticError> validateExpressionInternal(ExpressionType lhs, ExpressionType rhs) {
        if (lhs != ExpressionType.bool()) {
            return Optional.of(new SemanticError("Left hand side of " + token + " expression must be a boolean."));
        }

        if (rhs != ExpressionType.bool()) {
            return Optional.of(new SemanticError("Left hand side of " + token + " expression must be a boolean."));
        }

        return Optional.empty();
    }
}
