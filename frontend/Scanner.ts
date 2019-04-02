namespace beagle.compiler {


class CompilationContext
{
}

class Comment
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
	value : String;
	location : SourceLocation;
	lineBreak : number;
    comments : Comment[];

    constructor(location : SourceLocation, lineBreak : number, comments : Comment[], type : TokenType, value : string = "")
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

    constructor(name : string = "", isKeyword : boolean = false)
    {
        this.name = name;
        this.isKeyword = isKeyword;
    }
}

const TOK_ABSTRACT = new TokenType("abstract", true);
const TOK_AND = new TokenType("and", true);
const TOK_AS = new TokenType("as", true);
const TOK_ASSIGN = new TokenType("=", false);
const TOK_AT = new TokenType("@", false);
const TOK_BACK_SLASH = new TokenType("\\", false);
const TOK_BAND = new TokenType("&", false);
const TOK_BAND_ASSIGN = new TokenType("&=", false);
const TOK_BANG = new TokenType("!", false);
const TOK_BIN_LITERAL = new TokenType;
const TOK_BOOL_LITERAL = new TokenType;
const TOK_BOOLEAN = new TokenType("bool", true);
const TOK_BOR = new TokenType("|", false);
const TOK_BOR_ASSIGN = new TokenType("|=", false);
const TOK_BREAK = new TokenType("break", true);
const TOK_CASE = new TokenType("case", true);
const TOK_CATCH = new TokenType("catch", true);
const TOK_CHAR = new TokenType("char", true);
const TOK_CLASS = new TokenType("class", true);
const TOK_COLON = new TokenType(":", false);
const TOK_COMA = new TokenType(",", false);
const TOK_COMMENT = new TokenType;
const TOK_CONST = new TokenType("const", true);
const TOK_CONTINUE = new TokenType("continue", true);
const TOK_DEC = new TokenType("--", false);
const TOK_DEC_LITERAL = new TokenType;
const TOK_DEDENT = new TokenType;
const TOK_DEF = new TokenType("def", true);
const TOK_DEFAULT = new TokenType("default", true);
const TOK_DIV = new TokenType("/", false);
const TOK_DIV_ASSIGN = new TokenType("/=", false);
const TOK_DOCSTRING = new TokenType;
const TOK_DOT = new TokenType(".", false);
const TOK_ELIF = new TokenType("elif", true);
//TOK_DOUBLE("double", true),
const TOK_ELSE = new TokenType("else", true);
const TOK_EOF = new TokenType("end of file", false);
const TOK_EOL = new TokenType("end of line", false);
const TOK_EQ = new TokenType("==", false);
const TOK_EXTENDS = new TokenType("extends", true);
const TOK_FALSE = new TokenType("false", true);
const TOK_FINALLY = new TokenType("finally", true);
//TOK_FLOAT("float", true),
const TOK_FOR = new TokenType("for", true);
const TOK_FP_LITERAL = new TokenType;
const TOK_GE = new TokenType(">=", false);
const TOK_GT = new TokenType(">", false);
const TOK_HEX_LITERAL = new TokenType;
const TOK_IF = new TokenType("if", true);
const TOK_IMPLEMENTS = new TokenType("implements", true);
const TOK_IMPORT = new TokenType("import", true);
const TOK_IN = new TokenType("in", true);
const TOK_INC = new TokenType("++", false);
const TOK_INDENT = new TokenType();
const TOK_INTERFACE = new TokenType("interface", true);
const TOK_IS = new TokenType("is", true);
const TOK_LE = new TokenType("<=", false);
const TOK_LEFT_BRACE = new TokenType("{", false);
const TOK_LEFT_BRACKET = new TokenType("[", false);
const TOK_LEFT_PAR = new TokenType("(", false);
const TOK_LONG = new TokenType("long", true);
const TOK_LT = new TokenType("<", false);
const TOK_MINUS = new TokenType("-", false);
const TOK_MINUS_ASSIGN = new TokenType("-=", false);
const TOK_MOD = new TokenType("%", false);
const TOK_MOD_ASSIGN = new TokenType("%=", false);
const TOK_MUL = new TokenType("*", false);
const TOK_MUL_ASSIGN = new TokenType("*=", false);
const TOK_NAME = new TokenType();
const TOK_NATIVE = new TokenType("native", true);
const TOK_NE = new TokenType("!=", false);
const TOK_NEG_ASSIGN = new TokenType("~=", false);
const TOK_NEW = new TokenType("new", true);
const TOK_NIN = new TokenType("not in", false);
const TOK_NIS = new TokenType("not is", false);
const TOK_NOT = new TokenType("not", true);
const TOK_NOT_ASSIGN = new TokenType("!=", false);
const TOK_NULL = new TokenType("null", true);
const TOK_OCT_LITERAL = new TokenType;
const TOK_OR = new TokenType("or", true);
//TOK_INTERNAL("internal", true),
const TOK_PACKAGE = new TokenType("package", true);
const TOK_PLUS = new TokenType("+", false);
const TOK_PLUS_ASSIGN = new TokenType("+=", false);
const TOK_QUEST = new TokenType("?", false);
const TOK_READLOCK = new TokenType("readlock", true);
//TOK_PRIVATE("private", true),
//TOK_PROTECTED("protected", true),
//TOK_PUBLIC("public", true),
const TOK_RETURN = new TokenType("return", true);
const TOK_RIGHT_BRACE = new TokenType("}", false);
const TOK_RIGHT_BRACKET = new TokenType("]", false);
const TOK_RIGHT_PAR = new TokenType(")", false);
const TOK_SEMICOLON = new TokenType(";", false);
const TOK_SHL = new TokenType("<<", false);
const TOK_SHL_ASSIGN = new TokenType("<<=", false);
const TOK_SHR = new TokenType(">>", false);
const TOK_SHR_ASSIGN = new TokenType(">>=", false);
const TOK_STATIC = new TokenType("static", true);
const TOK_STRING_LITERAL = new TokenType("string literal", false);
const TOK_MSTRING_LITERAL = new TokenType("multiline string literal", false);
const TOK_SUPER = new TokenType("super", true);
const TOK_SUSPEND = new TokenType("suspend", true);
const TOK_SWITCH = new TokenType("switch", true);
const TOK_THEN = new TokenType("then", true);
const TOK_THIS = new TokenType("this", true);
const TOK_THROW = new TokenType("throw", true);
const TOK_TILDE = new TokenType("~", false);
//TOK_UINT8("uint8", true),
//TOK_UINT16("uint16", true),
//TOK_UINT32("uint32", true),
//TOK_UINT64("uint64", true),
//TOK_INT8("int8", true),
//TOK_INT16("int16", true),
//TOK_INT32("int32", true),
//TOK_INT64("int64", true),
const TOK_TRUE = new TokenType("true", true);
const TOK_TRY = new TokenType("try", true);
const TOK_VAR = new TokenType("var", true);
const TOK_VARARG = new TokenType("vararg", true);
const TOK_WHILE = new TokenType("while", true);
const TOK_WRITELOCK = new TokenType("writelock", true);
const TOK_XOR = new TokenType("^", false);
const TOK_XOR_ASSIGN = new TokenType("^=", false);
const TOK_STRUCT = new TokenType("struct", true);

export class Scanner 
{
	source : ScanString;
	listener : CompilationListener;
	context : CompilationContext;
	lineBreak : boolean = false;
	comments : Comment[];

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

	processBlockComment() : Comment
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

			return new tree.Comment(capture.toString(), type == TOK_DOCSTRING);
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

	processInlineComment() : Comment
	{
		this.source.nextAt(2);

		let capture = "";
		while (this.source.peek() != '\n' && this.source.peek() != ScanString.EOI)
		{
			capture += this.source.peek();
			this.source.next();
		}
		return new tree.Comment(capture.toString(), false);
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