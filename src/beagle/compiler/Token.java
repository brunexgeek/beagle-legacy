package beagle.compiler;

public class Token
{

	protected TokenType type;

	protected String value;
	
	protected SourceLocation location;

	public Token(SourceLocation location, TokenType type)
	{
		this(location, type, null);
	}

	/**
	 * Creates a token infering its type.
	 * 
	 * If the value matches a known keyword, the token type is set to the
	 * matched keyword type. Otherwise, the type {@link TokenType.TOK_NAME} is
	 * used.
	 * 
	 * @param value
	 */
	public Token(SourceLocation location, String value)
	{
		this.location = location.clone();
		this.type = TokenType.getType(value);
		this.value = value;
	}

	public Token(SourceLocation location, TokenType type, String value)
	{
		this.location = location.clone();
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString()
	{
		if (value != null)
			return "Token [Type: " + type.toString() + "  Value: " + value + "]";
		else
			return "Token [Type: " + type.toString() + "]";
	}

}
