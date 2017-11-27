package beagle.compiler.tree;

public class Argument extends TreeElement implements ITreeElement
{

	IName name;

	IExpression value;

	public Argument(IName name, IExpression value)
	{
		this.name = name;
		this.value = value;
	}

	public IName name()
	{
		return name;
	}

	public void name(IName name)
	{
		this.name = name;
	}

	public IExpression value()
	{
		return value;
	}

	public void value(IExpression value)
	{
		this.value = value;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, name);
			accept(visitor, value);
		}
		visitor.finish(this);
	}

}
