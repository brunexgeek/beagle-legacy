package beagle.compiler;

import beagle.compiler.Token.LineBreak;

/**
 * Extract tokens from the input source code.
 */
public class Scanner implements IScanner
{

	protected ScanString source;

	private CompilationListener listener;

	private CompilationContext context;

	private boolean ignoreEol = true;

	Scanner( CompilationContext context, ScanString source )
	{
		this.source = source;
		this.context = context;
		this.listener = context.listener;
	}

	protected LineBreak getLineBreak()
	{
		return LineBreak.NONE;
	}

	protected Token createToken( TokenType type )
	{
		return new Token(source.location, getLineBreak(), type);
	}

	protected Token createToken( TokenType type, String name )
	{
		if (type == null)
			return new Token(source.location, getLineBreak(), name);
		else
			return new Token(source.location, getLineBreak(), type, name);
	}

	/**
	 * Advance the cursor and process the current character.
	 */
	@Override
	public Token readToken()
	{
		while (true)
		{
			char last = source.peek();
			if (last == ScanString.BOI) last = '\n';

			switch (source.next())
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
					return processIdentifier();
				case '/':
					// capture block comments
					if (source.peek(1) == '*')
					{
						processBlockComment();
						break;
					}
					// capture inline comments
					if (source.peek(1) == '/')
					{
						processInlineComment();
						break;
					}

					if (source.peek(1) == '=')
						return createToken(TokenType.TOK_DIV_ASSIGN);

					return createToken(TokenType.TOK_DIV);
				case '"':
				case '\'':
					return processString();
				case '=':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_EQ);
					}
					return createToken(TokenType.TOK_ASSIGN);
				case '+':
					if (source.peek(1) == '+')
					{
						source.next();
						return createToken(TokenType.TOK_INC);
					}
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_PLUS_ASSIGN);
					}
					if (isDigit(source.peek(1)))
					{
						return processNumber();
					}
					return createToken(TokenType.TOK_PLUS);
				case '-':
					if (source.peek(1) == '-')
					{
						source.next();
						return createToken(TokenType.TOK_DEC);
					}
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_MINUS_ASSIGN);
					}
					if (isDigit(source.peek(1)))
					{
						return processNumber();
					}
					return createToken(TokenType.TOK_MINUS);
				case '*':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_MUL_ASSIGN);
					}
					return createToken(TokenType.TOK_MUL);
				case '%':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_MOD_ASSIGN);
					}
					return createToken(TokenType.TOK_MOD);
				case '&':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_BAND_ASSIGN);
					}
					if (source.peek(1) == '&')
					{
						source.next();
						return createToken(TokenType.TOK_AND);
					}
					return createToken(TokenType.TOK_BAND);
				case '|':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_BOR_ASSIGN);
					}
					if (source.peek(1) == '|')
					{
						source.next();
						return createToken(TokenType.TOK_OR);
					}
					return createToken(TokenType.TOK_BOR);
				case '.':
					return createToken(TokenType.TOK_DOT);
				case '\\':
					return createToken(TokenType.TOK_BACK_SLASH);
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
					return processNumber();
				case '(':
					return createToken(TokenType.TOK_LEFT_PAR);
				case ')':
					return createToken(TokenType.TOK_RIGHT_PAR);
				case '[':
					return createToken(TokenType.TOK_LEFT_BRACKET);
				case ']':
					return createToken(TokenType.TOK_RIGHT_BRACKET);
				case '{':
					return createToken(TokenType.TOK_LEFT_BRACE);
				case '}':
					return createToken(TokenType.TOK_RIGHT_BRACE);
				case '@':
					return createToken(TokenType.TOK_AT);
				case '>':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_GREATER_THAN);
					}
					if (source.peek(1) == '>')
					{
						source.next();
						if (source.peek(1) == '=')
						{
							source.next();
							return createToken(TokenType.TOK_SHR_ASSIGN);
						}
						else
							return createToken(TokenType.TOK_SHR);
					}
					return createToken(TokenType.TOK_GREATER_THAN);
				case '<':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_LESS_THAN);
					}
					if (source.peek(1) == '<')
					{
						source.next();
						if (source.peek(1) == '=')
						{
							source.next();
							return createToken(TokenType.TOK_SHL_ASSIGN);
						}
						else
							return createToken(TokenType.TOK_SHL);
					}
					return createToken(TokenType.TOK_LESS_THAN);

				case ',':
					return createToken(TokenType.TOK_COMA);

				case ';':
					return createToken(TokenType.TOK_SEMICOLON);

				case ':':
					return createToken(TokenType.TOK_COLON);

				case '?':
					return createToken(TokenType.TOK_QUEST);

				case '!':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_NE);
					}
					return createToken(TokenType.TOK_BANG);

				case '~':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_NEG_ASSIGN);
					}
					return createToken(TokenType.TOK_TILDE);

				case '^':
					if (source.peek(1) == '=')
					{
						source.next();
						return createToken(TokenType.TOK_XOR_ASSIGN);
					}
					return createToken(TokenType.TOK_XOR);

				case '\n':
					if (ignoreEol || last == '\n') break;
					return createToken(TokenType.TOK_EOL);

				case ScanString.BOL:
					break;

				case ScanString.EOI:
					return createToken(TokenType.TOK_EOF);

				default:
					if (Character.isWhitespace(source.peek()) || source.peek() == ScanString.BOI)
						break;
					listener.onError(source.location, "Invalid character '" + source.peek() + "'");
					break;
			}
		}
	}

	private Token processString()
	{
		char type = source.peek();
		source.next();

		Capture capture = new Capture();
		while (source.peek() != type && source.peek() != ScanString.EOI)
		{
			capture.push(source.peek());
			if (source.peek() == '\\')
			{
				// FIXME: validate escape sequence
				capture.push(source.peek(1));
				source.next(2);
			}
			else
				source.next();
		}
		if (source.peek() != type)
		{
			return returnError("Unterminated string");
		}
		else
			return createToken(TokenType.TOK_STRING_LITERAL, capture.toString());
	}

	private Token processBlockComment()
	{
		TokenType type = TokenType.TOK_COMMENT;
		source.next(2);

		if (source.peek() == '*')
		{
			source.next();
			type = TokenType.TOK_DOCSTRING;
		}

		Capture capture = new Capture();
		while (source.peek() != ScanString.EOI && source.lookahead('*', '/') != LookaheadStatus.MATCH)
		{
			capture.push(source.peek());
			if (source.peek() == '\\')
			{
				// FIXME: validate escape sequence
				capture.push(source.peek(1));
				source.next(2);
			}
			else
				source.next();
		}

		if (source.lookahead('*', '/') != LookaheadStatus.MATCH)
		{
			return returnError("Unterminated block comment");
		}
		else
		{
			source.next(1); // skip the '*' (but not the '/')
			discardWhiteSpaces();
			return createToken(type, capture.toString());
		}
	}

	private void discardWhiteSpaces()
	{
		while (true)
		{
			switch (source.peek(1))
			{
				case '\n':
				case ' ':
				case '\t':
					source.next();
					continue;
				default:
					return;
			}
		}
	}

	private Token processInlineComment()
	{
		source.next(2);

		Capture capture = new Capture();
		while (source.peek() != '\n' && source.peek() != ScanString.EOI)
		{
			capture.push(source.peek());
			source.next();
		}
		return createToken(TokenType.TOK_COMMENT, capture.toString());
	}

	/*private Token emitIfLookahead( TokenType type, char... values)
	{
		//if (values.length == 0)
		//	throw new IllegalArgumentException("Missing lookahead symbols");

		if (source.lookahead(values) == LookaheadStatus.MATCH)
		{
			source.next(values.length - 1);
			return new Token(type);
		}
		return null;
	}*/

	private boolean isDigit(char value)
	{
		return (value >= '0' && value <= '9');
	}

	private Token processNumber()
	{
		TokenType type = TokenType.TOK_DEC_LITERAL;

		char value = source.peek();
		if (value == '0')
		{
			value = source.peek(1);
			if (value == 'b' || value == 'B')
				return processBinary();
			if (value == 'x' || value == 'X')
				return processHexadecimal();
			//return processOctal();
			type = TokenType.TOK_OCT_LITERAL;
		}

		Capture capture = new Capture();

		while (true)
		{
			value = source.peek();
			if (isDigit(value))
			{
				capture.push(value);
				source.next();
			}
			else
				break;
		}

		if (source.peek() == '.')
		{
			if (isDigit(source.peek(1)))
			{
				// we have a floating-point number
				capture.push('.');
				source.next();

				while (true)
				{
					value = source.peek();
					if (isDigit(value))
					{
						capture.push(value);
						source.next();
					}
					else
						break;
				}

				return createToken(TokenType.TOK_FP_LITERAL, capture.toString());
			}
		}

		return createToken(type, capture.toString());
	}

	private Token returnError( String message )
	{
		listener.onError(source.location, message);
		return null;
	}

	private Token processHexadecimal()
	{
		Capture capture = new Capture();
		capture.push("0x");
		source.next(1);

		while (true)
		{
			char value = source.peek(1);
			if ((value >= '0' && value <= '9') ||
				(value >= 'a' && value <= 'f') ||
				(value >= 'A' && value <= 'F'))
			{
				source.next();
				capture.push(value);
			}
			else
				break;
		}

		if (capture.length() > 2)
			return createToken(TokenType.TOK_HEX_LITERAL, capture.toString());
		else
		{
			return returnError("Invalid hexadecimal literal");
		}
	}

	private Token processBinary()
	{
		Capture capture = new Capture();
		capture.push("0b");
		source.next();

		while (true)
		{
			char value = source.peek(1);
			if (value == '0' || value == '1')
			{
				source.next();
				capture.push(value);
			}
			else
				break;
		}

		if (capture.length() > 2)
			return createToken(TokenType.TOK_BIN_LITERAL, capture.toString());
		else
		{
			return returnError("Invalid binary literal");
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
	private Token processIdentifier()
	{
		Capture capture = new Capture();

		capture.push( source.peek() );
		while (true)
		{
			char current = source.peek(1);
			if ((current >= 'A' && current <= 'Z') ||
				(current >= 'a' && current <= 'z') ||
				(current >= '0' && current <= '9') ||
				current == '_')
			{
				capture.push(source.next());
			}
			else
				break;
		}

		return createToken(null, capture.toString());
	}

	@Override
	public String getFileName()
	{
		return source.location.fileName;
	}

	@Override
	public CompilationContext getContext()
	{
		return context;
	}
}
