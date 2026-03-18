package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.*;


public class Checker {

    private final IHANLinkedList<HashMap<String, ExpressionType>> variableTypes = new HANLinkedList<>();


    public void check(AST ast) {
        checkBodyStatements(ast.root);
    }

    private void checkBodyStatements(BodyStatement body) {
        variableTypes.addFirst(new HashMap<>());

        for (ASTNode child : body.body) {
            if (child instanceof VariableAssignment assignment) {
                String name = assignment.name.name;

                Result<ExpressionType, SemanticError> type = assignment.expression.getExpressionType(variableTypes);
                if (type.isError()) {
                    assignment.expression.setError(type.error());
                    continue;
                }

                Optional<SemanticError> isValidExpression = assignment.expression.validateExpression(variableTypes);
                if (isValidExpression.isPresent()) {
                    assignment.expression.setError(isValidExpression.get());
                    continue;
                }

                HashMap<String, ExpressionType> scopedVars = variableTypes.getFirst();
                scopedVars.put(name, type.value());

                continue;
            }

            if (child instanceof Declaration declaration) {
                Optional<SemanticError> isValidExpression = declaration.expression.validateExpression(variableTypes);
                if (isValidExpression.isPresent()) {
                    declaration.setError(isValidExpression.get());
                    continue;
                }
            }

            if (child instanceof IfClause ifClause) {
                Result<ExpressionType, SemanticError> result = ifClause.conditionalExpression.getExpressionType(variableTypes);
                if (result.isError()) {
                    ifClause.setError(result.error());
                }

                if (result.value() != ExpressionType.BOOL) {
                    ifClause.setError("The conditional expression of the if statement must be a boolean expression.");
                }
            }

            if (child instanceof BodyStatement innerBody) {
                checkBodyStatements(innerBody);
            }
        }

        popVariablesOffStack(variableTypes, body);
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
