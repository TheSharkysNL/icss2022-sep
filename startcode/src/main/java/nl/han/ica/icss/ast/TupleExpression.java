package nl.han.ica.icss.ast;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.literals.TupleLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TupleExpression extends Expression {
    public final List<Expression> expressions;

    public TupleExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator) {
        ArrayList<Literal> evals = new ArrayList<>();
        for (Expression expression : expressions) {
            Result<Literal, EvaluationError> result = expression.tryEvaluate(evaluator);
            if (result.isError()) {
                return result;
            }

            evals.add(result.value());
        }

        return Result.of(new TupleLiteral(evals));
    }

    @Override
    public Optional<SemanticError> validateExpression(Checker checker) {
        for (Expression expression : expressions) {
            Optional<SemanticError> error = expression.validateExpression(checker);
            if (error.isPresent()) {
                return error;
            }
        }

        return Optional.empty();
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        return Result.of(ExpressionType.TUPLE);
    }

    @Override
    public String getNodeLabel() {
        return "Tuple";
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(expressions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TupleExpression that = (TupleExpression) o;
        return Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expressions);
    }
}
