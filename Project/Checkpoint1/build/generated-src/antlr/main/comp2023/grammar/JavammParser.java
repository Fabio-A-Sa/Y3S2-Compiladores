// Generated from comp2023/grammar/Javamm.g4 by ANTLR 4.5.3

    package pt.up.fe.comp2023;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JavammParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		INTEGER=53, ID=54, SINGLE_COMMENT=55, MULTI_COMMENT=56, WS=57;
	public static final int
		RULE_program = 0, RULE_importDeclaration = 1, RULE_classDeclaration = 2, 
		RULE_varDeclaration = 3, RULE_type = 4, RULE_argument = 5, RULE_returnStmt = 6, 
		RULE_methodDeclaration = 7, RULE_statement = 8, RULE_ifExpr = 9, RULE_elseifExpr = 10, 
		RULE_elseExpr = 11, RULE_expression = 12;
	public static final String[] ruleNames = {
		"program", "importDeclaration", "classDeclaration", "varDeclaration", 
		"type", "argument", "returnStmt", "methodDeclaration", "statement", "ifExpr", 
		"elseifExpr", "elseExpr", "expression"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'import'", "'.'", "';'", "'class'", "'extends'", "'{'", "'}'", 
		"'['", "']'", "'double'", "'float'", "'boolean'", "'int'", "'String'", 
		"','", "'return'", "'public'", "'('", "')'", "'static'", "'void'", "'main'", 
		"'for'", "'while'", "'='", "'if'", "'else if'", "'else'", "'new'", "'length'", 
		"'this'", "'!'", "'*'", "'/'", "'+'", "'-'", "'<'", "'>'", "'<='", "'>='", 
		"'=='", "'!='", "'+='", "'-='", "'*='", "'/='", "'&&'", "'||'", "'true'", 
		"'false'", "'++'", "'--'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, "INTEGER", "ID", "SINGLE_COMMENT", "MULTI_COMMENT", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Javamm.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JavammParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public TerminalNode EOF() { return getToken(JavammParser.EOF, 0); }
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(26);
				importDeclaration();
				}
				}
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(32);
			classDeclaration();
			setState(33);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclarationContext extends ParserRuleContext {
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
	 
		public ImportDeclarationContext() { }
		public void copyFrom(ImportDeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ImportStmtContext extends ImportDeclarationContext {
		public Token ID;
		public List<Token> value = new ArrayList<Token>();
		public List<TerminalNode> ID() { return getTokens(JavammParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(JavammParser.ID, i);
		}
		public ImportStmtContext(ImportDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterImportStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitImportStmt(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_importDeclaration);
		int _la;
		try {
			_localctx = new ImportStmtContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			match(T__0);
			setState(36);
			((ImportStmtContext)_localctx).ID = match(ID);
			((ImportStmtContext)_localctx).value.add(((ImportStmtContext)_localctx).ID);
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(37);
				match(T__1);
				setState(38);
				((ImportStmtContext)_localctx).ID = match(ID);
				((ImportStmtContext)_localctx).value.add(((ImportStmtContext)_localctx).ID);
				}
				}
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(44);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
	 
		public ClassDeclarationContext() { }
		public void copyFrom(ClassDeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ClassStmtContext extends ClassDeclarationContext {
		public Token className;
		public Token extendedClass;
		public List<TerminalNode> ID() { return getTokens(JavammParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(JavammParser.ID, i);
		}
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<MethodDeclarationContext> methodDeclaration() {
			return getRuleContexts(MethodDeclarationContext.class);
		}
		public MethodDeclarationContext methodDeclaration(int i) {
			return getRuleContext(MethodDeclarationContext.class,i);
		}
		public ClassStmtContext(ClassDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterClassStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitClassStmt(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDeclaration);
		int _la;
		try {
			int _alt;
			_localctx = new ClassStmtContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(T__3);
			setState(47);
			((ClassStmtContext)_localctx).className = match(ID);
			setState(50);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(48);
				match(T__4);
				setState(49);
				((ClassStmtContext)_localctx).extendedClass = match(ID);
				}
			}

			setState(52);
			match(T__5);
			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(53);
					varDeclaration();
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__16) | (1L << T__19) | (1L << ID))) != 0)) {
				{
				{
				setState(59);
				methodDeclaration();
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(65);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclarationContext extends ParserRuleContext {
		public Token name;
		public Token op;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitVarDeclaration(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_varDeclaration);
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				type(0);
				setState(68);
				((VarDeclarationContext)_localctx).name = match(ID);
				setState(69);
				match(T__2);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				type(0);
				setState(72);
				((VarDeclarationContext)_localctx).name = match(ID);
				setState(73);
				((VarDeclarationContext)_localctx).op = match(T__7);
				setState(74);
				((VarDeclarationContext)_localctx).op = match(T__8);
				setState(75);
				match(T__2);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArrayContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitArray(this);
		}
	}
	public static class FloatContext extends TypeContext {
		public Token value;
		public FloatContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterFloat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitFloat(this);
		}
	}
	public static class StringContext extends TypeContext {
		public Token value;
		public StringContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitString(this);
		}
	}
	public static class IdContext extends TypeContext {
		public Token value;
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public IdContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitId(this);
		}
	}
	public static class BooleanContext extends TypeContext {
		public Token value;
		public BooleanContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitBoolean(this);
		}
	}
	public static class DoubleContext extends TypeContext {
		public Token value;
		public DoubleContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterDouble(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitDouble(this);
		}
	}
	public static class IntContext extends TypeContext {
		public Token value;
		public IntContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitInt(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			switch (_input.LA(1)) {
			case T__9:
				{
				_localctx = new DoubleContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(80);
				((DoubleContext)_localctx).value = match(T__9);
				}
				break;
			case T__10:
				{
				_localctx = new FloatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(81);
				((FloatContext)_localctx).value = match(T__10);
				}
				break;
			case T__11:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(82);
				((BooleanContext)_localctx).value = match(T__11);
				}
				break;
			case T__12:
				{
				_localctx = new IntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(83);
				((IntContext)_localctx).value = match(T__12);
				}
				break;
			case T__13:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84);
				((StringContext)_localctx).value = match(T__13);
				}
				break;
			case ID:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(85);
				((IdContext)_localctx).value = match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(93);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArrayContext(new TypeContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(88);
					if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
					setState(89);
					match(T__7);
					setState(90);
					match(T__8);
					}
					} 
				}
				setState(95);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public Token argName;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitArgument(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_argument);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			type(0);
			setState(97);
			((ArgumentContext)_localctx).argName = match(ID);
			setState(102);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(98);
					match(T__14);
					setState(99);
					argument();
					}
					} 
				}
				setState(104);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnStmtContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterReturnStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitReturnStmt(this);
		}
	}

	public final ReturnStmtContext returnStmt() throws RecognitionException {
		ReturnStmtContext _localctx = new ReturnStmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_returnStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(T__15);
			setState(106);
			expression(0);
			setState(107);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclarationContext extends ParserRuleContext {
		public Token methodName;
		public Token argName;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ReturnStmtContext returnStmt() {
			return getRuleContext(ReturnStmtContext.class,0);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitMethodDeclaration(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_methodDeclaration);
		int _la;
		try {
			int _alt;
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(110);
				_la = _input.LA(1);
				if (_la==T__16) {
					{
					setState(109);
					match(T__16);
					}
				}

				setState(112);
				type(0);
				setState(113);
				((MethodDeclarationContext)_localctx).methodName = match(ID);
				setState(114);
				match(T__17);
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << ID))) != 0)) {
					{
					{
					setState(115);
					argument();
					}
					}
					setState(120);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(121);
				match(T__18);
				setState(122);
				match(T__5);
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(123);
						varDeclaration();
						}
						} 
					}
					setState(128);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__17) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__28) | (1L << T__30) | (1L << T__31) | (1L << T__48) | (1L << T__49) | (1L << INTEGER) | (1L << ID))) != 0)) {
					{
					{
					setState(129);
					statement();
					}
					}
					setState(134);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(135);
				returnStmt();
				setState(136);
				match(T__6);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(139);
				_la = _input.LA(1);
				if (_la==T__16) {
					{
					setState(138);
					match(T__16);
					}
				}

				setState(141);
				match(T__19);
				setState(142);
				match(T__20);
				setState(143);
				match(T__21);
				setState(144);
				match(T__17);
				setState(145);
				match(T__13);
				setState(146);
				match(T__7);
				setState(147);
				match(T__8);
				setState(148);
				((MethodDeclarationContext)_localctx).argName = match(ID);
				setState(149);
				match(T__18);
				setState(150);
				match(T__5);
				setState(154);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(151);
						varDeclaration();
						}
						} 
					}
					setState(156);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__17) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__28) | (1L << T__30) | (1L << T__31) | (1L << T__48) | (1L << T__49) | (1L << INTEGER) | (1L << ID))) != 0)) {
					{
					{
					setState(157);
					statement();
					}
					}
					setState(162);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(163);
				match(T__6);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AssignmentContext extends StatementContext {
		public Token var;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public AssignmentContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitAssignment(this);
		}
	}
	public static class ArrayAssignContext extends StatementContext {
		public Token var;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public ArrayAssignContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterArrayAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitArrayAssign(this);
		}
	}
	public static class IfStmtContext extends StatementContext {
		public IfExprContext ifExpr() {
			return getRuleContext(IfExprContext.class,0);
		}
		public List<ElseifExprContext> elseifExpr() {
			return getRuleContexts(ElseifExprContext.class);
		}
		public ElseifExprContext elseifExpr(int i) {
			return getRuleContext(ElseifExprContext.class,i);
		}
		public ElseExprContext elseExpr() {
			return getRuleContext(ElseExprContext.class,0);
		}
		public IfStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitIfStmt(this);
		}
	}
	public static class ExprStmtContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExprStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterExprStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitExprStmt(this);
		}
	}
	public static class BracketsContext extends StatementContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BracketsContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterBrackets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitBrackets(this);
		}
	}
	public static class WhileStmtContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterWhileStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitWhileStmt(this);
		}
	}
	public static class ForStmtContext extends StatementContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterForStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitForStmt(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(215);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				_localctx = new ExprStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				expression(0);
				setState(167);
				match(T__2);
				}
				break;
			case 2:
				_localctx = new BracketsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(169);
				match(T__5);
				setState(173);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__17) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__28) | (1L << T__30) | (1L << T__31) | (1L << T__48) | (1L << T__49) | (1L << INTEGER) | (1L << ID))) != 0)) {
					{
					{
					setState(170);
					statement();
					}
					}
					setState(175);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(176);
				match(T__6);
				}
				break;
			case 3:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(177);
				ifExpr();
				setState(181);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(178);
						elseifExpr();
						}
						} 
					}
					setState(183);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				setState(185);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(184);
					elseExpr();
					}
					break;
				}
				}
				break;
			case 4:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(187);
				match(T__22);
				setState(188);
				match(T__17);
				setState(189);
				statement();
				setState(190);
				expression(0);
				setState(191);
				match(T__2);
				setState(192);
				expression(0);
				setState(193);
				match(T__18);
				setState(194);
				statement();
				}
				break;
			case 5:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(196);
				match(T__23);
				setState(197);
				match(T__17);
				setState(198);
				expression(0);
				setState(199);
				match(T__18);
				setState(200);
				statement();
				}
				break;
			case 6:
				_localctx = new AssignmentContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(202);
				((AssignmentContext)_localctx).var = match(ID);
				setState(203);
				match(T__24);
				setState(204);
				expression(0);
				setState(205);
				match(T__2);
				}
				break;
			case 7:
				_localctx = new ArrayAssignContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(207);
				((ArrayAssignContext)_localctx).var = match(ID);
				setState(208);
				match(T__7);
				setState(209);
				expression(0);
				setState(210);
				match(T__8);
				setState(211);
				match(T__24);
				setState(212);
				expression(0);
				setState(213);
				match(T__2);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfExprContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public IfExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterIfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitIfExpr(this);
		}
	}

	public final IfExprContext ifExpr() throws RecognitionException {
		IfExprContext _localctx = new IfExprContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_ifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			match(T__25);
			setState(218);
			match(T__17);
			setState(219);
			expression(0);
			setState(220);
			match(T__18);
			setState(221);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseifExprContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ElseifExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseifExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterElseifExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitElseifExpr(this);
		}
	}

	public final ElseifExprContext elseifExpr() throws RecognitionException {
		ElseifExprContext _localctx = new ElseifExprContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_elseifExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			match(T__26);
			setState(224);
			match(T__17);
			setState(225);
			expression(0);
			setState(226);
			match(T__18);
			setState(227);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseExprContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ElseExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterElseExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitElseExpr(this);
		}
	}

	public final ElseExprContext elseExpr() throws RecognitionException {
		ElseExprContext _localctx = new ElseExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_elseExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(T__27);
			setState(230);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayDeclarationContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayDeclarationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterArrayDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitArrayDeclaration(this);
		}
	}
	public static class NegationContext extends ExpressionContext {
		public Token value;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NegationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitNegation(this);
		}
	}
	public static class ParentesisContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParentesisContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterParentesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitParentesis(this);
		}
	}
	public static class IntegerContext extends ExpressionContext {
		public Token value;
		public TerminalNode INTEGER() { return getToken(JavammParser.INTEGER, 0); }
		public IntegerContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitInteger(this);
		}
	}
	public static class IdentifierContext extends ExpressionContext {
		public Token value;
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public IdentifierContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitIdentifier(this);
		}
	}
	public static class ClassInstantiationContext extends ExpressionContext {
		public Token className;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public ClassInstantiationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterClassInstantiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitClassInstantiation(this);
		}
	}
	public static class LengthContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LengthContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterLength(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitLength(this);
		}
	}
	public static class ArraySubscriptContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArraySubscriptContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterArraySubscript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitArraySubscript(this);
		}
	}
	public static class IncrementContext extends ExpressionContext {
		public Token value;
		public Token op;
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public IncrementContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterIncrement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitIncrement(this);
		}
	}
	public static class ObjectContext extends ExpressionContext {
		public Token value;
		public ObjectContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitObject(this);
		}
	}
	public static class FunctionCallContext extends ExpressionContext {
		public Token value;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public FunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitFunctionCall(this);
		}
	}
	public static class NewClassContext extends ExpressionContext {
		public Token classname;
		public TerminalNode ID() { return getToken(JavammParser.ID, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public NewClassContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterNewClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitNewClass(this);
		}
	}
	public static class BinaryOpContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BinaryOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).enterBinaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavammListener ) ((JavammListener)listener).exitBinaryOp(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				_localctx = new ParentesisContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(233);
				match(T__17);
				setState(234);
				expression(0);
				setState(235);
				match(T__18);
				}
				break;
			case 2:
				{
				_localctx = new ArrayDeclarationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(237);
				match(T__28);
				setState(238);
				match(T__12);
				setState(239);
				match(T__7);
				setState(240);
				expression(0);
				setState(241);
				match(T__8);
				}
				break;
			case 3:
				{
				_localctx = new NewClassContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(243);
				match(T__28);
				setState(244);
				((NewClassContext)_localctx).classname = match(ID);
				setState(245);
				match(T__17);
				setState(254);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__28) | (1L << T__30) | (1L << T__31) | (1L << T__48) | (1L << T__49) | (1L << INTEGER) | (1L << ID))) != 0)) {
					{
					setState(246);
					expression(0);
					setState(251);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__14) {
						{
						{
						setState(247);
						match(T__14);
						setState(248);
						expression(0);
						}
						}
						setState(253);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(256);
				match(T__18);
				}
				break;
			case 4:
				{
				_localctx = new ClassInstantiationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(257);
				((ClassInstantiationContext)_localctx).className = match(ID);
				setState(258);
				expression(16);
				}
				break;
			case 5:
				{
				_localctx = new ObjectContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(259);
				((ObjectContext)_localctx).value = match(T__30);
				}
				break;
			case 6:
				{
				_localctx = new NegationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(260);
				((NegationContext)_localctx).value = match(T__31);
				setState(261);
				expression(12);
				}
				break;
			case 7:
				{
				_localctx = new IntegerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(262);
				((IntegerContext)_localctx).value = match(INTEGER);
				}
				break;
			case 8:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(263);
				((IdentifierContext)_localctx).value = match(T__48);
				}
				break;
			case 9:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				((IdentifierContext)_localctx).value = match(T__49);
				}
				break;
			case 10:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(265);
				((IdentifierContext)_localctx).value = match(ID);
				}
				break;
			case 11:
				{
				_localctx = new IncrementContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(266);
				((IncrementContext)_localctx).value = match(ID);
				setState(267);
				((IncrementContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__50 || _la==T__51) ) {
					((IncrementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(313);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(311);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(270);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(271);
						((BinaryOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__32 || _la==T__33) ) {
							((BinaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(272);
						expression(12);
						}
						break;
					case 2:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(273);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(274);
						((BinaryOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__34 || _la==T__35) ) {
							((BinaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(275);
						expression(11);
						}
						break;
					case 3:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(276);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(277);
						((BinaryOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__36 || _la==T__37) ) {
							((BinaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(278);
						expression(10);
						}
						break;
					case 4:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(279);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(280);
						((BinaryOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45))) != 0)) ) {
							((BinaryOpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(281);
						expression(9);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(282);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(283);
						((BinaryOpContext)_localctx).op = match(T__46);
						setState(284);
						expression(8);
						}
						break;
					case 6:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(285);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(286);
						((BinaryOpContext)_localctx).op = match(T__47);
						setState(287);
						expression(7);
						}
						break;
					case 7:
						{
						_localctx = new ArraySubscriptContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(288);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(289);
						match(T__7);
						setState(290);
						expression(0);
						setState(291);
						match(T__8);
						}
						break;
					case 8:
						{
						_localctx = new FunctionCallContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(293);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(294);
						match(T__1);
						setState(295);
						((FunctionCallContext)_localctx).value = match(ID);
						setState(296);
						match(T__17);
						setState(305);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__28) | (1L << T__30) | (1L << T__31) | (1L << T__48) | (1L << T__49) | (1L << INTEGER) | (1L << ID))) != 0)) {
							{
							setState(297);
							expression(0);
							setState(302);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==T__14) {
								{
								{
								setState(298);
								match(T__14);
								setState(299);
								expression(0);
								}
								}
								setState(304);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(307);
						match(T__18);
						}
						break;
					case 9:
						{
						_localctx = new LengthContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(308);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(309);
						match(T__1);
						setState(310);
						match(T__29);
						}
						break;
					}
					} 
				}
				setState(315);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4:
			return type_sempred((TypeContext)_localctx, predIndex);
		case 12:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 17);
		case 8:
			return precpred(_ctx, 15);
		case 9:
			return precpred(_ctx, 14);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3;\u013f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\7\2\36\n\2\f\2\16\2!\13\2\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\7\3*\n\3\f\3\16\3-\13\3\3\3\3\3\3\4\3\4\3\4\3\4\5\4"+
		"\65\n\4\3\4\3\4\7\49\n\4\f\4\16\4<\13\4\3\4\7\4?\n\4\f\4\16\4B\13\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5P\n\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\5\6Y\n\6\3\6\3\6\3\6\7\6^\n\6\f\6\16\6a\13\6\3\7\3\7\3"+
		"\7\3\7\7\7g\n\7\f\7\16\7j\13\7\3\b\3\b\3\b\3\b\3\t\5\tq\n\t\3\t\3\t\3"+
		"\t\3\t\7\tw\n\t\f\t\16\tz\13\t\3\t\3\t\3\t\7\t\177\n\t\f\t\16\t\u0082"+
		"\13\t\3\t\7\t\u0085\n\t\f\t\16\t\u0088\13\t\3\t\3\t\3\t\3\t\5\t\u008e"+
		"\n\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u009b\n\t\f\t\16"+
		"\t\u009e\13\t\3\t\7\t\u00a1\n\t\f\t\16\t\u00a4\13\t\3\t\5\t\u00a7\n\t"+
		"\3\n\3\n\3\n\3\n\3\n\7\n\u00ae\n\n\f\n\16\n\u00b1\13\n\3\n\3\n\3\n\7\n"+
		"\u00b6\n\n\f\n\16\n\u00b9\13\n\3\n\5\n\u00bc\n\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\5\n\u00da\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u00fc\n\16\f\16\16"+
		"\16\u00ff\13\16\5\16\u0101\n\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\5\16\u010f\n\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u012f\n\16\f\16\16"+
		"\16\u0132\13\16\5\16\u0134\n\16\3\16\3\16\3\16\3\16\7\16\u013a\n\16\f"+
		"\16\16\16\u013d\13\16\3\16\2\4\n\32\17\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\2\7\3\2\65\66\3\2#$\3\2%&\3\2\'(\3\2)\60\u0166\2\37\3\2\2\2\4%\3\2\2"+
		"\2\6\60\3\2\2\2\bO\3\2\2\2\nX\3\2\2\2\fb\3\2\2\2\16k\3\2\2\2\20\u00a6"+
		"\3\2\2\2\22\u00d9\3\2\2\2\24\u00db\3\2\2\2\26\u00e1\3\2\2\2\30\u00e7\3"+
		"\2\2\2\32\u010e\3\2\2\2\34\36\5\4\3\2\35\34\3\2\2\2\36!\3\2\2\2\37\35"+
		"\3\2\2\2\37 \3\2\2\2 \"\3\2\2\2!\37\3\2\2\2\"#\5\6\4\2#$\7\2\2\3$\3\3"+
		"\2\2\2%&\7\3\2\2&+\78\2\2\'(\7\4\2\2(*\78\2\2)\'\3\2\2\2*-\3\2\2\2+)\3"+
		"\2\2\2+,\3\2\2\2,.\3\2\2\2-+\3\2\2\2./\7\5\2\2/\5\3\2\2\2\60\61\7\6\2"+
		"\2\61\64\78\2\2\62\63\7\7\2\2\63\65\78\2\2\64\62\3\2\2\2\64\65\3\2\2\2"+
		"\65\66\3\2\2\2\66:\7\b\2\2\679\5\b\5\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2"+
		":;\3\2\2\2;@\3\2\2\2<:\3\2\2\2=?\5\20\t\2>=\3\2\2\2?B\3\2\2\2@>\3\2\2"+
		"\2@A\3\2\2\2AC\3\2\2\2B@\3\2\2\2CD\7\t\2\2D\7\3\2\2\2EF\5\n\6\2FG\78\2"+
		"\2GH\7\5\2\2HP\3\2\2\2IJ\5\n\6\2JK\78\2\2KL\7\n\2\2LM\7\13\2\2MN\7\5\2"+
		"\2NP\3\2\2\2OE\3\2\2\2OI\3\2\2\2P\t\3\2\2\2QR\b\6\1\2RY\7\f\2\2SY\7\r"+
		"\2\2TY\7\16\2\2UY\7\17\2\2VY\7\20\2\2WY\78\2\2XQ\3\2\2\2XS\3\2\2\2XT\3"+
		"\2\2\2XU\3\2\2\2XV\3\2\2\2XW\3\2\2\2Y_\3\2\2\2Z[\f\t\2\2[\\\7\n\2\2\\"+
		"^\7\13\2\2]Z\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`\13\3\2\2\2a_\3\2\2"+
		"\2bc\5\n\6\2ch\78\2\2de\7\21\2\2eg\5\f\7\2fd\3\2\2\2gj\3\2\2\2hf\3\2\2"+
		"\2hi\3\2\2\2i\r\3\2\2\2jh\3\2\2\2kl\7\22\2\2lm\5\32\16\2mn\7\5\2\2n\17"+
		"\3\2\2\2oq\7\23\2\2po\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\5\n\6\2st\78\2\2t"+
		"x\7\24\2\2uw\5\f\7\2vu\3\2\2\2wz\3\2\2\2xv\3\2\2\2xy\3\2\2\2y{\3\2\2\2"+
		"zx\3\2\2\2{|\7\25\2\2|\u0080\7\b\2\2}\177\5\b\5\2~}\3\2\2\2\177\u0082"+
		"\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0086\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0083\u0085\5\22\n\2\u0084\u0083\3\2\2\2\u0085\u0088\3"+
		"\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0089\u008a\5\16\b\2\u008a\u008b\7\t\2\2\u008b\u00a7\3"+
		"\2\2\2\u008c\u008e\7\23\2\2\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u0090\7\26\2\2\u0090\u0091\7\27\2\2\u0091\u0092\7"+
		"\30\2\2\u0092\u0093\7\24\2\2\u0093\u0094\7\20\2\2\u0094\u0095\7\n\2\2"+
		"\u0095\u0096\7\13\2\2\u0096\u0097\78\2\2\u0097\u0098\7\25\2\2\u0098\u009c"+
		"\7\b\2\2\u0099\u009b\5\b\5\2\u009a\u0099\3\2\2\2\u009b\u009e\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u00a2\3\2\2\2\u009e\u009c\3\2"+
		"\2\2\u009f\u00a1\5\22\n\2\u00a0\u009f\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u00a2\3\2"+
		"\2\2\u00a5\u00a7\7\t\2\2\u00a6p\3\2\2\2\u00a6\u008d\3\2\2\2\u00a7\21\3"+
		"\2\2\2\u00a8\u00a9\5\32\16\2\u00a9\u00aa\7\5\2\2\u00aa\u00da\3\2\2\2\u00ab"+
		"\u00af\7\b\2\2\u00ac\u00ae\5\22\n\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3"+
		"\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b2\u00da\7\t\2\2\u00b3\u00b7\5\24\13\2\u00b4\u00b6\5"+
		"\26\f\2\u00b5\u00b4\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7"+
		"\u00b8\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba\u00bc\5\30"+
		"\r\2\u00bb\u00ba\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00da\3\2\2\2\u00bd"+
		"\u00be\7\31\2\2\u00be\u00bf\7\24\2\2\u00bf\u00c0\5\22\n\2\u00c0\u00c1"+
		"\5\32\16\2\u00c1\u00c2\7\5\2\2\u00c2\u00c3\5\32\16\2\u00c3\u00c4\7\25"+
		"\2\2\u00c4\u00c5\5\22\n\2\u00c5\u00da\3\2\2\2\u00c6\u00c7\7\32\2\2\u00c7"+
		"\u00c8\7\24\2\2\u00c8\u00c9\5\32\16\2\u00c9\u00ca\7\25\2\2\u00ca\u00cb"+
		"\5\22\n\2\u00cb\u00da\3\2\2\2\u00cc\u00cd\78\2\2\u00cd\u00ce\7\33\2\2"+
		"\u00ce\u00cf\5\32\16\2\u00cf\u00d0\7\5\2\2\u00d0\u00da\3\2\2\2\u00d1\u00d2"+
		"\78\2\2\u00d2\u00d3\7\n\2\2\u00d3\u00d4\5\32\16\2\u00d4\u00d5\7\13\2\2"+
		"\u00d5\u00d6\7\33\2\2\u00d6\u00d7\5\32\16\2\u00d7\u00d8\7\5\2\2\u00d8"+
		"\u00da\3\2\2\2\u00d9\u00a8\3\2\2\2\u00d9\u00ab\3\2\2\2\u00d9\u00b3\3\2"+
		"\2\2\u00d9\u00bd\3\2\2\2\u00d9\u00c6\3\2\2\2\u00d9\u00cc\3\2\2\2\u00d9"+
		"\u00d1\3\2\2\2\u00da\23\3\2\2\2\u00db\u00dc\7\34\2\2\u00dc\u00dd\7\24"+
		"\2\2\u00dd\u00de\5\32\16\2\u00de\u00df\7\25\2\2\u00df\u00e0\5\22\n\2\u00e0"+
		"\25\3\2\2\2\u00e1\u00e2\7\35\2\2\u00e2\u00e3\7\24\2\2\u00e3\u00e4\5\32"+
		"\16\2\u00e4\u00e5\7\25\2\2\u00e5\u00e6\5\22\n\2\u00e6\27\3\2\2\2\u00e7"+
		"\u00e8\7\36\2\2\u00e8\u00e9\5\22\n\2\u00e9\31\3\2\2\2\u00ea\u00eb\b\16"+
		"\1\2\u00eb\u00ec\7\24\2\2\u00ec\u00ed\5\32\16\2\u00ed\u00ee\7\25\2\2\u00ee"+
		"\u010f\3\2\2\2\u00ef\u00f0\7\37\2\2\u00f0\u00f1\7\17\2\2\u00f1\u00f2\7"+
		"\n\2\2\u00f2\u00f3\5\32\16\2\u00f3\u00f4\7\13\2\2\u00f4\u010f\3\2\2\2"+
		"\u00f5\u00f6\7\37\2\2\u00f6\u00f7\78\2\2\u00f7\u0100\7\24\2\2\u00f8\u00fd"+
		"\5\32\16\2\u00f9\u00fa\7\21\2\2\u00fa\u00fc\5\32\16\2\u00fb\u00f9\3\2"+
		"\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u00f8\3\2\2\2\u0100\u0101\3\2"+
		"\2\2\u0101\u0102\3\2\2\2\u0102\u010f\7\25\2\2\u0103\u0104\78\2\2\u0104"+
		"\u010f\5\32\16\22\u0105\u010f\7!\2\2\u0106\u0107\7\"\2\2\u0107\u010f\5"+
		"\32\16\16\u0108\u010f\7\67\2\2\u0109\u010f\7\63\2\2\u010a\u010f\7\64\2"+
		"\2\u010b\u010f\78\2\2\u010c\u010d\78\2\2\u010d\u010f\t\2\2\2\u010e\u00ea"+
		"\3\2\2\2\u010e\u00ef\3\2\2\2\u010e\u00f5\3\2\2\2\u010e\u0103\3\2\2\2\u010e"+
		"\u0105\3\2\2\2\u010e\u0106\3\2\2\2\u010e\u0108\3\2\2\2\u010e\u0109\3\2"+
		"\2\2\u010e\u010a\3\2\2\2\u010e\u010b\3\2\2\2\u010e\u010c\3\2\2\2\u010f"+
		"\u013b\3\2\2\2\u0110\u0111\f\r\2\2\u0111\u0112\t\3\2\2\u0112\u013a\5\32"+
		"\16\16\u0113\u0114\f\f\2\2\u0114\u0115\t\4\2\2\u0115\u013a\5\32\16\r\u0116"+
		"\u0117\f\13\2\2\u0117\u0118\t\5\2\2\u0118\u013a\5\32\16\f\u0119\u011a"+
		"\f\n\2\2\u011a\u011b\t\6\2\2\u011b\u013a\5\32\16\13\u011c\u011d\f\t\2"+
		"\2\u011d\u011e\7\61\2\2\u011e\u013a\5\32\16\n\u011f\u0120\f\b\2\2\u0120"+
		"\u0121\7\62\2\2\u0121\u013a\5\32\16\t\u0122\u0123\f\23\2\2\u0123\u0124"+
		"\7\n\2\2\u0124\u0125\5\32\16\2\u0125\u0126\7\13\2\2\u0126\u013a\3\2\2"+
		"\2\u0127\u0128\f\21\2\2\u0128\u0129\7\4\2\2\u0129\u012a\78\2\2\u012a\u0133"+
		"\7\24\2\2\u012b\u0130\5\32\16\2\u012c\u012d\7\21\2\2\u012d\u012f\5\32"+
		"\16\2\u012e\u012c\3\2\2\2\u012f\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130"+
		"\u0131\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2\2\u0133\u012b\3\2"+
		"\2\2\u0133\u0134\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u013a\7\25\2\2\u0136"+
		"\u0137\f\20\2\2\u0137\u0138\7\4\2\2\u0138\u013a\7 \2\2\u0139\u0110\3\2"+
		"\2\2\u0139\u0113\3\2\2\2\u0139\u0116\3\2\2\2\u0139\u0119\3\2\2\2\u0139"+
		"\u011c\3\2\2\2\u0139\u011f\3\2\2\2\u0139\u0122\3\2\2\2\u0139\u0127\3\2"+
		"\2\2\u0139\u0136\3\2\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3\2\2\2\u013b"+
		"\u013c\3\2\2\2\u013c\33\3\2\2\2\u013d\u013b\3\2\2\2\36\37+\64:@OX_hpx"+
		"\u0080\u0086\u008d\u009c\u00a2\u00a6\u00af\u00b7\u00bb\u00d9\u00fd\u0100"+
		"\u010e\u0130\u0133\u0139\u013b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}