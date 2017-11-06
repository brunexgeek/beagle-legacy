package beagle.compiler;

public class Token
{

	protected TokenType type;

	protected String value;

	protected SourceLocation location;

	protected LineBreak lineBreak;

	public Token(SourceLocation location, LineBreak lineBreak, TokenType type)
	{
		this(location, lineBreak, type, null);
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
	public Token(SourceLocation location, LineBreak lineBreak, String value)
	{
		this.location = location.clone();
		this.lineBreak = lineBreak;
		this.type = TokenType.getType(value);
		this.value = value;
	}

	public Token(SourceLocation location, LineBreak lineBreak, TokenType type, String value)
	{
		this.location = location.clone();
		this.lineBreak = lineBreak;
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString()
	{
		String output = "Token [Type: " + type.toString() + "   LineBreak: " + lineBreak;
		if (value != null)
			output += "  Value: " + value;
		return output + "]";
	}

	public static enum LineBreak
	{
		NONE,
		BEFORE,
		AFTER,
		BOTH
	}

}
