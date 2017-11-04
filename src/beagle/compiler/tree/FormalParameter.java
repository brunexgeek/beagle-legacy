package beagle.compiler.tree;

import java.io.PrintStream;

public class FormalParameter implements IFormalParameter
{

	private ITypeReference type;
	
	private IName name;
	
	public FormalParameter( IName name, ITypeReference type )
	{
		this.type = type;
		this.name = name;
	}
	
	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeReference getType()
	{
		return type;
	}

	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.println("[FormatParameter]");
		
		type.print(out, level + 1);
		name.print(out, level + 1);
	}

}
