package nl.han.ica.icss.ast.function;

import nl.han.ica.icss.ast.ASTNode;

import java.util.List;

public class ImportedFunctionDeclaration extends FunctionDeclaration {
    public final String location; // location of the file from which the function originated

    public ImportedFunctionDeclaration(String location, String name, List<String> parameters, List<ASTNode> body) {
        super(name, parameters, body);

        this.location = location;
    }
}
