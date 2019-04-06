/// <reference path="tree.ts" />
/// <reference path="ScanString.ts" />

namespace beagle.compiler {


enum LineBreak
{
    NONE = 0,
    BEFORE = 2,
    AFTER = 1,
    BOTH = 3
}

export class Token
{
    public type : TokenType;
	public value : string;
	public location : SourceLocation;
	public lineBreak : number;
    public comments : beagle.compiler.tree.Comment[];

    constructor(location : SourceLocation, lineBreak : number, comments : beagle.compiler.tree.Comment[], type : TokenType, value : string = "")
	{
		// clone location object
		this.location = new SourceLocation();
		this.location.line = location.line;
		this.location.column = location.column;

		this.lineBreak = lineBreak;
		this.comments = comments;
		this.value = null;
		if (type == null)
			this.type = TokenType.parse(value);
		else
			this.type = type;
		if (!this.type.isKeyword) this.value = value;
    }

}

export class TokenType
{
    public readonly name : string;
	public readonly isKeyword : boolean;
	public readonly token : string;
	private static entries: { [name: string] : TokenType } = {};

	static readonly TOK_ABSTRACT = new TokenType("abstract", true, 'TOK_ABSTRACT');
	static readonly TOK_AND = new TokenType("and", true, 'TOK_AND');
	static readonly TOK_AS = new TokenType("as", true, 'TOK_AS');
	static readonly TOK_ASSIGN = new TokenType("=", false, 'TOK_ASSIGN');
	static readonly TOK_AT = new TokenType("@", false, 'TOK_AT');
	static readonly TOK_BACK_SLASH = new TokenType("\\", false, 'TOK_BACK_SLASH');
	static readonly TOK_BAND = new TokenType("&", false, 'TOK_BAND');
	static readonly TOK_BAND_ASSIGN = new TokenType("&=", false, 'TOK_BAND_ASSIGN');
	static readonly TOK_BANG = new TokenType("!", false, 'TOK_BANG');
	static readonly TOK_BIN_LITERAL = new TokenType("", false, 'TOK_BIN_LITERAL');
	static readonly TOK_BOOL_LITERAL = new TokenType("", false, 'TOK_BOOL_LITERAL');
	static readonly TOK_BOOLEAN = new TokenType("bool", true, 'TOK_BOOLEAN');
	static readonly TOK_BOR = new TokenType("|", false, 'TOK_BOR');
	static readonly TOK_BOR_ASSIGN = new TokenType("|=", false, 'TOK_BOR_ASSIGN');
	static readonly TOK_BREAK = new TokenType("break", true, 'TOK_BREAK');
	static readonly TOK_CASE = new TokenType("case", true, 'TOK_CASE');
	static readonly TOK_CATCH = new TokenType("catch", true, 'TOK_CATCH');
	static readonly TOK_CHAR = new TokenType("char", true, 'TOK_CHAR');
	static readonly TOK_CLASS = new TokenType("class", true, 'TOK_CLASS');
	static readonly TOK_COLON = new TokenType(":", false, 'TOK_COLON');
	static readonly TOK_COMA = new TokenType(",", false, 'TOK_COMA');
	static readonly TOK_COMMENT = new TokenType("", false, 'TOK_COMMENT');
	static readonly TOK_CONST = new TokenType("const", true, 'TOK_CONST');
	static readonly TOK_CONTINUE = new TokenType("continue", true, 'TOK_CONTINUE');
	static readonly TOK_DEC = new TokenType("--", false, 'TOK_DEC');
	static readonly TOK_DEC_LITERAL = new TokenType("", false, 'TOK_DEC_LITERAL');
	static readonly TOK_DEDENT = new TokenType("", false, 'TOK_DEDENT');
	static readonly TOK_DEF = new TokenType("def", true, 'TOK_DEF');
	static readonly TOK_DEFAULT = new TokenType("default", true, 'TOK_DEFAULT');
	static readonly TOK_DIV = new TokenType("/", false, 'TOK_DIV');
	static readonly TOK_DIV_ASSIGN = new TokenType("/=", false, 'TOK_DIV_ASSIGN');
	static readonly TOK_DOCSTRING = new TokenType("", false, 'TOK_DOCSTRING');
	static readonly TOK_DOT = new TokenType(".", false, 'TOK_DOT');
	static readonly TOK_ELIF = new TokenType("elif", true, 'TOK_ELIF');
	static readonly TOK_DOUBLE = new TokenType("double", true, 'TOK_DOUBLE');
	static readonly TOK_ELSE = new TokenType("else", true, 'TOK_ELSE');
	static readonly TOK_EOF = new TokenType("end of file", false, 'TOK_EOF');
	static readonly TOK_EOL = new TokenType("end of line", false, 'TOK_EOL');
	static readonly TOK_EQ = new TokenType("==", false, 'TOK_EQ');
	static readonly TOK_EXTENDS = new TokenType("extends", true, 'TOK_EXTENDS');
	static readonly TOK_FALSE = new TokenType("false", true, 'TOK_FALSE');
	static readonly TOK_FINALLY = new TokenType("finally", true, 'TOK_FINALLY');
	static readonly TOK_FLOAT = new TokenType("float", true, 'TOK_FLOAT');
	static readonly TOK_FOR = new TokenType("for", true, 'TOK_FOR');
	static readonly TOK_FP_LITERAL = new TokenType("", false, 'TOK_FP_LITERAL');
	static readonly TOK_GE = new TokenType(">=", false, 'TOK_GE');
	static readonly TOK_GT = new TokenType(">", false, 'TOK_GT');
	static readonly TOK_HEX_LITERAL = new TokenType("", false, 'TOK_HEX_LITERAL');
	static readonly TOK_IF = new TokenType("if", true, 'TOK_IF');
	static readonly TOK_IMPLEMENTS = new TokenType("implements", true, 'TOK_IMPLEMENTS');
	static readonly TOK_IMPORT = new TokenType("import", true, 'TOK_IMPORT');
	static readonly TOK_IN = new TokenType("in", true, 'TOK_IN');
	static readonly TOK_INC = new TokenType("++", false, 'TOK_INC');
	static readonly TOK_INDENT = new TokenType("", false, 'TOK_INDENT');
	static readonly TOK_INTERFACE = new TokenType("interface", true, 'TOK_INTERFACE');
	static readonly TOK_IS = new TokenType("is", true, 'TOK_IS');
	static readonly TOK_LE = new TokenType("<=", false, 'TOK_LE');
	static readonly TOK_LEFT_BRACE = new TokenType("{", false, 'TOK_LEFT_BRACE');
	static readonly TOK_LEFT_BRACKET = new TokenType("[", false, 'TOK_LEFT_BRACKET');
	static readonly TOK_LEFT_PAR = new TokenType("(", false, 'TOK_LEFT_PAR');
	static readonly TOK_LET = new TokenType("let", true, 'TOK_LET');
	static readonly TOK_LONG = new TokenType("long", true, 'TOK_LONG');
	static readonly TOK_LT = new TokenType("<", false, 'TOK_LT');
	static readonly TOK_MINUS = new TokenType("-", false, 'TOK_MINUS');
	static readonly TOK_MINUS_ASSIGN = new TokenType("-=", false, 'TOK_MINUS_ASSIGN');
	static readonly TOK_MOD = new TokenType("%", false, 'TOK_MOD');
	static readonly TOK_MOD_ASSIGN = new TokenType("%=", false, 'TOK_MOD_ASSIGN');
	static readonly TOK_MUL = new TokenType("*", false, 'TOK_MUL');
	static readonly TOK_MUL_ASSIGN = new TokenType("*=", false, 'TOK_MUL_ASSIGN');
	static readonly TOK_NAME = new TokenType("", false, 'TOK_NAME');
	static readonly TOK_NAMESPACE = new TokenType("namespace", true, 'TOK_NAMESPACE');
	static readonly TOK_NATIVE = new TokenType("native", true, 'TOK_NATIVE');
	static readonly TOK_NE = new TokenType("!=", false, 'TOK_NE');
	static readonly TOK_NEG_ASSIGN = new TokenType("~=", false, 'TOK_NEG_ASSIGN');
	static readonly TOK_NEW = new TokenType("new", true, 'TOK_NEW');
	static readonly TOK_NIN = new TokenType("not in", false, 'TOK_NIN');
	static readonly TOK_NIS = new TokenType("not is", false, 'TOK_NIS');
	static readonly TOK_NOT = new TokenType("not", true, 'TOK_NOT');
	static readonly TOK_NOT_ASSIGN = new TokenType("!=", false, 'TOK_NOT_ASSIGN');
	static readonly TOK_NULL = new TokenType("null", true, 'TOK_NULL');
	static readonly TOK_OCT_LITERAL = new TokenType("", false, 'TOK_OCT_LITERAL');
	static readonly TOK_OR = new TokenType("or", true, 'TOK_OR');
	static readonly TOK_INTERNAL = new TokenType("internal", true, 'TOK_INTERNAL');
	static readonly TOK_PACKAGE = new TokenType("package", true, 'TOK_PACKAGE');
	static readonly TOK_PLUS = new TokenType("+", false, 'TOK_PLUS');
	static readonly TOK_PLUS_ASSIGN = new TokenType("+=", false, 'TOK_PLUS_ASSIGN');
	static readonly TOK_QUEST = new TokenType("?", false, 'TOK_QUEST');
	static readonly TOK_READLOCK = new TokenType("readlock", true, 'TOK_READLOCK');
	static readonly TOK_PRIVATE = new TokenType("private", true, 'TOK_PRIVATE');
	static readonly TOK_PROTECTED = new TokenType("protected", true, 'TOK_PROTECTED');
	static readonly TOK_PUBLIC = new TokenType("public", true, 'TOK_PUBLIC');
	static readonly TOK_RETURN = new TokenType("return", true, 'TOK_RETURN');
	static readonly TOK_RIGHT_BRACE = new TokenType("}", false, 'TOK_RIGHT_BRACE');
	static readonly TOK_RIGHT_BRACKET = new TokenType("]", false, 'TOK_RIGHT_BRACKET');
	static readonly TOK_RIGHT_PAR = new TokenType(")", false, 'TOK_RIGHT_PAR');
	static readonly TOK_SEMICOLON = new TokenType(";", false, 'TOK_SEMICOLON');
	static readonly TOK_SHL = new TokenType("<<", false, 'TOK_SHL');
	static readonly TOK_SHL_ASSIGN = new TokenType("<<=", false, 'TOK_SHL_ASSIGN');
	static readonly TOK_SHR = new TokenType(">>", false, 'TOK_SHR');
	static readonly TOK_SHR_ASSIGN = new TokenType(">>=", false, 'TOK_SHR_ASSIGN');
	static readonly TOK_STATIC = new TokenType("static", true, 'TOK_STATIC');
	static readonly TOK_STRING_LITERAL = new TokenType("string literal", false, 'TOK_STRING_LITERAL');
	static readonly TOK_MSTRING_LITERAL = new TokenType("multiline string literal", false, 'TOK_MSTRING_LITERAL');
	static readonly TOK_SUPER = new TokenType("super", true, 'TOK_SUPER');
	static readonly TOK_SUSPEND = new TokenType("suspend", true, 'TOK_SUSPEND');
	static readonly TOK_SWITCH = new TokenType("switch", true, 'TOK_SWITCH');
	static readonly TOK_THEN = new TokenType("then", true, 'TOK_THEN');
	static readonly TOK_THIS = new TokenType("this", true, 'TOK_THIS');
	static readonly TOK_THROW = new TokenType("throw", true, 'TOK_THROW');
	static readonly TOK_TILDE = new TokenType("~", false, 'TOK_TILDE');
	static readonly TOK_TRUE = new TokenType("true", true, 'TOK_TRUE');
	static readonly TOK_TRY = new TokenType("try", true, 'TOK_TRY');
	static readonly TOK_VAR = new TokenType("var", true, 'TOK_VAR');
	static readonly TOK_VARARG = new TokenType("vararg", true, 'TOK_VARARG');
	static readonly TOK_WHILE = new TokenType("while", true, 'TOK_WHILE');
	static readonly TOK_WRITELOCK = new TokenType("writelock", true, 'TOK_WRITELOCK');
	static readonly TOK_XOR = new TokenType("^", false, 'TOK_XOR');
	static readonly TOK_XOR_ASSIGN = new TokenType("^=", false, 'TOK_XOR_ASSIGN');
	static readonly TOK_STRUCT = new TokenType("struct", true, 'TOK_STRUCT');

