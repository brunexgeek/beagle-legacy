package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.List;

public class MethodDeclaration implements IMethodDeclaration
{

	ITypeReference type;

	List<IFormalParameter> parameters;

	IMethodBody body;

	IName name;

	IModifiers modifiers;

	ITypeBody parent;

	List<IAnnotation> annotations;

	public MethodDeclaration(List<IAnnotation> annots, ITypeReference type, IName name, List<IFormalParameter> parameters, IMethodBody body)
	{
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
		this.modifiers = modifiers;
	}

	@Override
	public ITypeReference getReturnType()
	{
		return type;
	}

	@Override
	public List<IFormalParameter> getParameters()
	{
		return parameters;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public IMethodBody getBody()
	{
		return body;
	}

	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.print('[');
		out.print(getClass().getSimpleName());
		out.print("]  ");
		Printer.print(out, "isContructor", isContructor());
		out.println();

		if (modifiers != null)
			modifiers.print(out, level + 1);
		type.print(out, level + 1);
		name.print(out, level + 1);

		Printer.indent(out, level + 1);
		out.println("[FormalParameterList]");
		for (IFormalParameter param : parameters)
			param.print(out, level + 2);
	}

	@Override
	public boolean isContructor()
	{
		return (name.equals(type.getName()));
	}

	@Override
	public IModifiers getModifiers()
	{
		return modifiers;
	}

	@Override
	public ITypeBody getParent()
	{
		return parent;
	}

	@Override
	public void setParent(ITypeBody parent)
	{
		this.parent = parent;
	}

}
