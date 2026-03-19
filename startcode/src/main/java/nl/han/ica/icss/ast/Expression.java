package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;

import java.util.HashMap;
import java.util.Optional;

public abstract class Expression extends ASTNode {
    public abstract Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator);

    public abstract Optional<SemanticError> validateExpression(Checker checker);
    public abstract Result<ExpressionType, SemanticError> getExpressionType(Checker checker);
}
