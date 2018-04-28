package beagle.compiler;

import java.util.List;

import beagle.compiler.tree.Comment;

public class Token
{

	public TokenType type;

	public String value;

	public SourceLocation location;

	public int lineBreak;

	public List<Comment> comments;

	/**
	 *
	 * If {@code type} is null, creates a token infering its type. If the
	 * value matches a known keyword, the token type is set to the
	 * matched keyword type. Otherwise, the type {@link TokenType.TOK_NAME} is
	 * used.
	 *
	 * The parameter {@code value} can be {@code null}.
	 *
	 * @param location
	 * @param lineBreak
	 * @param type
	 * @param value
	 */
	public Token(SourceLocation location, int lineBreak, List<Comment> comments, TokenType type, String value)
	{
		this.location = location.clone();
		this.lineBreak = lineBreak;
		this.comments = comments;
		if (type == null)
			this.type = TokenType.fromString(value);
		else
			this.type = type;
		this.value = value;
	}

	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();

		output.append("Token [Type: ");
		output.append(type.toString());

		if (lineBreak != LineBreak.NONE)
		{
			output.append("   LineBreak: ");
			output.append(lineBreak);
		}

		if (comments != null)
		{
			output.append("   Comments: ");
			output.append(comments.size());
		}

		if (value != null)
		{
			output.append("   Value: '");
			if (value.length() < 12)
				output.append(value);
			else
			{
				output.append( value.substring(0, 9) );
				output.append("...");
			}
			output.append('\'');
		}

		output.append(']');
		return output.toString();
	}

	public static class LineBreak
	{
		public static final int NONE = 0;

		public static final int BEFORE = 2;

		public static final int AFTER = 1;

		public static final int BOTH = 3;
	}

}
