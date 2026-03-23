package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;

import java.util.Optional;

public class NegateOperation extends Expression {
    private final Expression expression;

    public NegateOperation(Expression expression) {
        this.expression = expression;
    }


    @Override
    public Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator) {
        Result<Literal, EvaluationError> eval = expression.tryEvaluate(evaluator);
        if (eval.isError()) {
            return eval;
        }

        return eval.value().negate();
    }

    @Override
    public Optional<SemanticError> validateExpression(Checker checker) {
        Optional<SemanticError> validate = expression.validateExpression(checker);
        if (validate.isPresent()) {
            return validate;
        }

        Result<ExpressionType, SemanticError> expressionType = expression.getExpressionType(checker);
        if (expressionType.isError()) {
            return expressionType.ok();
        }

        if (expressionType.value() != ExpressionType.BOOL) {
            return Optional.of(new SemanticError("Cannot negate a expression of the type: '" + expressionType.value() + "' must be a boolean expression."));
        }

        return Optional.empty();
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        return Result.of(ExpressionType.BOOL); // negate always returns a boolean
    }

    @Override
    public String getNodeLabel() {
        return "Negate";
    }
}
