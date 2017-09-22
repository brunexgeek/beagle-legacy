package beagle.compiler;

public class Token
{

	protected TokenType type;

	protected String value;

	public Token(TokenType type)
	{
		this(type, null);
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
	public Token(String value)
	{
		this.type = TokenType.getType(value);
		this.value = value;
	}

	public Token(TokenType type, String value)
	{
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString()
	{
		if (value != null)
			return "Token [Type: " + type.toString() + ";  Value: " + value + "]";
		else
			return "Token [Type: " + type.toString() + "]";
	}

}
