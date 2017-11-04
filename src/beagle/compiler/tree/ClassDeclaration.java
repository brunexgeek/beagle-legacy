package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.List;

// TODO: rename to TypeDeclaration
public class ClassDeclaration
{
/*
	IModifiers modifiers;

	ITypeReference extended;

	List<ITypeReference> implemented;
	
	ITypeBody body;

	public ClassDeclaration(ICompilationUnit unit, IModifiers modifiers, IName name, ITypeReference extended,
			List<ITypeReference> implemented, ITypeBody body)
	{
		super(unit, name);
		this.modifiers = modifiers;
		this.extended = extended;
		this.implemented = implemented;
		this.body = body;
		
		body.setParent(this);
	}

	public ClassDeclaration(IPackage pack, IName name)
	{
		super(pack, name);
	}

	@Override
	public void print(PrintStream out, int level)
	{
		super.print(out, level);

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

	@Override
	public ITypeBody getBody()
	{
		return body;
	}
*/
}
