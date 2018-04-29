package beagle.compiler.tree;

public class Function extends TreeElement
{

	AnnotationList annotations;

	Modifiers modifiers;

	Name name;

	FormalParameterList parameters;

	TypeReference type;

	Block body;

	TreeElement parent;

	public Function(AnnotationList annots, TypeReference type, Name name, FormalParameterList parameters, Block body)
	{
		this.annotations = annots;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
		this.modifiers = null;
	}


	public TypeReference returnType()
	{
		return type;
	}

	public void returnType(TypeReference value)
	{
		this.type = value;
	}

	public FormalParameterList parameters()
	{
		return parameters;
	}

	public Name name()
	{
		return name;
	}

	public void name(Name value)
	{
		this.name = value;
	}

	public Block body()
	{
		return body;
	}

	public void body(Block value)
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


	public boolean isContructor()
	{
		return (name.equals(type.name()));
	}

	public Modifiers modifiers()
	{
		return modifiers;
	}

	public void modifiers(Modifiers value)
	{
		this.modifiers = value;
	}

	@Override
	public TreeElement parent()
	{
		return parent;
	}

	public void parent(TypeBody parent)
	{
		this.parent = parent;
	}

	public AnnotationList annotations()
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
