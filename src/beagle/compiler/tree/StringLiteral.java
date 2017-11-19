package beagle.compiler.tree;

public class StringLiteral extends TreeElement implements IStringLiteral
{

	protected String value;

	public StringLiteral(String value)
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
	public String value()
	{
		return value;
	}

	@Override
	public void value(String value)
	{
		this.value = value;
	}

}
