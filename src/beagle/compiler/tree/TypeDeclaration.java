package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.List;

public class TypeDeclaration implements ITypeDeclaration
{

	protected IPackage pack;
	
	protected boolean complete;
	
	protected ICompilationUnit parent;
	
	protected IModifiers modifiers;

	protected IName name;
	
	protected ITypeReference extended;

	protected List<ITypeReference> implemented;
	
	protected ITypeBody body;


	/**
	 * Create a complete type declaration
	 * 
	 * @param unit
	 * @param modifiers
	 * @param name
	 * @param extended
	 * @param implemented
	 * @param body
	 */
	public TypeDeclaration(ICompilationUnit parent, IModifiers modifiers, IName name, ITypeReference extended,
			List<ITypeReference> implemented, ITypeBody body)
	{
		if (name.getCount() > 1)
			throw new IllegalArgumentException("Invalid simple name");
		if (parent == null)
			throw new IllegalArgumentException("Compilation unit can not be null");
		
		this.parent = parent;
		this.name = name;
		this.pack = parent.getPackage();
		this.complete = true;
		this.modifiers = modifiers;
		this.extended = extended;
		this.implemented = implemented;
		this.body = body;
		
		body.setParent(this);
	}

	/**
	 * Create an incomplete type declaration
	 * 
	 * @param pack
	 * @param name
	 */
	public TypeDeclaration(IPackage pack, IName name)
	{
		if (name.getCount() > 1)
			throw new IllegalArgumentException("Invalid simple name");
		if (pack == null)
			throw new IllegalArgumentException("Package can not be null");
		
		this.parent = null;
		this.name = name;
		this.pack = pack;
		this.complete = false;
	}
	
	public TypeDeclaration( ICompilationUnit unit, IName name )
	{

	}
	
	@Override
	public boolean isComplete()
	{
		return complete;
	}

	public void setComplete( boolean state )
	{
		complete = state;
	}
	
	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public ICompilationUnit getCompilationUnit()
	{
		return parent;
	}

	@Override
	public String getQualifiedName()
	{
		return pack.getQualifiedName() + "." + name;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeBody getBody()
	{
		return body;
	}
	
	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.print("]  ");
		Printer.print(out, "name", getQualifiedName());
		Printer.print(out, "isComplete", complete);
		out.println();

		if (modifiers != null)
			modifiers.print(out, level + 1);

		if (extended != null)
		{
			Printer.indent(out, level + 1);
			out.println("[Extends]");
			extended.print(out, level + 2);
		}

		if (implemented != null)
		{
			Printer.indent(out, level + 1);
			out.println("[Implements]");
			for (ITypeReference item : implemented)
				item.print(out, level + 2);
		}
		
		body.print(out, level + 1);
	}

}
