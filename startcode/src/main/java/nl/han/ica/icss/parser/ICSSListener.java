// Generated from /Users/mcyuillian/Documents/GitHub/icss2022-sep/startcode/src/main/antlr4/nl/han/ica/icss/parser/ICSS.g4 by ANTLR 4.13.2
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code tagSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterTagSelector(ICSSParser.TagSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tagSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitTagSelector(ICSSParser.TagSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterIdSelector(ICSSParser.IdSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitIdSelector(ICSSParser.IdSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterClassSelector(ICSSParser.ClassSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitClassSelector(ICSSParser.ClassSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#rule}.
	 * @param ctx the parse tree
	 */
	void enterRule(ICSSParser.RuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#rule}.
	 * @param ctx the parse tree
	 */
	void exitRule(ICSSParser.RuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(ICSSParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(ICSSParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(ICSSParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(ICSSParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#style}.
	 * @param ctx the parse tree
	 */
	void enterStyle(ICSSParser.StyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#style}.
	 * @param ctx the parse tree
	 */
	void exitStyle(ICSSParser.StyleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code colorLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterColorLiteral(ICSSParser.ColorLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code colorLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitColorLiteral(ICSSParser.ColorLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pixelLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pixelLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code percentageLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code percentageLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scalarLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scalarLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trueLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterTrueLiteral(ICSSParser.TrueLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trueLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitTrueLiteral(ICSSParser.TrueLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code falseLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void enterFalseLiteral(ICSSParser.FalseLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code falseLiteral}
	 * labeled alternative in {@link ICSSParser#expressionLit}.
	 * @param ctx the parse tree
	 */
	void exitFalseLiteral(ICSSParser.FalseLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableIdent}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterVariableIdent(ICSSParser.VariableIdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableIdent}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitVariableIdent(ICSSParser.VariableIdentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionLiteral}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionLiteral(ICSSParser.ExpressionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionLiteral}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionLiteral(ICSSParser.ExpressionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code braceExpression}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterBraceExpression(ICSSParser.BraceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code braceExpression}
	 * labeled alternative in {@link ICSSParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitBraceExpression(ICSSParser.BraceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(ICSSParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(ICSSParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(ICSSParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(ICSSParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ICSSParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ICSSParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
}