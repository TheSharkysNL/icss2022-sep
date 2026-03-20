package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.iImport.ImportItem;
import nl.han.ica.icss.ast.iImport.ImportStatement;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.ArrayList;
import java.util.List;

public class ImportTypeVisitor extends ICSSBaseVisitor<ImportStatement.ImportType> {
    @Override
    public ImportStatement.ImportType visitFunctionsImportType(ICSSParser.FunctionsImportTypeContext ctx) {
        List<ImportItem> items;

        if (ctx.importItems() != null) {
            items = StatementVisitor.getList(
                    ctx.importItems().importList(),
                    ICSSParser.ImportListContext::importList,
                    item -> item.importItem().accept(new ImportItemVisitor())
            );
        } else {
            items = new ArrayList<>();
            items.add(new ImportItem(new ImportItem.ImportItemType.WildCard()));
        }

        return new ImportStatement.ImportType.Functions(items);
    }

    @Override
    public ImportStatement.ImportType visitStylesImportType(ICSSParser.StylesImportTypeContext ctx) {
        return new ImportStatement.ImportType.Styles();
    }

    @Override
    public ImportStatement.ImportType visitBothImportType(ICSSParser.BothImportTypeContext ctx) {
        return new ImportStatement.ImportType.Both();
    }
}
