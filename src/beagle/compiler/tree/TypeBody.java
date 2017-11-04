package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class TypeBody implements ITypeBody
{

	List<IMethodDeclaration> methods;
	
	List<IFieldDeclaration> fields;
	
	ITypeDeclaration parent;

	public TypeBody()
	{
		this(null);
	}
	
	public TypeBody( ITypeDeclaration parent )
	{
		this.methods = new LinkedList<IMethodDeclaration>();
		this.fields = new LinkedList<IFieldDeclaration>();
		this.parent = parent;
	}
	
	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.println("[Body]");
		for (IMethodDeclaration item : methods)
			item.print(out, level + 1);
	}

	@Override
	public List<IMethodDeclaration> getMethods()
	{
		return methods;
	}

	@Override
	public ITypeDeclaration getParent()
	{
		return parent;
	}
	
	@Override
	public void setParent( ITypeDeclaration parent)
	{
		this.parent = parent;
	}

	@Override
	public List<IFieldDeclaration> getFields()
	{
		return fields;
	}

}
