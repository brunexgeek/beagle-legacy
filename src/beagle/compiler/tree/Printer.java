package beagle.compiler.tree;

import java.io.PrintStream;

public class Printer
{

	private Printer()
	{
	}
	
	public static void indent( PrintStream out, int level )
	{
		for (int i = 0; i < level; ++i)
			out.print("   ");
	}
	
	public static void print( PrintStream out, String key, String value )
	{
		out.print(key);
		out.print("='");
		out.print(value);
		out.print("'  ");
	}

	public static void print( PrintStream out, String key, long value )
	{
		out.print(key);
		out.print("=");
		out.print(value);
		out.print("  ");
	}
	
	public static void print( PrintStream out, String key, boolean value )
	{
		out.print(key);
		out.print("=");
		out.print(value);
		out.print("  ");
	}
	
}
