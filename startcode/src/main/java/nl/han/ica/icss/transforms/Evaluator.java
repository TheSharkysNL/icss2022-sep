package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.*;
import nl.han.ica.icss.ast.iImport.ImportStatement;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import org.checkerframework.checker.nullness.Opt;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Evaluator implements Transform {

    private final IHANLinkedList<HashMap<String, Literal>> variableValues;
    private final IHANLinkedList<HashMap<String, FunctionDeclaration>> functions;
    private final HashMap<String, HashMap<String, Literal>> importedVariableValues;

    public Evaluator() {
        functions = new HANLinkedList<>();
        variableValues = new HANLinkedList<>();
        importedVariableValues = new HashMap<>();
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

        if (functionDeclaration instanceof ImportedFunctionDeclaration importedFunctionDeclaration) {
            String location = importedFunctionDeclaration.location;

            HashMap<String, Literal> variables = importedVariableValues.get(location);
            variableValues.addFirst(variables);
        }

        variableValues.addFirst(functionVariables);
        Result<List<ASTNode>, EvaluationError> result = evaluateBody(functionDeclaration, true);
        variableValues.removeFirst();

        if (functionDeclaration instanceof ImportedFunctionDeclaration) {
            variableValues.removeFirst();
        }

        return result;
    }

    public Result<List<ASTNode>, EvaluationError> evaluateFor(ForStatement forStatement, boolean isInFunction) {
        List<ASTNode> newBody = new ArrayList<>();
        variableValues.addFirst(new HashMap<>());

        for (VariableAssignment variableAssignment : forStatement.initialAssignments) { // evaluate all initial assignments
            Optional<EvaluationError> error = evaluateVariableAssignment(variableAssignment);
            if (error.isPresent()) {
                return new Result.Error<>(error.get());
            }
        }

        while (true) {
            Result<Literal, EvaluationError> expressionEvaluation = forStatement.loopExpression.tryEvaluate(this);

            if (expressionEvaluation.isError()) {
                return new Result.Error<>(expressionEvaluation.error());
            }

            if (!(expressionEvaluation.value() instanceof BoolLiteral bool)) {
                return new Result.Error<>(new InvalidType(expressionEvaluation.value().getNodeLabel(), "BOOL"));
            }

            if (!bool.value) { // check expression to see if the loop is at the end
                break;
            }

            Result<List<ASTNode>, EvaluationError> result = evaluateBody(forStatement, isInFunction); // evaluate the for loop body with the current variables
            if (result.isError()) {
                return result;
            }

            newBody.addAll(result.value());

            for (VariableAssignment loopVariableAssignments : forStatement.loopAssignments) { // update the variables for the loop
                Optional<EvaluationError> error = evaluateVariableAssignment(loopVariableAssignments);
                if (error.isPresent()) {
                    return new Result.Error<>(error.get());
                }
            }
        }

        variableValues.removeFirst();
        return new Result.Success<>(newBody);
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

            if (child instanceof ForStatement forStatement) {
                Result<List<ASTNode>, EvaluationError> result = evaluateFor(forStatement, isInFunction);
                if (result.isError()) {
                    return result;
                }

                newBody.addAll(result.value());
                continue;
            }

            if (child instanceof FunctionDeclaration functionDeclaration) {
                functions.getFirst()
                        .put(functionDeclaration.name, functionDeclaration);
                newBody.add(functionDeclaration);
                continue; // Skip for now as the function will be evaluated later at a function call expression.
            }

            if (child instanceof ImportStatement importStatement) {
                if (importStatement.isImported) {
                    continue;
                }
                importStatement.isImported = true; // so that a function call doesn't evaluate imports more than once

                newBody.addAll(importStatement.getImportedStyleRules());
                for (FunctionDeclaration functionDeclaration : importStatement.getImportedFunctions()) {
                    functions.getFirst()
                            .put(functionDeclaration.name, functionDeclaration);
                }
                HashMap<String, Literal> importedVariables = new HashMap<>(importStatement.getImportedVariables().size());
                for (VariableAssignment variableAssignment : importStatement.getImportedVariables()) {
                    // expression here should already be a literal as it should be evaluated by the import statement
                    importedVariables.put(variableAssignment.name.name, (Literal)variableAssignment.expression);
                }
                importedVariableValues.put(importStatement.location, importedVariables);

                continue;
            }

            if (child instanceof BodyStatement innerBody) {
                Result<List<ASTNode>, EvaluationError> newInnerBody = evaluateBody(innerBody, isInFunction);
                if (newInnerBody.isError()) {
                    return newInnerBody;
                }

                if (!isInFunction) {
                    innerBody.body = newInnerBody.value();
                }
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

    private Optional<EvaluationError> evaluateVariableAssignment(VariableAssignment variableAssignment) {
        Expression expression = variableAssignment.expression;

        Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
        if (literal.isError()) {
            return literal.ok();
        }

        HashMap<String, Literal> map =  variableValues.getFirst();
        map.put(variableAssignment.name.name, literal.value());

        return Optional.empty();
    }

    private Result<Optional<ASTNode>, EvaluationError> evaluate(ASTNode node, boolean isInFunction) {
        if (node instanceof VariableAssignment variableAssignment) {
            return evaluateVariableAssignment(variableAssignment)
                    .map(error -> (Result<Optional<ASTNode>, EvaluationError>)new Result.Error<Optional<ASTNode>, EvaluationError>(error))
                    .orElse(new Result.Success<>(Optional.of(new VariableAssignment(variableAssignment.name, variableAssignment.expression.tryEvaluate(this).value()))));
        } else if (node instanceof Declaration declaration) {
            Expression expression = declaration.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            return new Result.Success<>(Optional.of(new Declaration(declaration.property.name, literal.value())));
        } else if (isInFunction && node instanceof ExpressionReturnStatement returnStatement) {
            Expression expression = returnStatement.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(this);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            return new Result.Success<>(Optional.of(new ExpressionReturnStatement(literal.value())));
        } else if (isInFunction && node instanceof StyleReturnStatement styleReturnStatement) {
            Result<List<ASTNode>, EvaluationError> result = evaluateBody(styleReturnStatement.style, isInFunction);
            if (result.isError()) {
                return new Result.Error<>(result.error());
            }

            Stylerule stylerule = new Stylerule(styleReturnStatement.style.selectors, result.value());
            return new Result.Success<>(Optional.of(new StyleReturnStatement(stylerule)));
        } else if (node instanceof FunctionCall functionCall) {
            Optional<Stylerule> style = functionCall.evaluateStyle(this);
            if (style.isPresent()) {
                return new Result.Success<>(Optional.of(style.get())); // add the new style to the view
            }
        }

        return new Result.Success<>(Optional.of(node));
    }

    
}
