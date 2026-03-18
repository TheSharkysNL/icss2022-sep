package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;

import java.util.HashMap;
import java.util.Optional;

public abstract class Expression extends ASTNode {
    public abstract Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables);

    public abstract Optional<SemanticError> validateExpression(IHANLinkedList<HashMap<String, ExpressionType>> variables);
    public abstract Result<ExpressionType, SemanticError> getExpressionType(IHANLinkedList<HashMap<String, ExpressionType>> variables);
}
