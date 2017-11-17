package beagle.compiler.tree;

public class FormalParameter extends TreeElement implements IFormalParameter
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
