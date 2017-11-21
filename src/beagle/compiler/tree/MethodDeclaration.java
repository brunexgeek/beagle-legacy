package beagle.compiler.tree;

public class MethodDeclaration extends TreeElement implements IMethodDeclaration
{

	IAnnotationList annotations;

	IModifiers modifiers;

	IName name;

	IFormalParameterList parameters;

	ITypeReference type;

	IBlock body;

	ITypeBody parent;

	public MethodDeclaration(IAnnotationList annots, ITypeReference type, IName name, IFormalParameterList parameters, IBlock body)
	{
		this.annotations = annots;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
		this.modifiers = null;
	}

	@Override
	public ITypeReference returnType()
	{
		return type;
	}

	@Override
	public void returnType(ITypeReference value)
	{
		this.type = value;
	}

	@Override
	public IFormalParameterList parameters()
	{
		return parameters;
	}

	@Override
	public IName name()
	{
		return name;
	}

	@Override
	public void name(IName value)
	{
		this.name = value;
	}

	@Override
	public IBlock body()
	{
		return body;
	}

	@Override
	public void body(IBlock value)
	{
		this.body = value;
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag(getClass().getSimpleName(), level);
//		out.printAttribute("isContructor", isContructor());
//		out.println();
//
//		if (modifiers != null)
//			modifiers.print(out, level + 1);
//		type.print(out, level + 1);
//		name.print(out, level + 1);
//
//		out.printTag("[FormalParameterList]", level + 1);
//		for (IFormalParameter param : parameters)
//			param.print(out, level + 2);
//	}

	@Override
	public boolean isContructor()
	{
		return (name.equals(type.getName()));
	}

	@Override
	public IModifiers modifiers()
	{
		return modifiers;
	}

	@Override
	public void modifiers(IModifiers value)
	{
		this.modifiers = value;
	}

	@Override
	public ITypeBody parent()
	{
		return parent;
	}

	@Override
	public void parent(ITypeBody parent)
	{
		this.parent = parent;
	}

	@Override
	public IAnnotationList annotations()
	{
		return annotations;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, annotations);
			accept(visitor, modifiers);
			accept(visitor, name);
			accept(visitor, parameters);
			accept(visitor, type);
			accept(visitor, body);
		}
		visitor.finish(this);
	}

}
