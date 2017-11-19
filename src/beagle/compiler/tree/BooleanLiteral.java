package beagle.compiler.tree;

public class BooleanLiteral extends TreeElement implements IBooleanLiteral
{

	protected boolean value;

	public BooleanLiteral(boolean value)
	{
		this.value = value;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

	@Override
	public boolean value()
	{
		return value;
	}

	@Override
	public void value(boolean value)
	{
		this.value = value;
	}

}
