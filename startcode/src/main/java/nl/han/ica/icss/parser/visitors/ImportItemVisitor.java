package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.iImport.ImportItem;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

public class ImportItemVisitor extends ICSSBaseVisitor<ImportItem> {
    @Override
    public ImportItem visitNamedImportItem(ICSSParser.NamedImportItemContext ctx) {
        String name = ctx.CAPITAL_IDENT().getText();
        ImportItem.ImportItemType type = new ImportItem.ImportItemType.Named(name);

        return new ImportItem(type);
    }

    @Override
    public ImportItem visitWildcardImportItem(ICSSParser.WildcardImportItemContext ctx) {
        return new ImportItem(new ImportItem.ImportItemType.WildCard());
    }
}
