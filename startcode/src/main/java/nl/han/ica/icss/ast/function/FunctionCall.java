package nl.han.ica.icss.ast.function;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;
import nl.han.ica.icss.transforms.FunctionNotFound;
import nl.han.ica.icss.transforms.InvalidFunctionReturnType;

import java.util.*;
import java.util.function.Function;

public class FunctionCall extends Expression {
    public String name;
    public List<Expression> arguments;

    public FunctionCall(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    private <T> T evaluateFunctionCall(Evaluator evaluator, Function<ReturnStatement, T> function, Function<Result<List<ASTNode>, EvaluationError>, T> mapper) {
        Optional<FunctionDeclaration> func = evaluator.getFunction(name);
        if (func.isEmpty()) {
            return mapper.apply(new Result.Error<>(new FunctionNotFound(name)));
        }

        Result<List<ASTNode>, EvaluationError> result = evaluator.evaluateFunction(func.get(), arguments);
        if (result.isError()) {
            return mapper.apply(new Result.Error<>(result.error()));
        }

        for (ASTNode node : result.value()) {
            if (node instanceof ReturnStatement returnStatement) {
                return function.apply(returnStatement);
            }
        }

        return mapper.apply(new Result.Error<>(new InvalidFunctionReturnType("void")));
    }

    @Override
    public Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator) {
        return evaluateFunctionCall(evaluator, returnStatement -> {
            if (returnStatement instanceof ExpressionReturnStatement expression) {
                return expression.expression.tryEvaluate(evaluator);
            }

            return new Result.Error<>(new InvalidFunctionReturnType("style"));
        }, result -> new Result.Error<>(result.error()));
    }

    public Optional<Stylerule> evaluateStyle(Evaluator evaluator) {
        return evaluateFunctionCall(evaluator, returnStatement -> {
            if (returnStatement instanceof StyleReturnStatement styleReturnStatement) {
                return Optional.of(styleReturnStatement.style);
            }

            return Optional.empty();
        }, ignored -> Optional.empty());
    }

    @Override
    public Optional<SemanticError> validateExpression(Checker checker) {
        Optional<FunctionDeclaration> functionDeclaration = checker.getFunction(name);
        if (functionDeclaration.isEmpty()) {
            return Optional.of(new SemanticError("Could not find the function: '" + name + "'."));
        }

        return checker.validateFunctionCall(functionDeclaration.get(), arguments);
    }

    @Override
    public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
        Optional<FunctionDeclaration> functionDeclaration = checker.getFunction(name);
        if (functionDeclaration.isEmpty()) {
            return new Result.Error<>(new SemanticError("Could not find the function: '" + name + "'."));
        }

        return checker.getFunctionCallExpressionType(functionDeclaration.get(), arguments);
    }

    @Override
    public String getNodeLabel() {
        return "FuncCall";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FunctionCall that = (FunctionCall) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public List<ASTNode> getChildren() {
        return new ArrayList<>(arguments);
    }
}
