package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.checker.Checker;
import org.checkerframework.checker.nullness.Opt;

import java.util.*;

public class Evaluator implements Transform {

    private final IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        Result<List<ASTNode>, EvaluationError> styleSheetBody = evaluateBody(ast.root);
        if (styleSheetBody.isError()) { // evalution errors should never happen as the checker should have caught them
            throw new RuntimeException(styleSheetBody.error().toString());
        }

        ast.root.body = styleSheetBody.value();

    }

    private Result<List<ASTNode>, EvaluationError> evaluateBody(BodyStatement body) {
        ArrayList<ASTNode> newBody = new ArrayList<>();
        variableValues.addFirst(new HashMap<>());

        for (int i = 0; i < body.body.size(); i++) {
            ASTNode child = body.body.get(i);
            if (child instanceof IfClause ifClause) {
                Result<Literal, EvaluationError> result = ifClause.conditionalExpression.tryEvaluate(variableValues);
                if (result.isError()) {
                    return new Result.Error<>(result.error());
                }

                Literal literal = result.value();
                if (!(literal instanceof BoolLiteral bool)) {
                    return new Result.Error<>(new InvalidType(literal.getNodeLabel(), "BOOL"));
                }


                if (bool.value) {
                    Result<List<ASTNode>, EvaluationError> newIfBody = evaluateBody(ifClause);
                    if (newIfBody.isError()) {
                        return newIfBody;
                    }

                    newBody.addAll(newIfBody.value());
                } else {
                    Result<List<ASTNode>, EvaluationError> newElseBody = evaluateBody(ifClause.elseClause);
                    if (newElseBody.isError()) {
                        return newElseBody;
                    }

                    newBody.addAll(newElseBody.value());
                }

                continue;
            }

            if (child instanceof BodyStatement innerBody) {
                Result<List<ASTNode>, EvaluationError> newInnerBody = evaluateBody(innerBody);
                if (newInnerBody.isError()) {
                    return newInnerBody;
                }

                innerBody.body = newInnerBody.value();
            }

            Result<Optional<ASTNode>, EvaluationError> evaluation = evaluate(child);
            if (evaluation.isError()) {
                return new Result.Error<>(evaluation.error());
            }
            if (evaluation.value().isPresent()) {
                newBody.add(evaluation.value().get());
            }
        }

        Checker.popVariablesOffStack(variableValues, body);

        return new Result.Success<>(newBody);
    }

    private Result<Optional<ASTNode>, EvaluationError> evaluate(ASTNode node) {
        if (node instanceof VariableAssignment variableAssignment) {
            Expression expression = variableAssignment.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(variableValues);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            variableAssignment.expression = literal.value();

            HashMap<String, Literal> map =  variableValues.getFirst();
            map.put(variableAssignment.name.name, literal.value());

            return new Result.Success<>(Optional.empty());
        } else if (node instanceof Declaration declaration) {
            Expression expression = declaration.expression;

            Result<Literal, EvaluationError> literal = expression.tryEvaluate(variableValues);
            if (literal.isError()) {
                return new Result.Error<>(literal.error());
            }

            declaration.expression = literal.value();
        }

        return new Result.Success<>(Optional.of(node));
    }

    
}
