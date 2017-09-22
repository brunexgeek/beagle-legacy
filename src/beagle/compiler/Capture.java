package beagle.compiler;

public class Capture
{

	private char[] buffer; 
	
	private int index = 0;
	
	public Capture()
	{
		this(128);
	}
	
	public Capture( int size )
	{
		buffer = new char[size];
	}
	
	public void push( char value )
	{
		if (index < buffer.length)
			buffer[index++] = value;
	}
	
	public void push( String value )
	{
		for (int i = 0; i < value.length(); ++i)
			push(value.charAt(i));
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(buffer, 0, index);
	}
	
	public int toInteger()
	{
		return Integer.valueOf( this.toString() );
	}
	
	public double toDouble()
	{
		return Double.valueOf( this.toString() );
	}

	public int length()
	{
		return index;
	}

	public char peek()
	{
		if (index == 0) return ScanString.BOI;
		return buffer[index - 1];
	}
	
	public void pop()
	{
		if (index > 0) index--;
	}
	
}
