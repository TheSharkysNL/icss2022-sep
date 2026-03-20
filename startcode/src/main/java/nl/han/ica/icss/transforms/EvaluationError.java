package nl.han.ica.icss.transforms;

public class EvaluationError {
    private final String message;

    public EvaluationError() {
        message = "An error occurred whilst evaluation the program.";
    }

    public EvaluationError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
