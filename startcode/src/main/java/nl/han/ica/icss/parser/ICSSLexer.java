// Generated from /Users/mcyuillian/Documents/GitHub/icss2022-sep/startcode/src/main/antlr4/nl/han/ica/icss/parser/ICSS.g4 by ANTLR 4.13.2
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ICSSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, ELSE=2, BOX_BRACKET_OPEN=3, BOX_BRACKET_CLOSE=4, FN=5, RETURN=6, 
		COMMA=7, TRUE=8, FALSE=9, PIXELSIZE=10, PERCENTAGE=11, SCALAR=12, COLOR=13, 
		ID_IDENT=14, CLASS_IDENT=15, LOWER_IDENT=16, CAPITAL_IDENT=17, WS=18, 
		OPEN_BRACE=19, CLOSE_BRACE=20, SEMICOLON=21, COLON=22, PLUS=23, MIN=24, 
		MUL=25, ASSIGNMENT_OPERATOR=26;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "ELSE", "BOX_BRACKET_OPEN", "BOX_BRACKET_CLOSE", "FN", "RETURN", 
			"COMMA", "TRUE", "FALSE", "PIXELSIZE", "PERCENTAGE", "SCALAR", "COLOR", 
			"ID_IDENT", "CLASS_IDENT", "LOWER_IDENT", "CAPITAL_IDENT", "WS", "OPEN_BRACE", 
			"CLOSE_BRACE", "SEMICOLON", "COLON", "PLUS", "MIN", "MUL", "ASSIGNMENT_OPERATOR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'else'", "'['", "']'", "'fn'", "'return'", "','", "'TRUE'", 
			"'FALSE'", null, null, null, null, null, null, null, null, null, "'{'", 
			"'}'", "';'", "':'", "'+'", "'-'", "'*'", "':='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "ELSE", "BOX_BRACKET_OPEN", "BOX_BRACKET_CLOSE", "FN", "RETURN", 
			"COMMA", "TRUE", "FALSE", "PIXELSIZE", "PERCENTAGE", "SCALAR", "COLOR", 
			"ID_IDENT", "CLASS_IDENT", "LOWER_IDENT", "CAPITAL_IDENT", "WS", "OPEN_BRACE", 
			"CLOSE_BRACE", "SEMICOLON", "COLON", "PLUS", "MIN", "MUL", "ASSIGNMENT_OPERATOR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public ICSSLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ICSS.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u001a\u00a6\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0004\tZ\b\t\u000b\t\f\t[\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0004\nb\b\n\u000b\n\f\nc\u0001\n\u0001\n\u0001\u000b\u0004\u000bi"+
		"\b\u000b\u000b\u000b\f\u000bj\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0004\rw\b\r\u000b\r\f\rx\u0001"+
		"\u000e\u0001\u000e\u0004\u000e}\b\u000e\u000b\u000e\f\u000e~\u0001\u000f"+
		"\u0001\u000f\u0005\u000f\u0083\b\u000f\n\u000f\f\u000f\u0086\t\u000f\u0001"+
		"\u0010\u0001\u0010\u0005\u0010\u008a\b\u0010\n\u0010\f\u0010\u008d\t\u0010"+
		"\u0001\u0011\u0004\u0011\u0090\b\u0011\u000b\u0011\f\u0011\u0091\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0000\u0000\u001a\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017"+
		"\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'"+
		"\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a\u0001\u0000\u0007\u0001"+
		"\u000009\u0002\u000009af\u0003\u0000--09az\u0001\u0000az\u0001\u0000A"+
		"Z\u0004\u000009AZ__az\u0003\u0000\t\n\r\r  \u00ad\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000"+
		"#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001"+
		"\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000"+
		"\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u0000"+
		"1\u0001\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00015\u0001"+
		"\u0000\u0000\u0000\u00038\u0001\u0000\u0000\u0000\u0005=\u0001\u0000\u0000"+
		"\u0000\u0007?\u0001\u0000\u0000\u0000\tA\u0001\u0000\u0000\u0000\u000b"+
		"D\u0001\u0000\u0000\u0000\rK\u0001\u0000\u0000\u0000\u000fM\u0001\u0000"+
		"\u0000\u0000\u0011R\u0001\u0000\u0000\u0000\u0013Y\u0001\u0000\u0000\u0000"+
		"\u0015a\u0001\u0000\u0000\u0000\u0017h\u0001\u0000\u0000\u0000\u0019l"+
		"\u0001\u0000\u0000\u0000\u001bt\u0001\u0000\u0000\u0000\u001dz\u0001\u0000"+
		"\u0000\u0000\u001f\u0080\u0001\u0000\u0000\u0000!\u0087\u0001\u0000\u0000"+
		"\u0000#\u008f\u0001\u0000\u0000\u0000%\u0095\u0001\u0000\u0000\u0000\'"+
		"\u0097\u0001\u0000\u0000\u0000)\u0099\u0001\u0000\u0000\u0000+\u009b\u0001"+
		"\u0000\u0000\u0000-\u009d\u0001\u0000\u0000\u0000/\u009f\u0001\u0000\u0000"+
		"\u00001\u00a1\u0001\u0000\u0000\u00003\u00a3\u0001\u0000\u0000\u00005"+
		"6\u0005i\u0000\u000067\u0005f\u0000\u00007\u0002\u0001\u0000\u0000\u0000"+
		"89\u0005e\u0000\u00009:\u0005l\u0000\u0000:;\u0005s\u0000\u0000;<\u0005"+
		"e\u0000\u0000<\u0004\u0001\u0000\u0000\u0000=>\u0005[\u0000\u0000>\u0006"+
		"\u0001\u0000\u0000\u0000?@\u0005]\u0000\u0000@\b\u0001\u0000\u0000\u0000"+
		"AB\u0005f\u0000\u0000BC\u0005n\u0000\u0000C\n\u0001\u0000\u0000\u0000"+
		"DE\u0005r\u0000\u0000EF\u0005e\u0000\u0000FG\u0005t\u0000\u0000GH\u0005"+
		"u\u0000\u0000HI\u0005r\u0000\u0000IJ\u0005n\u0000\u0000J\f\u0001\u0000"+
		"\u0000\u0000KL\u0005,\u0000\u0000L\u000e\u0001\u0000\u0000\u0000MN\u0005"+
		"T\u0000\u0000NO\u0005R\u0000\u0000OP\u0005U\u0000\u0000PQ\u0005E\u0000"+
		"\u0000Q\u0010\u0001\u0000\u0000\u0000RS\u0005F\u0000\u0000ST\u0005A\u0000"+
		"\u0000TU\u0005L\u0000\u0000UV\u0005S\u0000\u0000VW\u0005E\u0000\u0000"+
		"W\u0012\u0001\u0000\u0000\u0000XZ\u0007\u0000\u0000\u0000YX\u0001\u0000"+
		"\u0000\u0000Z[\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001"+
		"\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0005p\u0000\u0000^_\u0005"+
		"x\u0000\u0000_\u0014\u0001\u0000\u0000\u0000`b\u0007\u0000\u0000\u0000"+
		"a`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000"+
		"\u0000cd\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000ef\u0005%\u0000"+
		"\u0000f\u0016\u0001\u0000\u0000\u0000gi\u0007\u0000\u0000\u0000hg\u0001"+
		"\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000"+
		"jk\u0001\u0000\u0000\u0000k\u0018\u0001\u0000\u0000\u0000lm\u0005#\u0000"+
		"\u0000mn\u0007\u0001\u0000\u0000no\u0007\u0001\u0000\u0000op\u0007\u0001"+
		"\u0000\u0000pq\u0007\u0001\u0000\u0000qr\u0007\u0001\u0000\u0000rs\u0007"+
		"\u0001\u0000\u0000s\u001a\u0001\u0000\u0000\u0000tv\u0005#\u0000\u0000"+
		"uw\u0007\u0002\u0000\u0000vu\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000"+
		"\u0000xv\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y\u001c\u0001"+
		"\u0000\u0000\u0000z|\u0005.\u0000\u0000{}\u0007\u0002\u0000\u0000|{\u0001"+
		"\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000"+
		"~\u007f\u0001\u0000\u0000\u0000\u007f\u001e\u0001\u0000\u0000\u0000\u0080"+
		"\u0084\u0007\u0003\u0000\u0000\u0081\u0083\u0007\u0002\u0000\u0000\u0082"+
		"\u0081\u0001\u0000\u0000\u0000\u0083\u0086\u0001\u0000\u0000\u0000\u0084"+
		"\u0082\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085"+
		" \u0001\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0087\u008b"+
		"\u0007\u0004\u0000\u0000\u0088\u008a\u0007\u0005\u0000\u0000\u0089\u0088"+
		"\u0001\u0000\u0000\u0000\u008a\u008d\u0001\u0000\u0000\u0000\u008b\u0089"+
		"\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\"\u0001"+
		"\u0000\u0000\u0000\u008d\u008b\u0001\u0000\u0000\u0000\u008e\u0090\u0007"+
		"\u0006\u0000\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u0090\u0091\u0001"+
		"\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0091\u0092\u0001"+
		"\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0094\u0006"+
		"\u0011\u0000\u0000\u0094$\u0001\u0000\u0000\u0000\u0095\u0096\u0005{\u0000"+
		"\u0000\u0096&\u0001\u0000\u0000\u0000\u0097\u0098\u0005}\u0000\u0000\u0098"+
		"(\u0001\u0000\u0000\u0000\u0099\u009a\u0005;\u0000\u0000\u009a*\u0001"+
		"\u0000\u0000\u0000\u009b\u009c\u0005:\u0000\u0000\u009c,\u0001\u0000\u0000"+
		"\u0000\u009d\u009e\u0005+\u0000\u0000\u009e.\u0001\u0000\u0000\u0000\u009f"+
		"\u00a0\u0005-\u0000\u0000\u00a00\u0001\u0000\u0000\u0000\u00a1\u00a2\u0005"+
		"*\u0000\u0000\u00a22\u0001\u0000\u0000\u0000\u00a3\u00a4\u0005:\u0000"+
		"\u0000\u00a4\u00a5\u0005=\u0000\u0000\u00a54\u0001\u0000\u0000\u0000\t"+
		"\u0000[cjx~\u0084\u008b\u0091\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}