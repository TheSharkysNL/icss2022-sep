package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.SemanticError;
import nl.han.ica.icss.transforms.EvaluationError;
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
	public Result<Literal, EvaluationError> tryEvaluate(IHANLinkedList<HashMap<String, Literal>> variables) {
		for (int i = 0; i < variables.getSize(); i++) {
			HashMap<String, Literal> map = variables.get(i);

			Literal variableLit = map.get(name);
			if (variableLit != null) {
				return new Result.Success<>(variableLit);
			}
		}

		return new Result.Error<>(new VariableNotFound(name));
	}

	@Override
	public Optional<SemanticError> validateExpression(IHANLinkedList<HashMap<String, ExpressionType>> variables) {
		return getExpressionType(variables)
				.ok();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public Result<ExpressionType, SemanticError> getExpressionType(IHANLinkedList<HashMap<String, ExpressionType>> variables) {
		for (int i = 0; i < variables.getSize(); i++) {
			HashMap<String, ExpressionType> map = variables.get(i);
			ExpressionType type = map.get(name);

			if (type != null) {
				return new Result.Success<>(type);
			}
		}

		return new Result.Error<>(new SemanticError("Variable not found: \"" + name + "\""));
	}
}
