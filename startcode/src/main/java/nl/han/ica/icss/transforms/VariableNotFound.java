package nl.han.ica.icss.transforms;

public class VariableNotFound extends EvaluationError {
    private final String name;

    public VariableNotFound(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Could not find the variable with the name: '" + name + "'";
    }
}
