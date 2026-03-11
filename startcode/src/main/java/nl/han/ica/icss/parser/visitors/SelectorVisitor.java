package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Selector;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

public class SelectorVisitor extends ICSSBaseVisitor<Selector> {
    @Override
    public Selector visitIdSelector(ICSSParser.IdSelectorContext selector) {
        String id = selector.ID_IDENT().getText().substring(1); // remove '#'
        return new IdSelector(id);
    }

    @Override
    public Selector visitTagSelector(ICSSParser.TagSelectorContext selector) {
        String identifier = selector.LOWER_IDENT().getText();
        return new TagSelector(identifier);
    }

    @Override
    public Selector visitClassSelector(ICSSParser.ClassSelectorContext selector) {
        String cls = selector.CLASS_IDENT().getText().substring(1); // remove '.'
        return new ClassSelector(cls);
    }
}
