package nl.han.ica.icss.ast.function;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.BodyStatement;
import nl.han.ica.icss.ast.VariableAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionDeclaration extends BodyStatement {
    public String name;
    public List<String> parameters;

    public FunctionDeclaration(String name, List<String> parameters, List<ASTNode> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "FunctionDeclaration (" + name + "[ " + String.join(", ", parameters) + "] )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FunctionDeclaration that = (FunctionDeclaration) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters, body);
    }
}
