package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

public class Package implements IPackage
{

	private IName name;
	
	private HashMap<String, ITypeDeclaration> types;
	
	public Package( IName name )
	{
		this.name = name;
		types = new HashMap<String, ITypeDeclaration>();
	}
	
	@Override
	public String getQualifiedName()
	{
		return name.getQualifiedName();
	}

	@Override
	public Set<String> getTypeNames()
	{
		return types.keySet();
	}

	@Override
	public ITypeDeclaration getType( String name )
	{
		return types.get(name); 
	}
	
	public void addType( ITypeDeclaration type )
	{
		types.put(type.getQualifiedName(), type);
	}
	
	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "]";
	}
	
	@Override
	public void print( PrintStream out, int level )
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.print("]  ");
		Printer.print(out, "name", name.getQualifiedName());
		out.println();
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeDeclaration getType(IName name)
	{
		return getType(name.getQualifiedName());
	}
	
}
