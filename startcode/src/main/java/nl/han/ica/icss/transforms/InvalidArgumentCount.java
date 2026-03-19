package nl.han.ica.icss.transforms;

public class InvalidArgumentCount extends EvaluationError {
    private final int parameterCount;
    private final int argumentCount;

    private final String name;

    public InvalidArgumentCount(int parameterCount, int argumentCount, String functionName) {
        this.parameterCount = parameterCount;
        this.argumentCount = argumentCount;
        this.name = functionName;
    }

    @Override
    public String toString() {
        return "Invalid argument count for the function: '" + name + "' expected " + parameterCount + " argument(s) but got " + argumentCount + " argument(s)";
    }
}
