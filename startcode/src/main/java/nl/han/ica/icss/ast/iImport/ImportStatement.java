package nl.han.ica.icss.ast.iImport;

import nl.han.ica.icss.Pipeline;
import nl.han.ica.icss.Result;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;
import nl.han.ica.icss.ast.function.FunctionDeclaration;
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

    private AST importedSyntaxTree = null;

    public ImportStatement(String location, List<ImportType> importTypes) {
        this.location = location;
        this.importTypes = importTypes;
    }

    public Result<AST, SemanticError> getImportedSyntaxTree() {
        if (importedSyntaxTree != null) {
            return new Result.Success<>(importedSyntaxTree);
        }

        return getImportedSyntaxTree(new File(location));
    }

    private AST getOnlyNeededImports(AST ast) {
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

        ArrayList<ASTNode> newNodes = new ArrayList<>();
        for (ASTNode node : ast.root.body) {
            if (node instanceof FunctionDeclaration functionDeclaration && (useWildcardForFunctions || neededFunctions.contains(functionDeclaration.name))) {
                newNodes.add(node);
            } else if (node instanceof Stylerule stylerule && includeStyles) {
                newNodes.add(stylerule);
            }
        }

        return new AST(new Stylesheet(newNodes));
    }

    private Result<AST, SemanticError> getImportedSyntaxTree(File file) {
        if (importedSyntaxTree != null) { // reuse syntax tree here as it should never change.
            return new Result.Success<>(importedSyntaxTree);
        }

        try {
            String fileString = Files.readString(file.toPath());

            Pipeline pipeline = new Pipeline();
            pipeline.parseString(fileString);

            importedSyntaxTree = getOnlyNeededImports(pipeline.getAST());
            return new Result.Success<>(importedSyntaxTree);
        } catch (IOException e) {
            return new Result.Error<>(new SemanticError("Could not read the file at: '" + file.toPath().toAbsolutePath() + "'. The error that occurred: " + e.getMessage()));
        }
    }

    public Result<HashMap<String, FunctionDeclaration>, SemanticError> validateAndGetImportedFunctions(File file, Checker checker) {
        HashMap<String, FunctionDeclaration> importedFunctions = new HashMap<>();

        for (ImportType importType : importTypes) {
            if (importType instanceof ImportType.Functions(List<ImportItem> items)) {
                Result<AST, SemanticError> syntaxTree = getImportedSyntaxTree(file);
                if (syntaxTree.isError()) {
                    return new Result.Error<>(syntaxTree.error());
                }

                boolean hasWildCard = items
                        .stream()
                        .anyMatch(f -> f.type instanceof ImportItem.ImportItemType.WildCard);

                Set<String> neededFunctions = items
                        .stream()
                        .filter(f -> f.type instanceof ImportItem.ImportItemType.Named)
                        .map(f -> ((ImportItem.ImportItemType.Named)f.type).name())
                        .collect(Collectors.toSet());

                checker.check(syntaxTree.value());
                List<SemanticError> errors = syntaxTree.value().getErrors();
                if (!errors.isEmpty()) {
                    return new Result.Error<>(new SemanticError("The semantics of the imported file at: '" + file.toPath().toAbsolutePath() + "' is invalid."));
                }

                for (ASTNode node : syntaxTree.value().root.body) {
                    if (node instanceof FunctionDeclaration functionDeclaration) {
                        boolean needsImport = neededFunctions.remove(functionDeclaration.name);
                        if (hasWildCard || needsImport) {
                            importedFunctions.put(functionDeclaration.name, functionDeclaration);
                        }
                    }
                }

                if (!neededFunctions.isEmpty()) {
                    return new Result.Error<>(new SemanticError("Some functions where not found in import from file: '" + file.toPath().toAbsolutePath() + "'. The following functions were not found: " + String.join(", ", neededFunctions) + "."));
                }
            }
        }

        return new Result.Success<>(importedFunctions);
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
