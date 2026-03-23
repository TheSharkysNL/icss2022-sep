package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
import nl.han.ica.icss.transforms.Evaluator;
import nl.han.ica.icss.transforms.VariableNotFound;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class VariableReference extends Expression {

	public String name;
	
	public VariableReference(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getNodeLabel() {
		return "VariableReference (" + name + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		VariableReference that = (VariableReference) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public Result<Literal, EvaluationError> tryEvaluate(Evaluator evaluator) {
	 	Optional<Literal> value = evaluator.getVariable(name);
		if (value.isPresent()) {
		 return Result.of(value.get());
		}

		return Result.err(new VariableNotFound(name));
	}

	@Override
	public Optional<SemanticError> validateExpression(Checker checker) {
		return getExpressionType(checker)
				.ok();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public Result<ExpressionType, SemanticError> getExpressionType(Checker checker) {
		Optional<ExpressionType> expressionType = checker.getVariable(name);
		if (expressionType.isPresent()) {
			return Result.of(expressionType.get());
		}

		return Result.err(new SemanticError("Variable not found: \"" + name + "\""));
	}
}
