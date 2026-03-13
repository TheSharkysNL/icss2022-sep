// Generated from /Users/mcyuillian/Documents/GitHub/icss2022-sep/startcode/src/main/antlr4/nl/han/ica/icss/parser/ICSS.g4 by ANTLR 4.13.2
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ICSSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ICSSVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code tagSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagSelector(ICSSParser.TagSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdSelector(ICSSParser.IdSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassSelector(ICSSParser.ClassSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#rule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule(ICSSParser.RuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(ICSSParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(ICSSParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#style}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyle(ICSSParser.StyleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code colorLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColorLiteral(ICSSParser.ColorLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pixelLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPixelLiteral(ICSSParser.PixelLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code percentageLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scalarLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalarLiteral(ICSSParser.ScalarLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trueLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(ICSSParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code falseLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(ICSSParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableIdent}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableIdent(ICSSParser.VariableIdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionLiteral}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionLiteral(ICSSParser.ExpressionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code braceExpression}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBraceExpression(ICSSParser.BraceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(ICSSParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(ICSSParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#variableAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableAssignment(ICSSParser.VariableAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ICSSParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylesheet(ICSSParser.StylesheetContext ctx);
}