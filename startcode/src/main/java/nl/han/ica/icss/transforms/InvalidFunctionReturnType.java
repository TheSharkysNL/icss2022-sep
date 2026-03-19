package nl.han.ica.icss.transforms;

public class InvalidFunctionReturnType extends EvaluationError {
    public String returnType;

    public InvalidFunctionReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "Invalid return type, expected a expression but got: '" + returnType + "'.";
    }
}
