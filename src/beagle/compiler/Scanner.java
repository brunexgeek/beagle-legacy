package beagle.compiler;

/**
 * Extract tokens from the input source code.
 */
public class Scanner implements IScanner
{
	
	protected ScanString source;
	
	private CompilationListener listener; 
	
	private CompilationContext context;
		
	Scanner( CompilationContext context, ScanString source )
	{
		this.source = source;
		this.context = context;
		this.listener = context.listener;
	}
	
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
						return TokenType.TOK_DIV_ASSIGN.createToken();
					
					return TokenType.TOK_DIV.createToken();
				case '"':
				case '\'':
					return processString();
				case '=':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_EQ.createToken();
					}
					return TokenType.TOK_ASSIGN.createToken();
				case '+':
					if (source.peek(1) == '+')
					{
						source.next();
						return TokenType.TOK_INC.createToken();
					}
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_PLUS_ASSIGN.createToken();
					}
					if (isDigit(source.peek(1)))
					{
						return processNumber();
					}
					return TokenType.TOK_PLUS.createToken();
				case '-':
					if (source.peek(1) == '-')
					{
						source.next();
						return TokenType.TOK_DEC.createToken();
					}
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_MINUS_ASSIGN.createToken();
					}
					if (isDigit(source.peek(1)))
					{
						return processNumber();
					}
					return TokenType.TOK_MINUS.createToken();
				case '*':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_MUL_ASSIGN.createToken();
					}
					return TokenType.TOK_MUL.createToken();
				case '%':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_MOD_ASSIGN.createToken();
					}
					return TokenType.TOK_MOD.createToken();
				case '&':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_BAND_ASSIGN.createToken();
					}
					if (source.peek(1) == '&')
					{
						source.next();
						return TokenType.TOK_AND.createToken();
					}
					return TokenType.TOK_BAND.createToken();
				case '|':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_BOR_ASSIGN.createToken();
					}
					if (source.peek(1) == '|')
					{
						source.next();
						return TokenType.TOK_OR.createToken();
					}
					return TokenType.TOK_BOR.createToken();
				case '.':
					return TokenType.TOK_DOT.createToken();
				case '\\':
					return TokenType.TOK_BACK_SLASH.createToken();
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
					return TokenType.TOK_LEFT_PAR.createToken();
				case ')':
					return TokenType.TOK_RIGHT_PAR.createToken();
				case '[':
					return TokenType.TOK_LEFT_BRACKET.createToken();
				case ']':
					return TokenType.TOK_RIGHT_BRACKET.createToken();
				case '{':
					return TokenType.TOK_LEFT_BRACE.createToken();
				case '}':
					return TokenType.TOK_RIGHT_BRACE.createToken();
				case '@':
					return TokenType.TOK_AT.createToken();
				case '>':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_GREATER_THAN.createToken();
					}
					if (source.peek(1) == '>')
					{
						source.next();
						if (source.peek(1) == '=')
						{
							source.next();
							return TokenType.TOK_SHR_ASSIGN.createToken();
						}
						else
							return TokenType.TOK_SHR.createToken();
					}
					return TokenType.TOK_GREATER_THAN.createToken();
				case '<':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_LESS_THAN.createToken();
					}
					if (source.peek(1) == '<')
					{
						source.next();
						if (source.peek(1) == '=')
						{
							source.next();
							return TokenType.TOK_SHL_ASSIGN.createToken();
						}
						else
							return TokenType.TOK_SHL.createToken();
					}
					return TokenType.TOK_LESS_THAN.createToken();
					
				case ',':
					return TokenType.TOK_COMA.createToken();
					
				case ';':
					return TokenType.TOK_SEMICOLON.createToken();
					
				case ':':
					return TokenType.TOK_COLON.createToken();
					
				case '?':
					return TokenType.TOK_QUEST.createToken();
					
				case '!':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_NE.createToken();
					}
					return TokenType.TOK_BANG.createToken();
					
				case '~':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_NEG_ASSIGN.createToken();
					}
					return TokenType.TOK_TILDE.createToken();
					
				case '^':
					if (source.peek(1) == '=')
					{
						source.next();
						return TokenType.TOK_XOR_ASSIGN.createToken();
					}
					return TokenType.TOK_XOR.createToken();
					
				case '\n':
					if (last == '\n') break;
					return TokenType.TOK_EOL.createToken();

				case ScanString.BOL:
					break;
				
				case ScanString.EOI:
					return TokenType.TOK_EOF.createToken();
					
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
			return new Token(TokenType.TOK_STRING_LITERAL, capture.toString());
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
			return new Token(type, capture.toString());
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
		return new Token(TokenType.TOK_COMMENT, capture.toString());
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
				
				return new Token(TokenType.TOK_FP_LITERAL, capture.toString());
			}
		}
		
		return new Token(type, capture.toString());
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
			return new Token(TokenType.TOK_HEX_LITERAL, capture.toString());
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
			return new Token(TokenType.TOK_BIN_LITERAL, capture.toString());
		else
		{
			return returnError("Invalid binary literal");
		}
	}
	
	/**
	 * Capture an identifier.
	 * 
	 * The current character is guaranted to be valid.
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
		
		return new Token(capture.toString());
	}

	@Override
	public String getFileName()
	{
		return source.location.fileName;
	}

	public CompilationContext getContext()
	{
		return context;
	}
}
