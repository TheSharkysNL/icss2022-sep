package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

public class ExpressionLiteralVisitor extends ICSSBaseVisitor<Expression> {
    @Override
    public final Expression visitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        return new ColorLiteral(ctx.COLOR().getText());
    }

    @Override
    public final Expression visitFalseLiteral(ICSSParser.FalseLiteralContext ctx) {
        return new BoolLiteral(false);
    }

    @Override
    public final Expression visitTrueLiteral(ICSSParser.TrueLiteralContext ctx) {
        return new BoolLiteral(true);
    }

    @Override
    public final Expression visitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        String pixels = ctx.PIXELSIZE().getText();
        return new PixelLiteral(pixels);
    }

    @Override
    public final Expression visitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        String percentage = ctx.PERCENTAGE().getText();
        return new PixelLiteral(percentage);
    }

    @Override
    public final Expression visitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        int integerValue = Integer.parseInt(ctx.SCALAR().getText());
        return new ScalarLiteral(integerValue);
    }
}