package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.ExpressionReturnStatement;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
import nl.han.ica.icss.ast.function.StyleReturnStatement;
import nl.han.ica.icss.ast.types.ExpressionType;
import org.checkerframework.framework.qual.LiteralKind;

import java.util.*;
import java.util.function.Function;


public class Checker {

    private final IHANLinkedList<HashMap<String, ExpressionType>> variableTypes = new HANLinkedList<>();
    private final IHANLinkedList<HashMap<String, FunctionDeclaration>> functions = new HANLinkedList<>();

    public void check(AST ast) {
        checkBodyStatements(ast.root);
    }

    public Optional<ExpressionType> getVariable(String name) {
        return getValueFromStack(variableTypes, name);
    }

    public Optional<FunctionDeclaration> getFunction(String name) {
        return getValueFromStack(functions, name);
    }

    private <T> T checkFunctionDeclaration(FunctionDeclaration declaration, List<Expression> arguments, Function<FunctionDeclaration, T> function, Function<SemanticError, T> mapper) {
        if (declaration.parameters.size() != arguments.size()) {
            return mapper.apply(new SemanticError("Invalid argument count, expected: " + declaration.parameters.size() + " arguments but got " + arguments.size() + " arguments"));
        }

        HashMap<String, ExpressionType> parameterExpressionMap = new HashMap<>(arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            Expression argument = arguments.get(i);
            String parameter = declaration.parameters.get(i);

            Result<ExpressionType, SemanticError> result = argument.getExpressionType(this);
            if (result.isError()) {
                return mapper.apply(result.error());
            }

            parameterExpressionMap.put(parameter, result.value());
        }

        variableTypes.addFirst(parameterExpressionMap);

        T result = function.apply(declaration);

        variableTypes.removeFirst();

        return result;
    }

    private Result<Optional<ExpressionType>, SemanticError> tryGetReturnValue(BodyStatement bodyStatement) {
        Optional<ExpressionType> expressionType = Optional.empty();

        for (ASTNode node : bodyStatement.body) {
            if (node instanceof ExpressionReturnStatement returnStatement) {
                Result<ExpressionType, SemanticError> result = returnStatement.expression.getExpressionType(this);
                if (result.isError()) {
                    return new Result.Error<>(result.error());
                }
                return new Result.Success<>(Optional.of(result.value()));
            }

            if (node instanceof StyleReturnStatement) {
                return new Result.Success<>(Optional.of(ExpressionType.UNDEFINED));
            }

            if (node instanceof BodyStatement innerBody) {
                Result<Optional<ExpressionType>, SemanticError> result = tryGetReturnValue(innerBody);
                if (result.isError()) {
                    return result;
                }

                if (result.value().isPresent()) {
                    if (expressionType.isEmpty()) {
                        expressionType = result.value();
                    } else {
                        if (result.value().get() != expressionType.get()) {
                            return new Result.Error<>(new SemanticError("Function returns multiple data types."));
                        }
                    }
                }
            }
        }

        return new Result.Success<>(Optional.empty());
    }

    public Result<ExpressionType, SemanticError> getFunctionCallExpressionType(FunctionDeclaration functionDeclaration, List<Expression> arguments) {
        return checkFunctionDeclaration(functionDeclaration, arguments, declaration -> {
            checkBodyStatements(declaration);

            Result<Optional<ExpressionType>, SemanticError> result = tryGetReturnValue(declaration);

            if (result.isError()) {
                return new Result.Error<>(result.error());
            }

            if (result.value().isPresent()) {
                return new Result.Success<>(result.value().get());
            }

            return new Result.Error<>(new SemanticError("Expression invalid as function returns 'void'."));
        }, Result.Error::new);
    }

    private Optional<SemanticError> validateReturnExpression(BodyStatement bodyStatement) {
        for (ASTNode node : bodyStatement.body) {
            if (node instanceof ExpressionReturnStatement returnStatement) {
                return returnStatement.expression.validateExpression(this); // When return statement is given then don't check other statements in body
            }

            if (node instanceof BodyStatement innerBody) {
                return validateReturnExpression(innerBody);
            }
        }

        return Optional.empty();
    }

    public Optional<SemanticError> validateFunctionCall(FunctionDeclaration functionDeclaration, List<Expression> arguments) {
        return checkFunctionDeclaration(functionDeclaration, arguments, declaration -> {
            checkBodyStatements(declaration);

            return validateReturnExpression(declaration);
        }, Optional::of);
    }

    private void checkBodyStatements(BodyStatement body) {
        variableTypes.addFirst(new HashMap<>());
        functions.addFirst(new HashMap<>());

        for (ASTNode child : body.getChildren()) {
            if (child instanceof FunctionDeclaration declaration) {
                functions.getFirst()
                        .put(declaration.name, declaration);
                continue; // skip as body will be checked later
            }

            if (child instanceof VariableAssignment assignment) {
                String name = assignment.name.name;

                Result<ExpressionType, SemanticError> type = assignment.expression.getExpressionType(this);
                if (type.isError()) {
                    assignment.expression.setError(type.error());
                    continue;
                }

                Optional<SemanticError> isValidExpression = assignment.expression.validateExpression(this);
                if (isValidExpression.isPresent()) {
                    assignment.expression.setError(isValidExpression.get());
                    continue;
                }

                HashMap<String, ExpressionType> scopedVars = variableTypes.getFirst();
                scopedVars.put(name, type.value());

                continue;
            }

            if (child instanceof Declaration declaration) {
                Optional<SemanticError> isValidExpression = declaration.expression.validateExpression(this);
                if (isValidExpression.isPresent()) {
                    declaration.setError(isValidExpression.get());
                    continue;
                }
            }

            if (child instanceof IfClause ifClause) {
                Result<ExpressionType, SemanticError> result = ifClause.conditionalExpression.getExpressionType(this);
                if (result.isError()) {
                    ifClause.setError(result.error());
                } else {
                    if (result.value() != ExpressionType.BOOL) {
                        ifClause.setError("The conditional expression of the if statement must be a boolean expression.");
                    }
                }
            }

            if (child instanceof ForStatement forStatement) {
                Result<ExpressionType, SemanticError> result = forStatement.loopExpression.getExpressionType(this);
                if (result.isError()) {
                    forStatement.setError(result.error());
                } else {
                    if (result.value() != ExpressionType.BOOL) {
                        forStatement.setError("The conditional expression of the for statement must be a boolean expression.");
                    }
                }
            }

            if (child instanceof BodyStatement innerBody) {
                checkBodyStatements(innerBody);
            }
        }

        popVariablesOffStack(variableTypes, body);
        functions.removeFirst();
    }

    public static <T> Optional<T> getValueFromStack(IHANLinkedList<HashMap<String, T>> stack, String name) {
        for (HashMap<String, T> map : stack) {
            if (map.containsKey(name)) {
                return Optional.of(map.get(name));
            }
        }

        return Optional.empty();
    }

    public static <T> void popVariablesOffStack(IHANLinkedList<HashMap<String, T>> variables, BodyStatement parent) {
        if (parent instanceof IfClause) { // only place variables in higher stacks if the variable was used within a if clause
            HashMap<String, T> lastOnStack = variables.getFirst();
            variables.removeFirst();
            HashMap<String, T> newLastOnStack = variables.getFirst();
            if (newLastOnStack != null) {
                for (Map.Entry<String, T> entry : lastOnStack.entrySet()) {
                    if (newLastOnStack.containsKey(entry.getKey())) { // if variable exists in a higher stack then replace its value
                        newLastOnStack.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
            variables.removeFirst();
        }
    }
}
