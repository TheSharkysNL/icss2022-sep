package nl.han.ica.icss.transforms;

public class InvalidType extends EvaluationError {
    private final String gotType;
    private final String expectedType;

    public InvalidType(String gotType, String expectedType) {
        this.gotType = gotType;
        this.expectedType = expectedType;
    }

    @Override
    public String toString() {
        return "Expected the type: '" + expectedType + "' but got the type: '" + gotType + "'";
    }
}
