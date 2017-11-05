package beagle.compiler;


/**
 * Ring array of tokens used to get tokens from input data
 * and to perform lookahead searchs.
 */
public class TokenArray
{

	private IScanner scanner;

	Token[] buffer;

	int current;

	int size;

	public TokenArray(IScanner scanner)
	{
		this(scanner, 16);
	}

	public TokenArray( IScanner scanner, int size)
	{
		current = 0;
		this.size = size;
		buffer = new Token[size];
		this.scanner = scanner;

		// fill the ring array with tokens
		for (int i = 0; i < size; ++i)
			if ((buffer[i] = scanner.readToken()) == null)
				break;
	}

	public Token pull()
	{
		if (buffer[current] != null)
		{
			Token value = buffer[current];
			buffer[current] = scanner.readToken();
			current = (current + 1) % size;
			return value;
		}
		return null;
	}

	public Token peek()
	{
		return peek(0);
	}

	public Token peek( int index )
	{
		int pos = (current + index) % size;
		return buffer[pos];
	}

	public TokenType peekType()
	{
		return peekType(0);
	}

	public TokenType peekType( int index )
	{
		Token current = peek(index);
		if (current == null)
			return null;
		else
			return current.type;
	}

	/**
	 * Check if the next tokens in the input sequence corresponds to the given ones.
	 *
	 * If isRequired is true and the lookahead fails, this function send a
	 * compilation error to the current listener before returns.
	 *
	 * @param types
	 * @return
	 */
	public boolean lookahead( boolean isRequired, TokenType... types )
	{
		if (types.length == 0)
			return false;
		if (types.length >= size)
			throw new IndexOutOfBoundsException();

		Token first = buffer[current];
		int count = types.length;

		for (int i = 0; i < count; ++i)
		{
			int pos = (current + i) % size;
			Token entry = buffer[pos];
			if (i != 0 && entry == first)
				return false;

			if (entry == null || entry.type != types[i])
				return false;
		}
		return true;
	}

	public boolean lookahead( TokenType... types )
	{
		return lookahead(false, types);
	}

	public void discard()
	{
		pull();
	}

	public void discard( int count )
	{
		for (int i = 0; i < count; ++i)
			pull();
	}

	public boolean discard( TokenType type )
	{
		Token current = peek();
		if (current != null && current.type == type)
		{
			pull();
			return true;
		}
		else
		{
			scanner.getContext().getListener().onError(null, "Syntax error, expected '" + type+ "'");
			return false;
		}
	}

	@Override
	public String toString()
	{
		return peek() + "";
	}
}
