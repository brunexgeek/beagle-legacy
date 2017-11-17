package beagle.compiler.tree;

import java.io.OutputStream;
import java.io.PrintStream;

public class Printer extends PrintStream
{

	public Printer( OutputStream stream )
	{
		super(stream);
	}

	public void indent( int level )
	{
		for (int i = 0; i < level; ++i)
			append("   ");
		flush();
	}

	/**
	 * Print a tag.
	 *
	 * @param name
	 * @param level
	 */
	void printTag( String name, int level )
	{
		indent(level);
		append('[');
		append(name);
		append("]  ");
		flush();
	}

	public void printAttribute( String key, String value )
	{
		append(key);
		append("='");
		append(value);
		append("'  ");
		flush();
	}

	public void printAttribute( String key, long value )
	{
		append(key);
		append("=");
		append(Long.toString(value));
		append("  ");
		flush();
	}

	public void printAttribute( String key, boolean value )
	{
		append(key);
		append("=");
		append(Boolean.toString(value));
		append("  ");
		flush();
	}

}
