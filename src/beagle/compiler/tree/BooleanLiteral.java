package beagle.compiler.tree;

public class BooleanLiteral extends Literal<Boolean>
{

	public BooleanLiteral(Boolean value)
	{
		super(value);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

	@Override
	public String toString()
	{
		return Boolean.toString(value);
	}
}
