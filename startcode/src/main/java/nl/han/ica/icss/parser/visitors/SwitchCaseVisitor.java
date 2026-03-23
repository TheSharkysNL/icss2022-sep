package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.iSwitch.SwitchCase;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.List;

public class SwitchCaseVisitor extends ICSSBaseVisitor<SwitchCase> {

    @Override
    public SwitchCase visitSingleStatementSwitchCase(ICSSParser.SingleStatementSwitchCaseContext ctx) {
        ASTNode statement = ctx.statement().accept(new StatementVisitor());

        return new SwitchCase(List.of(statement));
    }

    @Override
    public SwitchCase visitStatementSwitchCase(ICSSParser.StatementSwitchCaseContext ctx) {
        List<ASTNode> body = StatementVisitor.getStatements(ctx.statement());
        return new SwitchCase(body);
    }
}
