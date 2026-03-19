package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.ExpressionReturnStatement;
import nl.han.ica.icss.ast.function.FunctionCall;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
import nl.han.ica.icss.ast.function.StyleReturnStatement;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.checker.Checker;
import org.checkerframework.checker.nullness.Opt;

import java.util.*;
import java.util.stream.Collectors;

public class Evaluator implements Transform {

    private final IHANLinkedList<HashMap<String, Literal>> variableValues;
    private final IHANLinkedList<HashMap<String, FunctionDeclaration>> functions;

    public Evaluator() {
        functions = new HANLinkedList<>();
        variableValues = new HANLinkedList<>();
    }

    public Optional<Literal> getVariable(String name) {
        return Checker.getValueFromStack(variableValues, name);
    }

    public Optional<FunctionDeclaration> getFunction(String name) {
        return Checker.getValueFromStack(functions, name);
    }

    @Override
    public void apply(AST ast) {
        Result<List<ASTNode>, EvaluationError> styleSheetBody = evaluateBody(ast.root, false);
        if (styleSheetBody.isError()) { // evalution errors should never happen as the checker should have caught them
            throw new RuntimeException(styleSheetBody.error().toString());
        }

        ast.root.body = styleSheetBody.value();

    }

    public Result<List<ASTNode>, EvaluationError> evaluateFunction(FunctionDeclaration functionDeclaration, List<Expression> arguments) {
        if (functionDeclaration.parameters.size() != arguments.size()) {
            return new Result.Error<>(new InvalidArgumentCount(functionDeclaration.parameters.size(), arguments.size(), functionDeclaration.name));
        }

        HashMap<String, Literal> functionVariables = new HashMap<>(arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            Expression argument = arguments.get(i);
            String parameter = functionDeclaration.parameters.get(i);

            Result<Literal, EvaluationError> result = argument.tryEvaluate(this);
            if (result.isError()) {
                return new Result.Error<>(result.error());
            }

            functionVariables.put(parameter, result.value());
        }

        variableValues.addFirst(functionVariables);
        Result<List<ASTNode>, EvaluationError> result = evaluateBody(functionDeclaration, true);
        variableValues.removeFirst();
        return result;
    }

    private Result<List<ASTNode>, EvaluationError> evaluateBody(BodyStatement body, boolean isInFunction) {
        ArrayList<ASTNode> newBody = new ArrayList<>();
        variableValues.addFirst(new HashMap<>());
        functions.addFirst(new HashMap<>());

        for (int i = 0; i < body.body.size(); i++) {
            ASTNode child = body.body.get(i);
            if (child instanceof IfClause ifClause) {
                Result<Literal, EvaluationError> result = ifClause.conditionalExpression.tryEvaluate(this);
                if (result.isError()) {
                    return new Result.Error<>(result.error());
                }

                Literal literal = result.value();
                if (!(literal instanceof BoolLiteral bool)) {
                    return new Result.Error<>(new InvalidType(literal.getNodeLabel(), "BOOL"));
                }


                if (bool.value) {
                    Result<List<ASTNode>, EvaluationError> newIfBody = evaluateBody(ifClause, isInFunction);
                    if (newIfBody.isError()) {
                        return newIfBody;
                    }

                    newBody.addAll(newIfBody.value());
                } else {
                    Result<List<ASTNode>, EvaluationError> newElseBody = evaluateBody(ifClause.elseClause, isInFunction);
                    if (newElseBody.isError()) {
                        return newElseBody;
                    }

                    newBody.addAll(newElseBody.value());
                }

                continue;
            }

            if (child instanceof FunctionDeclaration functionDeclaration) {
                functions.getFirst()
                        .put(functionDeclaration.name, functionDeclaration);
                continue; // Skip for now as the function will be evaluated later at a function call expression.
            }

            if (child instanceof BodyStatement innerBody) {
                Result<List<ASTNode>, EvaluationError> newInnerBody = evaluateBody(innerBody, isInFunction);
                if (newInnerBody.isError()) {
                    return newInnerBody;
                }

                innerBody.body = newInnerBody.value();
            }

            Result<Optional<ASTNode>, EvaluationError> evaluation = evaluate(child, isInFunction);
            if (evaluation.isError()) {
                return new Result.Error<>(evaluation.error());
            }
            if (evaluation.value().isPresent()) {
                newBody.add(evaluation.value().get());
            }
        }

        Checker.popVariablesOffStack(variableValues, body);
        functions.removeFirst();

        return new Result.Success<>(newBody);
    }

    private Result<Optional<ASTNode>, EvaluationError> evaluate(ASTNode node, boolean isInFunction) {
        if (node instanceof VariableAssignment variableAssignment) {
            Expression expression = variableAssignment.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            variableAssignment.expression = literal.value();

            HashMap<String, Literal> map =  variableValues.getFirst();
            map.put(variableAssignment.name.name, literal.value());

            return new Result.Success<>(Optional.empty());
        } else if (node instanceof Declaration declaration) {
            Expression expression = declaration.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            declaration.expression = literal.value();
        } else if (isInFunction && node instanceof ExpressionReturnStatement returnStatement) {
            Expression expression = returnStatement.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            returnStatement.expression = literal.value();
        } else if (isInFunction && node instanceof StyleReturnStatement styleReturnStatement) {
            Result<List<ASTNode>, EvaluationError> result = evaluateBody(styleReturnStatement.style, isInFunction);
            if (result.isError()) {
                return new Result.Error<>(result.error());
            }

            styleReturnStatement.style.body = result.value();
        } else if (node instanceof FunctionCall functionCall) {
            Optional<Stylerule> style = functionCall.evaluateStyle(this);
            if (style.isPresent()) {
                return new Result.Success<>(Optional.of(style.get())); // add the new style to the view
            }
        }

        return new Result.Success<>(Optional.of(node));
    }

    
}
