package nl.han.ica.icss.parser.visitors;

import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.parser.ICSSBaseVisitor;
import nl.han.ica.icss.parser.ICSSParser;

public class ExpressionTypeVisitor extends ICSSBaseVisitor<ExpressionType> {
    @Override
    public ExpressionType visitScalarType(ICSSParser.ScalarTypeContext ctx) {
        return ExpressionType.scalar();
    }

    @Override
    public ExpressionType visitBoolType(ICSSParser.BoolTypeContext ctx) {
        return ExpressionType.bool();
    }

    @Override
    public ExpressionType visitPixelType(ICSSParser.PixelTypeContext ctx) {
        return ExpressionType.pixel();
    }

    @Override
    public ExpressionType visitColorType(ICSSParser.ColorTypeContext ctx) {
        return ExpressionType.color();
    }

    @Override
    public ExpressionType visitPercentageType(ICSSParser.PercentageTypeContext ctx) {
        return ExpressionType.percentage();
    }

    @Override
    public ExpressionType visitUndefinedType(ICSSParser.UndefinedTypeContext ctx) {
        return ExpressionType.undefined();
    }
}
