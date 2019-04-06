namespace beagle.compiler {

export class SourceLocation
{
	line : number;
	column : number;
}

export enum LookaheadStatus
{
	MATCH,
	NO_MATCH,
	EOI
}

export class ScanString
{

	/**
	 * Begin of input.
	 */
	static BOI : string = '\x01';

	/**
	 * End of input.
	 */
	static EOI : string = '\x04';

	/**
	 * Begin of line.
	 */
	static BOL : string = '\x02';

	/**
	 * End of line.
	 */
	static EOL : string = '\x0A';

	/**
	 * The input buffer.
	 */
	buffer : number[] = [];

	/**
	 * Index of next character to be read
	 */
	index : number;

	bufferSize : number;

	context : CompilationContext;

	location : SourceLocation;

	/**
	 * Create a scanner from the input array.
	 */
	constructor(context : CompilationContext, fileName : string, content : string)
	{
		this.context = context;
		this.location = new SourceLocation();
		this.location.column = 1;
		this.location.line = 1;
		//unescape(input);

        this.preprocess(content);
		this.index = -1;
	}

	/**
	 * Cleanup the input source code and ensures every line have its markers ({@link BOL} and {@link EOL}).
	 * @param content
	 * @return
	 */
	preprocess(content : string)
	{
		let total = content.length;

		// discard every EOL at the end of the input
		while (total > 0 && content[total-1] == '\n') --total;

		// check if the content is empty
		if (total == 0)
		{
            this.buffer.length = 2;
            this.buffer[0] = ScanString.EOL.charCodeAt(0);
            this.buffer[1] = ScanString.EOI.charCodeAt(0);
			return;
		}

        // ensure space for ending EOL+EOI markers
        this.buffer.length = content.length + 2;

        let i = 0, j = 0;

		while (i < total)
		{
			let value = content[i];

			// ignores most control characters
			if (value != '\t' && value != '\n' && value < ' ')
				this.buffer[j] = 0x32;
			else
				this.buffer[j] = value.charCodeAt(0);

			++j;
			++i;
		}
		this.buffer[j++] = 0x0A;
        this.buffer[j] = ScanString.EOI.charCodeAt(0);
        this.bufferSize = j++;
	}

	/**
	 * Returns the current character, but do not advances.
	 * @return
	 */
	peek() : string
	{
		return this.peekAt(0);
	}

	/**
	 * Returns some future character, but do not advances.
	 * @return
	 */
	peekAt( offset : number ) : string
	{
		let i = this.index + offset;
		if (i < 0)
			return ScanString.BOI;
		if (i < this.bufferSize)
			return String.fromCharCode(this.buffer[this.index + offset]);
		else
			return ScanString.EOI;
	}

	lookahead( ...values : string[] ) : LookaheadStatus
	{
		if (this.index + 1 >= this.bufferSize)
			return LookaheadStatus.EOI;
		if (this.index + values.length >= this.bufferSize)
			return LookaheadStatus.NO_MATCH;

		for (let i = 0; i < values.length; ++i)
		{
			if (this.buffer[this.index + i] != values[i].charCodeAt(0))
				return LookaheadStatus.NO_MATCH;
		}
		return LookaheadStatus.MATCH;
	}

	push(value : number) : boolean
	{
		if (this.index > 0)
		{
			this.index--;
            this.buffer[this.index] = value;
            return true;
		}
		else
			return false;
	}

	next() : string
	{
		return this.nextAt(1);
	}

	nextAt( count : number ) : string
	{
		if (count <= 0) return this.peek();

		while(count > 0 && this.index + 1 < this.bufferSize)
		{
			++this.index;
			--count;
			this.update(this.buffer[this.index]);
        }

        if (count > 0)
        {
            this.index++;
            return ScanString.EOI;
        }
		return String.fromCharCode(this.buffer[this.index]);
    }

    hasNext() : boolean
    {
        return this.index + 1 < this.bufferSize;
    }

/*
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

    private int hexDigit(char value)
    {
        if (value >= '0' && value <= '9')
        	return value - '0';
    	if (value >= 'a' && value <= 'f')
    		return value - 'a' + 10;
    	if (value >= 'A' && value <= 'F')
        	return value - 'A' + 10;
    	throw new NumberFormatException("'" + value + "' is not a valid hexadecimal digit");
    }
*/

	public update(value : number)
	{
		if (value == 0x0A)
		{
			++this.location.line;
			this.location.column = 1;
		} else
		{
			++this.location.column;
		}
	}
}

}
/*
let scanner = new beagle.compiler.ScanString("bla.bgl", "hello world!");
let body = document.getElementsByTagName("body")[0];
while (scanner.hasNext())
{
    let tmp = document.createElement("span");
    tmp.innerText = scanner.next();
    body.appendChild(tmp);
}
*/