package beagle.compiler.tree;

import java.io.PrintStream;

public class Modifiers implements IModifiers
{

	private int modifiers;
	
	public Modifiers( int flags )
	{
		this.modifiers = flags;
	}
	
	@Override
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public boolean hasModifier(int flags)
	{
		return (this.modifiers & flags) == flags;		
	}

	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.print("]  ");
		
		if ((modifiers & PUBLIC) != 0)
			out.print("public  ");
		if ((modifiers & PROTECTED) != 0)
			out.print("protected  ");
		if ((modifiers & PRIVATE) != 0)
			out.print("private  ");
		if ((modifiers & CONST) != 0)
			out.print("const  ");
		if ((modifiers & STATIC) != 0)
			out.print("static  ");
		
		out.println();
	}

}
