package beagle.compiler.tree;

public class Argument extends TreeElement implements ITreeElement
{

	Name name;

	IExpression value;

	public Argument(Name name, IExpression value)
	{
		this.name = name;
		this.value = value;
	}

	public Name name()
	{
		return name;
	}

	public void name(Name name)
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
