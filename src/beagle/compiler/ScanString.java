package beagle.compiler;

/**
 * Class used by {@link Scanner} to retrieve characters from the input source code.
 */
public class ScanString
{

	/**
	 * Begin of input.
	 */
	public static final char BOI = 0x01;
	
	/**
	 * End of input.
	 */
	public static final char EOI = 0x04;
	
	/**
	 * Begin of line.
	 */
	public static final char BOL = 0x02;
	
	/**
	 * End of line.
	 */
	public static final char EOL = 0x0A;
		
	/**
	 * The input buffer.
	 */
	protected char[] buffer;

	/**
	 * Index of next character to be read
	 */
	protected int index;

	protected int bufferSize;

	protected CompilationListener listener;
	
	protected SourceLocation location;
	
	/**
	 * Create a scanner from the input array. 
	 */
	public ScanString(String fileName, char[] content)
	{
		location = new SourceLocation(fileName);
		//unescape(input);
		
		buffer = preprocess(content);
		bufferSize = buffer.length;
		index = -1;
	}
	
	public ScanString(String fileName, String input)
	{
		this(fileName, input.toCharArray());
	}

	/**
	 * Cleanup the input source code and ensures every line have its markers ({@link BOL} and {@link EOL}).
	 * @param content
	 * @return
	 */
	private static char[] preprocess(char[] content)
	{
		int lines = 0;
		int total = content.length;
		
		// discard every EOL at the end of the input
		while (total > 0 && content[total-1] == '\n')
			--total;
		
		// check if the content is empty
		if (total == 0)
		{
			char[] buffer = new char[2];
			buffer[0] = EOL;
			buffer[1] = EOI;
			return buffer;
		}

		// computes the amount of lines
		for (int i = 0; i < total; ++i)
			if (content[i] == '\n') ++lines;
		lines++;
		
		// ensure space for ending EOL+EOI markers
		char[] buffer = new char[total + 2];
		
		int i = 0, j = 0;
		
		while (i < total)
		{
			char value = content[i];
			
			// ignores most control characters
			if (value != '\t' && value != '\n' && value < ' ')
				buffer[j] = ' ';
			else
				buffer[j] = value;
		
			++j;
			++i;
		}
		buffer[j++] = '\n';
		buffer[j] = EOI;
	
		return buffer;
	}

	/**
	 * Returns the next character.
	 * 
	 * <p>This is equivalent to call:
	 * 
	 * <pre>
	 * {@code
	 * next();
	 * char value = peek();
	 * }
	 * </pre>
	 * 
	 * @return
	 */
	protected char pull()
	{
		if (index < bufferSize - 1)
			return location.update(buffer[++index]);
		else
			return EOI;
	}
	
	/**
	 * Returns the current character, but do not advances.
	 * @return
	 */
	protected char peek()
	{
		if (index < 0)
			return BOI;
		else
			return buffer[index];
	}
	
	/**
	 * Returns some future character, but do not advances.
	 * @return
	 */
	protected char peek( int offset )
	{
		int i = this.index + offset;
		if (i < 0)
			return BOI;
		if (i < bufferSize)
			return buffer[this.index + offset];
		else
			return EOI;
	}
	
	protected LookaheadStatus lookahead( char... values )
	{
		if (index >= bufferSize - 1)
			return LookaheadStatus.EOI;
		if (index + values.length >= bufferSize)
			return LookaheadStatus.NO_MATCH;
		
		for (int i = 0; i < values.length; ++i)
		{
			if (buffer[index + i] != values[i]) 
				return LookaheadStatus.NO_MATCH;
		}
		return LookaheadStatus.MATCH;			
	}
	
	protected void push(char value)
	{
		if (index > 0)
		{
			index--;
			buffer[index] = value;
		}
		else
			throw new IndexOutOfBoundsException("Not enough space to push a character");
	}

	protected char next()
	{
		return next(1);
	}
	
	protected char next( int count )
	{
		if (count <= 0) return peek();
		if (index + count < bufferSize)
			index += count;
		else
			index = bufferSize - 1;
		return peek();
	}
    
	protected char previous()
	{
		return previous(1);
	}
	
	protected char previous( int count )
	{
		if (count <= 0) return peek();
		if (index - count >= 0)
			index -= count;
		else
			index = 0;
		return peek();
	}
	
    public void unescape( char[] data )
    {
		Integer i = new Integer(0);
		int j = 0;
		while (i < data.length)
		{
			if (data[i] == '\\' && i + 1 < data.length && data[i + 1] == 'u')
			{
				data[j++] = unescape(data, i);
				continue;
			}
			if (j != i)
			{
				data[j++] = data[i++];
			}
		}
    }

	private char unescape( char[] data, Integer offset )
	{
		try
		{
			while (offset < data.length && data[offset] == 'u')
				offset++;
	
			int end = offset + 3;
			if (end < data.length)
			{
				int d = hexDigit(data[offset]);
				int code = d;
				while (offset < end && d >= 0)
				{
					offset++;
					d = hexDigit(data[offset]);
					code = (code << 4) + d;
				}
				if (code >= 0)
					return (char) code;
			}
		} catch (NumberFormatException ex)
		{
		}
		return ' ';
	}
    
    private int hexDigit(char value) {
        if (value >= '0' && value <= '9')
        	return value - '0';
    	if (value >= 'a' && value <= 'f')
    		return value - 'a' + 10;
    	if (value >= 'A' && value <= 'F')
        	return value - 'A' + 10;
    	throw new NumberFormatException("'" + value + "' is not a valid hexadecimal digit");
    }

    @Override
    public String toString()
    {
    	return peek() + "";
    }
}
