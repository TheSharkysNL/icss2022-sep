package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.iSwitch.rules.*;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

import java.util.List;

public class SwitchRuleVisitor extends ICSSBaseVisitor<SwitchRule> {
    @Override
    public SwitchRule visitSwitchRangeRule(ICSSParser.SwitchRangeRuleContext ctx) {
        Literal min = ctx.min == null ? null : (Literal)ctx.min
                .accept(new ExpressionLiteralVisitor());
        Literal max = ctx.max == null ? null : (Literal)ctx.max
                .accept(new ExpressionLiteralVisitor());

        return new RangeRule(min, max);
    }

    @Override
    public SwitchRule visitSwitchLiteralRule(ICSSParser.SwitchLiteralRuleContext ctx) {
        Literal value = (Literal)ctx.expressionLit()
                .accept(new ExpressionLiteralVisitor());

        return new LiteralRule(value);
    }

    @Override
    public SwitchRule visitSwitchDefaultRule(ICSSParser.SwitchDefaultRuleContext ctx) {
        return new DefaultRule();
    }

    @Override
    public SwitchRule visitSwitchTupleRule(ICSSParser.SwitchTupleRuleContext ctx) {
        List<SwitchRule> innerRules = StatementVisitor.getList(
                ctx.switchCaseRuleList(),
                ICSSParser.SwitchCaseRuleListContext::switchCaseRuleList,
                r -> r.switchCaseRule().accept(this)
        );

        return new TupleRule(innerRules);
    }

    @Override
    public SwitchRule visitSwitchVariableRule(ICSSParser.SwitchVariableRuleContext ctx) {
        VariableReference reference = new VariableReference(ctx.CAPITAL_IDENT().getText());
        return new VariableRule(reference);
    }
}
