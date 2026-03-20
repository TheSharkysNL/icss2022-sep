package nl.han.ica.icss.ast.iImport;

import nl.han.ica.icss.ast.ASTNode;

import java.util.Objects;

public class ImportItem extends ASTNode {
    public final ImportItemType type;

    public ImportItem(ImportItemType type) {
        this.type = type;
    }

    public sealed interface ImportItemType {
        record Named(String name) implements ImportItemType {}
        record WildCard() implements ImportItemType {}
    }

    @Override
    public String getNodeLabel() {
        return "ImportItem";
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImportItem that = (ImportItem) o;
        return Objects.equals(type, that.type);
    }
}
