package nl.han.ica.icss.ast.iImport;

import nl.han.ica.icss.Pipeline;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.TransformationConfig;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
import nl.han.ica.icss.ast.function.ImportedFunctionDeclaration;
import nl.han.ica.icss.checker.Checker;
import nl.han.ica.icss.checker.SemanticError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ImportStatement extends ASTNode {
    public final String location;
    public final List<ImportType> importTypes;

    public boolean isImported;

    private final List<FunctionDeclaration> importedFunctions = new ArrayList<>();
    private final List<Stylerule> importedStyleRules = new ArrayList<>();
    private final List<VariableAssignment> importedVariables = new ArrayList<>();

    public ImportStatement(String location, List<ImportType> importTypes) {
        this.location = location;
        this.importTypes = importTypes;
    }

    private void setImportedValues(AST ast) {
        HashSet<String> neededFunctions = new HashSet<>();
        boolean includeStyles = false;
        boolean useWildcardForFunctions = false;

        for (ImportType importType : importTypes) {
            if (importType instanceof ImportType.Functions(List<ImportItem> items) && !useWildcardForFunctions) {
                for (ImportItem item : items) {
                    switch (item.type) {
                        case ImportItem.ImportItemType.Named(String name) -> neededFunctions.add(name);
                        case ImportItem.ImportItemType.WildCard() -> useWildcardForFunctions = true;
                    }
                }
            } else if (importType instanceof ImportType.Styles) {
                includeStyles = true;
            } else {
                useWildcardForFunctions = true;
                includeStyles = true;
            }
        }

        for (ASTNode node : ast.root.body) {
            if (node instanceof FunctionDeclaration functionDeclaration && (useWildcardForFunctions || neededFunctions.contains(functionDeclaration.name))) {
                importedFunctions.add(new ImportedFunctionDeclaration(location, functionDeclaration.name, functionDeclaration.parameters, functionDeclaration.body));
            } else if (node instanceof Stylerule stylerule && includeStyles) {
                importedStyleRules.add(stylerule);
            } else if (node instanceof VariableAssignment variableAssignment) {
                importedVariables.add(variableAssignment);
            }
        }
    }

    public List<FunctionDeclaration> getImportedFunctions() {
        return importedFunctions;
    }

    public List<Stylerule> getImportedStyleRules() {
        return importedStyleRules;
    }

    public List<VariableAssignment> getImportedVariables() {
        return importedVariables;
    }

    private Result<AST, SemanticError> getImportedSyntaxTree(File file) {
        try {
            String fileString = Files.readString(file.toPath());

            Pipeline pipeline = new Pipeline(new TransformationConfig(true, true));
            pipeline.parseString(fileString);
            pipeline.check();
            if (!pipeline.getErrors().isEmpty()) {
                String errorString = String.join(", ", pipeline.getErrors());

                return Result.err(new SemanticError("Errors occurred while trying to parse the imported file at: '" + file.toPath().toAbsolutePath() + "'. The following errors occurred: " + errorString));
            }

            pipeline.transform();

            setImportedValues(pipeline.getAST());
            return Result.of(pipeline.getAST());
        } catch (IOException e) {
            return Result.err(new SemanticError("Could not read the file at: '" + file.toPath().toAbsolutePath() + "'. The error that occurred: " + e.getMessage()));
        }
    }

    public Result<HashMap<String, FunctionDeclaration>, SemanticError> validateAndGetImportedFunctions(File file) {
        Result<AST, SemanticError> syntaxTree = getImportedSyntaxTree(file);
        if (syntaxTree.isError()) {
            return Result.err(syntaxTree.error());
        }

        HashMap<String, FunctionDeclaration> functions = new HashMap<>();
        for (FunctionDeclaration functionDeclaration : getImportedFunctions()) {
            functions.put(functionDeclaration.name, functionDeclaration);
        }

        return Result.of(functions);
    }

    public sealed interface ImportType {
        record Functions(List<ImportItem> items) implements ImportType {}
        record Styles() implements ImportType {}
        record Both() implements ImportType {}
    }

    @Override
    public String getNodeLabel() {
        return "Import";
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, importTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImportStatement that = (ImportStatement) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(importTypes, that.importTypes);
    }
}
