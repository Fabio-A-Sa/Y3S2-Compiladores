// Generated from comp2023/grammar/Javamm.g4 by ANTLR 4.5.3

    package pt.up.fe.comp2023;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavammParser}.
 */
public interface JavammListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JavammParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(JavammParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(JavammParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImportStmt}
	 * labeled alternative in {@link JavammParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportStmt(JavammParser.ImportStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImportStmt}
	 * labeled alternative in {@link JavammParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportStmt(JavammParser.ImportStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClassStmt}
	 * labeled alternative in {@link JavammParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassStmt(JavammParser.ClassStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClassStmt}
	 * labeled alternative in {@link JavammParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassStmt(JavammParser.ClassStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(JavammParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(JavammParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Array}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterArray(JavammParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Array}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitArray(JavammParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Float}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterFloat(JavammParser.FloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Float}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitFloat(JavammParser.FloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterString(JavammParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitString(JavammParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Id}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterId(JavammParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitId(JavammParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(JavammParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(JavammParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Void}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterVoid(JavammParser.VoidContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Void}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitVoid(JavammParser.VoidContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Double}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDouble(JavammParser.DoubleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Double}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDouble(JavammParser.DoubleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Int}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterInt(JavammParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitInt(JavammParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(JavammParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(JavammParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(JavammParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(JavammParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(JavammParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(JavammParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(JavammParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(JavammParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(JavammParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(JavammParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Brackets}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBrackets(JavammParser.BracketsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Brackets}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBrackets(JavammParser.BracketsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(JavammParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(JavammParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(JavammParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(JavammParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(JavammParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(JavammParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(JavammParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(JavammParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#ifExpr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpr(JavammParser.IfExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#ifExpr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpr(JavammParser.IfExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#elseifExpr}.
	 * @param ctx the parse tree
	 */
	void enterElseifExpr(JavammParser.ElseifExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#elseifExpr}.
	 * @param ctx the parse tree
	 */
	void exitElseifExpr(JavammParser.ElseifExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#elseExpr}.
	 * @param ctx the parse tree
	 */
	void enterElseExpr(JavammParser.ElseExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#elseExpr}.
	 * @param ctx the parse tree
	 */
	void exitElseExpr(JavammParser.ElseExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(JavammParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(JavammParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInteger(JavammParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInteger(JavammParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayDeclaration}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayDeclaration(JavammParser.ArrayDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayDeclaration}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayDeclaration(JavammParser.ArrayDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(JavammParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(JavammParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(JavammParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(JavammParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegation(JavammParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegation(JavammParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Length}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLength(JavammParser.LengthContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Length}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLength(JavammParser.LengthContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParentesis(JavammParser.ParentesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParentesis(JavammParser.ParentesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Increment}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIncrement(JavammParser.IncrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Increment}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIncrement(JavammParser.IncrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Object}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterObject(JavammParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Object}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitObject(JavammParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(JavammParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(JavammParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewClass}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewClass(JavammParser.NewClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewClass}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewClass(JavammParser.NewClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryOp}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOp(JavammParser.BinaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryOp}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOp(JavammParser.BinaryOpContext ctx);
}