    private constructor(name : string = "", isKeyword : boolean = false, token : string = "")
    {
        this.name = name;
		this.isKeyword = isKeyword;
		this.token = token;
		if (isKeyword) TokenType.entries[name] = this;
	}

	public static parse( name : string ) : TokenType
	{
		let item = TokenType.entries[name];
		if (item != null && item.isKeyword) return item;
		return TokenType.TOK_NAME;
	}
}



export class Scanner
{
	source : ScanString;
	context : CompilationContext;
	lineBreak : boolean = false;
	comments : beagle.compiler.tree.Comment[] = [];

	constructor( context : CompilationContext, source : ScanString )
	{
		this.source = source;
		this.context = context;
	}

	getLineBreak() : number
	{
		let state = 0;

		if (this.lineBreak)
			state = LineBreak.BEFORE;
		if (this.source.peekAt(1) == '\n')
			state |= LineBreak.AFTER;

		return state;
	}

	createToken( type : TokenType, name : string = null ) : Token
	{
		let state = this.getLineBreak();
		this.lineBreak = false;

		let output = new Token(this.source.location, state,
			(this.comments.length > 0) ? this.comments : null, type, name);

		if (this.comments.length > 0) this.comments = [];
		return output;
	}

	/**
	 * Advance the cursor and process the current character.
	 */
	readToken() : Token
	{
		while (true)
		{
			if (this.source.next() == '\n')
			{
				this.lineBreak = true;
				while (this.source.next() == '\n');
			}

            let current = this.source.peek();
			switch (current)
			{
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				case 'N':
				case 'O':
				case 'P':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
				case 'U':
				case 'V':
				case 'W':
				case 'X':
				case 'Y':
				case 'Z':
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z':
				case '$':
				case '_':
					return this.processIdentifier();
				case '/':
					// capture block comments
					if (this.source.peekAt(1) == '*')
					{
						let comm = this.processBlockComment();
						if (comm != null) this.comments.push(comm);
						break;
					}
					// capture inline comments
					if (this.source.peekAt(1) == '/')
					{
						let comm = this.processInlineComment();
						if (comm != null) this.comments.push(comm);
						break;
					}

					if (this.source.peekAt(1) == '=')
						return this.createToken(TokenType.TOK_DIV_ASSIGN);

					return this.createToken(TokenType.TOK_DIV);
				case '"':
				case '\'':
					return this.processString();
				case '=':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_EQ);
					}
					return this.createToken(TokenType.TOK_ASSIGN);
				case '+':
					if (this.source.peekAt(1) == '+')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_INC);
					}
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_PLUS_ASSIGN);
					}
					if (this.isDigit(this.source.peekAt(1)))
					{
						return this.processNumber();
					}
					return this.createToken(TokenType.TOK_PLUS);
				case '-':
					if (this.source.peekAt(1) == '-')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_DEC);
					}
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_MINUS_ASSIGN);
					}
					if (this.isDigit(this.source.peekAt(1)))
					{
						return this.processNumber();
					}
					return this.createToken(TokenType.TOK_MINUS);
				case '*':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_MUL_ASSIGN);
					}
					return this.createToken(TokenType.TOK_MUL);
				case '%':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_MOD_ASSIGN);
					}
					return this.createToken(TokenType.TOK_MOD);
				case '&':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_BAND_ASSIGN);
					}
					if (this.source.peekAt(1) == '&')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_AND);
					}
					return this.createToken(TokenType.TOK_BAND);
				case '|':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_BOR_ASSIGN);
					}
					if (this.source.peekAt(1) == '|')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_OR);
					}
					return this.createToken(TokenType.TOK_BOR);
				case '.':
					return this.createToken(TokenType.TOK_DOT);
				case '\\':
					return this.createToken(TokenType.TOK_BACK_SLASH);
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					return this.processNumber();
				case '(':
					return this.createToken(TokenType.TOK_LEFT_PAR);
				case ')':
					return this.createToken(TokenType.TOK_RIGHT_PAR);
				case '[':
					return this.createToken(TokenType.TOK_LEFT_BRACKET);
				case ']':
					return this.createToken(TokenType.TOK_RIGHT_BRACKET);
				case '{':
					return this.createToken(TokenType.TOK_LEFT_BRACE);
				case '}':
					return this.createToken(TokenType.TOK_RIGHT_BRACE);
				case '@':
					return this.createToken(TokenType.TOK_AT);
				case '>':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_GT);
					}
					if (this.source.peekAt(1) == '>')
					{
						this.source.next();
						if (this.source.peekAt(1) == '=')
						{
							this.source.next();
							return this.createToken(TokenType.TOK_SHR_ASSIGN);
						}
						else
							return this.createToken(TokenType.TOK_SHR);
					}
					return this.createToken(TokenType.TOK_GT);
				case '<':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_LT);
					}
					if (this.source.peekAt(1) == '<')
					{
						this.source.next();
						if (this.source.peekAt(1) == '=')
						{
							this.source.next();
							return this.createToken(TokenType.TOK_SHL_ASSIGN);
						}
						else
							return this.createToken(TokenType.TOK_SHL);
					}
					return this.createToken(TokenType.TOK_LT);

				case ',':
					return this.createToken(TokenType.TOK_COMA);

				case ';':
					return this.createToken(TokenType.TOK_SEMICOLON);

				case ':':
					return this.createToken(TokenType.TOK_COLON);

				case '?':
					return this.createToken(TokenType.TOK_QUEST);

				case '!':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_NE);
					}
					return this.createToken(TokenType.TOK_BANG);

				case '~':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_NEG_ASSIGN);
					}
					return this.createToken(TokenType.TOK_TILDE);

				case '^':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TokenType.TOK_XOR_ASSIGN);
					}
					return this.createToken(TokenType.TOK_XOR);

				case ScanString.BOL:
					break;

				case ScanString.EOI:
					return this.createToken(TokenType.TOK_EOF);

				default:
					if (this.isWhitespace(this.source.peek()) || this.source.peek() == ScanString.BOI)
						break;
					this.context.listener.onError(this.source.location, "Invalid character '" + this.source.peek() + "'");
					break;
			}
		}
	}

	processString() : Token
	{
		let type = this.source.peek();
		if (this.source.lookahead(type, type, type) == LookaheadStatus.MATCH)
			return this.processMultilineString();
            this.source.next();

		let capture = "";
		while (this.source.peek() != type && this.source.peek() != ScanString.EOI)
		{
			// FIXME: validate and expands escape sequences
			capture += this.source.peek();
			this.source.next();
		}
		if (this.source.peek() != type)
		{
			return this.returnError("Unterminated string");
		}
		else
			return this.createToken(TokenType.TOK_STRING_LITERAL, capture.toString());
	}

	processMultilineString() : Token
	{
		let type = this.source.peek();
		this.source.nextAt(3);
		let capture = "";
		while (this.source.lookahead(type, type, type) != LookaheadStatus.MATCH && this.source.peek() != ScanString.EOI)
		{
			// FIXME: validate and expands escape sequences
			let c = this.source.peek();
			if (c == '\n')
				capture += "\\n";
			else
				capture += c;
            this.source.next();
		}
		if (this.source.peek() != type)
		{
			return this.returnError("Unterminated string");
		}
		else
		{
			this.source.nextAt(2);
			return this.createToken(TokenType.TOK_MSTRING_LITERAL, capture.toString());
		}
	}

	processBlockComment() : beagle.compiler.tree.Comment
	{
		let type = TokenType.TOK_COMMENT;
		this.source.nextAt(2);

		if (this.source.peek() == '*')
		{
			this.source.next();
			type = TokenType.TOK_DOCSTRING;
		}

		let capture = "";
		while (this.source.peek() != ScanString.EOI && this.source.lookahead('*', '/') != LookaheadStatus.MATCH)
		{
			// FIXME: validate and expands escape sequences
			capture += this.source.peek();
			this.source.next();
		}

		if (this.source.lookahead('*', '/') != LookaheadStatus.MATCH)
		{
			//this.context.listener.onError(this.source.location, "Unterminated block comment");
			return null;
		}
		else
		{
			this.source.nextAt(1); // skip the '*' (but not the '/')
			this.discardWhiteSpaces();

			return new beagle.compiler.tree.Comment(capture.toString(), type == TokenType.TOK_DOCSTRING);
		}
	}

	discardWhiteSpaces()
	{
		while (true)
		{
			switch (this.source.peekAt(1))
			{
				case '\n':
				case ' ':
				case '\t':
                    this.source.next();
					continue;
				default:
					return;
			}
		}
	}

	processInlineComment() : beagle.compiler.tree.Comment
	{
		this.source.nextAt(2);

		let capture = "";
		while (this.source.peek() != '\n' && this.source.peek() != ScanString.EOI)
		{
			capture += this.source.peek();
			this.source.next();
		}
		return new beagle.compiler.tree.Comment(capture.toString(), false);
	}

	/*private Token emitIfLookahead( TokenType type, char... values)
	{
		//if (values.length == 0)
		//	throw new IllegalArgumentException("Missing lookahead symbols");

		if (this.source.lookahead(values) == LookaheadStatus.MATCH)
		{
			source.next(values.length - 1);
			return new Token(type);
		}
		return null;
	}*/

	isDigit(value : string) : boolean
	{
		return (value[0] >= '0' && value[0] <= '9');
	}

	/**
	 * Read a number (decimal, hexadecimal, binary or octal) and
	 * leave the cursor at the last digit.
	 *
	 * @return
	 */
	processNumber() : Token
	{
		let type = TokenType.TOK_DEC_LITERAL;

		let value = this.source.peek();
		if (value == '0')
		{
			value = this.source.peekAt(1);
			if (value == 'b' || value == 'B')
				return this.processBinary();
			if (value == 'x' || value == 'X')
				return this.processHexadecimal();
			//return processOctal();
			type = TokenType.TOK_OCT_LITERAL;
		}

		let capture = "";
		// first digit
		value = this.source.peek();
		if (this.isDigit(value)) capture += value;
		// remaining digits
		while (true)
		{
			value = this.source.peekAt(1);
			if (this.isDigit(value))
			{
				capture += value;
				this.source.next();
			}
			else
				break;
		}

		if (this.source.peekAt(1) == '.')
		{
			if (this.isDigit(this.source.peekAt(2)))
			{
				// we have a floating-point number
				capture += '.';
				this.source.next();

				while (true)
				{
					value = this.source.peekAt(1);
					if (this.isDigit(value))
					{
						capture += value;
						this.source.next();
					}
					else
						break;
				}

				return this.createToken(TokenType.TOK_FP_LITERAL, capture);
			}
		}

		return this.createToken(type, capture);
	}

	returnError( message : string ) : Token
	{
		this.context.listener.onError(this.source.location, message);
		return null;
	}

	processHexadecimal() : Token
	{
		let capture = "0x";
		this.source.nextAt(1);

		while (true)
		{
			let value = this.source.peekAt(1);
			if ((value >= '0' && value <= '9') ||
				(value >= 'a' && value <= 'f') ||
				(value >= 'A' && value <= 'F'))
			{
				this.source.next();
				capture += value;
			}
			else
				break;
		}

		if (capture.length > 2)
			return this.createToken(TokenType.TOK_HEX_LITERAL, capture);
		else
		{
			return this.returnError("Invalid hexadecimal literal");
		}
	}

	processBinary() : Token
	{
		let capture = "0b";
		this.source.next();

		while (true)
		{
			let value = this.source.peekAt(1);
			if (value == '0' || value == '1')
			{
				this.source.next();
				capture += value;
			}
			else
				break;
		}

		if (capture.length > 2)
			return this.createToken(TokenType.TOK_BIN_LITERAL, capture);
		else
		{
			return this.returnError("Invalid binary literal");
		}
	}

	/**
	 * Capture an identifier.
	 *
	 * The current character is guaranted to be valid. This function leaves the
	 * cursor in the last character of the identifier.
	 *
	 * @return
	 */
	processIdentifier() : Token
	{
		let capture = "";

		capture += this.source.peek();
		while (true)
		{
			let current = this.source.peekAt(1);
			if ((current >= 'A' && current <= 'Z') ||
				(current >= 'a' && current <= 'z') ||
				(current >= '0' && current <= '9') ||
				current == '_')
			{
				capture += this.source.next();
			}
			else
				break;
		}

		return this.createToken(null, capture);
    }

    isWhitespace(symbol : string) : boolean
    {
        switch (symbol)
        {
            case '\n':
            case '\r':
            case ' ':
            case '\t':
                return true;
            default: return false;
        }
    }
}

}
