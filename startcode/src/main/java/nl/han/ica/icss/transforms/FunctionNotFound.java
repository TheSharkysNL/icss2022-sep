package nl.han.ica.icss.transforms;

public class FunctionNotFound extends EvaluationError {
    private final String name;

    public FunctionNotFound(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Could not find the function with the name: '" + name + "'";
    }
}