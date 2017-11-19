package beagle.compiler.tree;

public class IntegerLiteral extends Literal<Long>
{

	public IntegerLiteral(Long value)
	{
		super(value);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

}
