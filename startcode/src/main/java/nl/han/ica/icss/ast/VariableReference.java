package nl.han.ica.icss.ast;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.transforms.EvaluationError;

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
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int hashCode() {

		return Objects.hash(name);
	}
}
