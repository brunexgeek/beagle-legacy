/// <reference path="tree.ts" />
/// <reference path="ScanString.ts" />

namespace beagle.compiler {


class CompilationContext
{
}


enum LineBreak
{
    NONE = 0,
    BEFORE = 2,
    AFTER = 1,
    BOTH = 3
}

class Token
{
    type : TokenType;
	value : string;
	location : SourceLocation;
	lineBreak : number;
    comments : beagle.compiler.tree.Comment[];

    constructor(location : SourceLocation, lineBreak : number, comments : beagle.compiler.tree.Comment[], type : TokenType, value : string = "")
	{
		//this.location = location.clone();
		this.lineBreak = lineBreak;
		this.comments = comments;
		this.type = type;
		this.value = value;
    }

}

class TokenType
{
    name : string;
	isKeyword : boolean;
	token : string;

    constructor(name : string = "", isKeyword : boolean = false, token : string = "")
    {
        this.name = name;
		this.isKeyword = isKeyword;
		this.token = token;
    }
}

export const TOK_ABSTRACT = new TokenType("abstract", true, 'TOK_ABSTRACT');
export const TOK_AND = new TokenType("and", true, 'TOK_AND');
export const TOK_AS = new TokenType("as", true, 'TOK_AS');
export const TOK_ASSIGN = new TokenType("=", false, 'TOK_ASSIGN');
export const TOK_AT = new TokenType("@", false, 'TOK_AT');
export const TOK_BACK_SLASH = new TokenType("\\", false, 'TOK_BACK_SLASH');
export const TOK_BAND = new TokenType("&", false, 'TOK_BAND');
export const TOK_BAND_ASSIGN = new TokenType("&=", false, 'TOK_BAND_ASSIGN');
export const TOK_BANG = new TokenType("!", false, 'TOK_BANG');
export const TOK_BIN_LITERAL = new TokenType("", false, 'TOK_BIN_LITERAL');
export const TOK_BOOL_LITERAL = new TokenType("", false, 'TOK_BOOL_LITERAL');
export const TOK_BOOLEAN = new TokenType("bool", true, 'TOK_BOOLEAN');
export const TOK_BOR = new TokenType("|", false, 'TOK_BOR');
export const TOK_BOR_ASSIGN = new TokenType("|=", false, 'TOK_BOR_ASSIGN');
export const TOK_BREAK = new TokenType("break", true, 'TOK_BREAK');
export const TOK_CASE = new TokenType("case", true, 'TOK_CASE');
export const TOK_CATCH = new TokenType("catch", true, 'TOK_CATCH');
export const TOK_CHAR = new TokenType("char", true, 'TOK_CHAR');
export const TOK_CLASS = new TokenType("class", true, 'TOK_CLASS');
export const TOK_COLON = new TokenType(":", false, 'TOK_COLON');
export const TOK_COMA = new TokenType(",", false, 'TOK_COMA');
export const TOK_COMMENT = new TokenType("", false, 'TOK_COMMENT');
export const TOK_CONST = new TokenType("const", true, 'TOK_CONST');
export const TOK_CONTINUE = new TokenType("continue", true, 'TOK_CONTINUE');
export const TOK_DEC = new TokenType("--", false, 'TOK_DEC');
export const TOK_DEC_LITERAL = new TokenType("", false, 'TOK_DEC_LITERAL');
export const TOK_DEDENT = new TokenType("", false, 'TOK_DEDENT');
export const TOK_DEF = new TokenType("def", true, 'TOK_DEF');
export const TOK_DEFAULT = new TokenType("default", true, 'TOK_DEFAULT');
export const TOK_DIV = new TokenType("/", false, 'TOK_DIV');
export const TOK_DIV_ASSIGN = new TokenType("/=", false, 'TOK_DIV_ASSIGN');
export const TOK_DOCSTRING = new TokenType("", false, 'TOK_DOCSTRING');
export const TOK_DOT = new TokenType(".", false, 'TOK_DOT');
export const TOK_ELIF = new TokenType("elif", true, 'TOK_ELIF');
export const TOK_DOUBLE = new TokenType("double", true, 'TOK_DOUBLE');
export const TOK_ELSE = new TokenType("else", true, 'TOK_ELSE');
export const TOK_EOF = new TokenType("end of file", false, 'TOK_EOF');
export const TOK_EOL = new TokenType("end of line", false, 'TOK_EOL');
export const TOK_EQ = new TokenType("==", false, 'TOK_EQ');
export const TOK_EXTENDS = new TokenType("extends", true, 'TOK_EXTENDS');
export const TOK_FALSE = new TokenType("false", true, 'TOK_FALSE');
export const TOK_FINALLY = new TokenType("finally", true, 'TOK_FINALLY');
export const TOK_FLOAT = new TokenType("float", true, 'TOK_FLOAT');
export const TOK_FOR = new TokenType("for", true, 'TOK_FOR');
export const TOK_FP_LITERAL = new TokenType("", false, 'TOK_FP_LITERAL');
export const TOK_GE = new TokenType(">=", false, 'TOK_GE');
export const TOK_GT = new TokenType(">", false, 'TOK_GT');
export const TOK_HEX_LITERAL = new TokenType("", false, 'TOK_HEX_LITERAL');
export const TOK_IF = new TokenType("if", true, 'TOK_IF');
export const TOK_IMPLEMENTS = new TokenType("implements", true, 'TOK_IMPLEMENTS');
export const TOK_IMPORT = new TokenType("import", true, 'TOK_IMPORT');
export const TOK_IN = new TokenType("in", true, 'TOK_IN');
export const TOK_INC = new TokenType("++", false, 'TOK_INC');
export const TOK_INDENT = new TokenType("", false, 'TOK_INDENT');
export const TOK_INTERFACE = new TokenType("interface", true, 'TOK_INTERFACE');
export const TOK_IS = new TokenType("is", true, 'TOK_IS');
export const TOK_LE = new TokenType("<=", false, 'TOK_LE');
export const TOK_LEFT_BRACE = new TokenType("{", false, 'TOK_LEFT_BRACE');
export const TOK_LEFT_BRACKET = new TokenType("[", false, 'TOK_LEFT_BRACKET');
export const TOK_LEFT_PAR = new TokenType("(", false, 'TOK_LEFT_PAR');
export const TOK_LONG = new TokenType("long", true, 'TOK_LONG');
export const TOK_LT = new TokenType("<", false, 'TOK_LT');
export const TOK_MINUS = new TokenType("-", false, 'TOK_MINUS');
export const TOK_MINUS_ASSIGN = new TokenType("-=", false, 'TOK_MINUS_ASSIGN');
export const TOK_MOD = new TokenType("%", false, 'TOK_MOD');
export const TOK_MOD_ASSIGN = new TokenType("%=", false, 'TOK_MOD_ASSIGN');
export const TOK_MUL = new TokenType("*", false, 'TOK_MUL');
export const TOK_MUL_ASSIGN = new TokenType("*=", false, 'TOK_MUL_ASSIGN');
export const TOK_NAME = new TokenType("", false, 'TOK_NAME');
export const TOK_NATIVE = new TokenType("native", true, 'TOK_NATIVE');
export const TOK_NE = new TokenType("!=", false, 'TOK_NE');
export const TOK_NEG_ASSIGN = new TokenType("~=", false, 'TOK_NEG_ASSIGN');
export const TOK_NEW = new TokenType("new", true, 'TOK_NEW');
export const TOK_NIN = new TokenType("not in", false, 'TOK_NIN');
export const TOK_NIS = new TokenType("not is", false, 'TOK_NIS');
export const TOK_NOT = new TokenType("not", true, 'TOK_NOT');
export const TOK_NOT_ASSIGN = new TokenType("!=", false, 'TOK_NOT_ASSIGN');
export const TOK_NULL = new TokenType("null", true, 'TOK_NULL');
export const TOK_OCT_LITERAL = new TokenType("", false, 'TOK_OCT_LITERAL');
export const TOK_OR = new TokenType("or", true, 'TOK_OR');
export const TOK_INTERNAL = new TokenType("internal", true, 'TOK_INTERNAL');
export const TOK_PACKAGE = new TokenType("package", true, 'TOK_PACKAGE');
export const TOK_PLUS = new TokenType("+", false, 'TOK_PLUS');
export const TOK_PLUS_ASSIGN = new TokenType("+=", false, 'TOK_PLUS_ASSIGN');
export const TOK_QUEST = new TokenType("?", false, 'TOK_QUEST');
export const TOK_READLOCK = new TokenType("readlock", true, 'TOK_READLOCK');
export const TOK_PRIVATE = new TokenType("private", true, 'TOK_PRIVATE');
export const TOK_PROTECTED = new TokenType("protected", true, 'TOK_PROTECTED');
export const TOK_PUBLIC = new TokenType("public", true, 'TOK_PUBLIC');
export const TOK_RETURN = new TokenType("return", true, 'TOK_RETURN');
export const TOK_RIGHT_BRACE = new TokenType("}", false, 'TOK_RIGHT_BRACE');
export const TOK_RIGHT_BRACKET = new TokenType("]", false, 'TOK_RIGHT_BRACKET');
export const TOK_RIGHT_PAR = new TokenType(")", false, 'TOK_RIGHT_PAR');
export const TOK_SEMICOLON = new TokenType(";", false, 'TOK_SEMICOLON');
export const TOK_SHL = new TokenType("<<", false, 'TOK_SHL');
export const TOK_SHL_ASSIGN = new TokenType("<<=", false, 'TOK_SHL_ASSIGN');
export const TOK_SHR = new TokenType(">>", false, 'TOK_SHR');
export const TOK_SHR_ASSIGN = new TokenType(">>=", false, 'TOK_SHR_ASSIGN');
export const TOK_STATIC = new TokenType("static", true, 'TOK_STATIC');
export const TOK_STRING_LITERAL = new TokenType("string literal", false, 'TOK_STRING_LITERAL');
export const TOK_MSTRING_LITERAL = new TokenType("multiline string literal", false, 'TOK_MSTRING_LITERAL');
export const TOK_SUPER = new TokenType("super", true, 'TOK_SUPER');
export const TOK_SUSPEND = new TokenType("suspend", true, 'TOK_SUSPEND');
export const TOK_SWITCH = new TokenType("switch", true, 'TOK_SWITCH');
export const TOK_THEN = new TokenType("then", true, 'TOK_THEN');
export const TOK_THIS = new TokenType("this", true, 'TOK_THIS');
export const TOK_THROW = new TokenType("throw", true, 'TOK_THROW');
export const TOK_TILDE = new TokenType("~", false, 'TOK_TILDE');
export const TOK_TRUE = new TokenType("true", true, 'TOK_TRUE');
export const TOK_TRY = new TokenType("try", true, 'TOK_TRY');
export const TOK_VAR = new TokenType("var", true, 'TOK_VAR');
export const TOK_VARARG = new TokenType("vararg", true, 'TOK_VARARG');
export const TOK_WHILE = new TokenType("while", true, 'TOK_WHILE');
export const TOK_WRITELOCK = new TokenType("writelock", true, 'TOK_WRITELOCK');
export const TOK_XOR = new TokenType("^", false, 'TOK_XOR');
export const TOK_XOR_ASSIGN = new TokenType("^=", false, 'TOK_XOR_ASSIGN');
export const TOK_STRUCT = new TokenType("struct", true, 'TOK_STRUCT');

export class Scanner
{
	source : ScanString;
	listener : CompilationListener;
	context : CompilationContext;
	lineBreak : boolean = false;
	comments : beagle.compiler.tree.Comment[] = [];

