package beagle.compiler.tree;

public class FormalParameter extends TreeElement
{

	private TypeReference type;

	private Name name;

	public FormalParameter( Name name, TypeReference type )
	{
		this.type = type;
		this.name = name;
	}

	public Name name()
	{
		return name;
	}

	public void name(Name value)
	{
		this.name = value;
	}

	public TypeReference type()
	{
		return type;
	}

	public void type(TypeReference value)
	{
		this.type = value;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, name);
			accept(visitor, type);
		}
		visitor.finish(this);
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag("FormatParameter", level);
//
//		type.print(out, level + 1);
//		name.print(out, level + 1);
//	}

}