	constructor( context : CompilationContext, source : ScanString )
	{
		this.source = source;
		this.context = context;
		//this.listener = context.listener;
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

	createToken( type : TokenType, name : string = "" ) : Token
	{
		let state = this.getLineBreak();
		this.lineBreak = false;

		let output = new Token(null, state, (this.comments.length > 0) ? this.comments : null, type, name);

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
						return this.createToken(TOK_DIV_ASSIGN);

					return this.createToken(TOK_DIV);
				case '"':
				case '\'':
					return this.processString();
				case '=':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_EQ);
					}
					return this.createToken(TOK_ASSIGN);
				case '+':
					if (this.source.peekAt(1) == '+')
					{
						this.source.next();
						return this.createToken(TOK_INC);
					}
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_PLUS_ASSIGN);
					}
					if (this.isDigit(this.source.peekAt(1)))
					{
						return this.processNumber();
					}
					return this.createToken(TOK_PLUS);
				case '-':
					if (this.source.peekAt(1) == '-')
					{
						this.source.next();
						return this.createToken(TOK_DEC);
					}
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_MINUS_ASSIGN);
					}
					if (this.isDigit(this.source.peekAt(1)))
					{
						return this.processNumber();
					}
					return this.createToken(TOK_MINUS);
				case '*':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_MUL_ASSIGN);
					}
					return this.createToken(TOK_MUL);
				case '%':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_MOD_ASSIGN);
					}
					return this.createToken(TOK_MOD);
				case '&':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_BAND_ASSIGN);
					}
					if (this.source.peekAt(1) == '&')
					{
						this.source.next();
						return this.createToken(TOK_AND);
					}
					return this.createToken(TOK_BAND);
				case '|':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_BOR_ASSIGN);
					}
					if (this.source.peekAt(1) == '|')
					{
						this.source.next();
						return this.createToken(TOK_OR);
					}
					return this.createToken(TOK_BOR);
				case '.':
					return this.createToken(TOK_DOT);
				case '\\':
					return this.createToken(TOK_BACK_SLASH);
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
					return this.createToken(TOK_LEFT_PAR);
				case ')':
					return this.createToken(TOK_RIGHT_PAR);
				case '[':
					return this.createToken(TOK_LEFT_BRACKET);
				case ']':
					return this.createToken(TOK_RIGHT_BRACKET);
				case '{':
					return this.createToken(TOK_LEFT_BRACE);
				case '}':
					return this.createToken(TOK_RIGHT_BRACE);
				case '@':
					return this.createToken(TOK_AT);
				case '>':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_GT);
					}
					if (this.source.peekAt(1) == '>')
					{
						this.source.next();
						if (this.source.peekAt(1) == '=')
						{
							this.source.next();
							return this.createToken(TOK_SHR_ASSIGN);
						}
						else
							return this.createToken(TOK_SHR);
					}
					return this.createToken(TOK_GT);
				case '<':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_LT);
					}
					if (this.source.peekAt(1) == '<')
					{
						this.source.next();
						if (this.source.peekAt(1) == '=')
						{
							this.source.next();
							return this.createToken(TOK_SHL_ASSIGN);
						}
						else
							return this.createToken(TOK_SHL);
					}
					return this.createToken(TOK_LT);

				case ',':
					return this.createToken(TOK_COMA);

				case ';':
					return this.createToken(TOK_SEMICOLON);

				case ':':
					return this.createToken(TOK_COLON);

				case '?':
					return this.createToken(TOK_QUEST);

				case '!':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_NE);
					}
					return this.createToken(TOK_BANG);

				case '~':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_NEG_ASSIGN);
					}
					return this.createToken(TOK_TILDE);

				case '^':
					if (this.source.peekAt(1) == '=')
					{
						this.source.next();
						return this.createToken(TOK_XOR_ASSIGN);
					}
					return this.createToken(TOK_XOR);

				case ScanString.BOL:
					break;

				case ScanString.EOI:
					return this.createToken(TOK_EOF);

				default:
					if (this.isWhitespace(this.source.peek()) || this.source.peek() == ScanString.BOI)
						break;
					//listener.onError(this.source.location, "Invalid character '" + this.source.peek() + "'");
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
			return this.createToken(TOK_STRING_LITERAL, capture.toString());
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
			return this.createToken(TOK_MSTRING_LITERAL, capture.toString());
		}
	}

	processBlockComment() : beagle.compiler.tree.Comment
	{
		let type = TOK_COMMENT;
		this.source.nextAt(2);

		if (this.source.peek() == '*')
		{
			this.source.next();
			type = TOK_DOCSTRING;
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

			return new beagle.compiler.tree.Comment(capture.toString(), type == TOK_DOCSTRING);
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
		let type = TOK_DEC_LITERAL;

		let value = this.source.peek();
		if (value == '0')
		{
			value = this.source.peekAt(1);
			if (value == 'b' || value == 'B')
				return this.processBinary();
			if (value == 'x' || value == 'X')
				return this.processHexadecimal();
			//return processOctal();
			type = TOK_OCT_LITERAL;
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

				return this.createToken(TOK_FP_LITERAL, capture.toString());
			}
		}

		return this.createToken(type, capture.toString());
	}

	returnError( message : string ) : Token
	{
		//this.listener.onError(this.source.location, message);
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
			return this.createToken(TOK_HEX_LITERAL, capture.toString());
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
			return this.createToken(TOK_BIN_LITERAL, capture.toString());
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

		return this.createToken(TOK_NAME, capture);
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


