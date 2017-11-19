package beagle.compiler.tree;

public class AtomicExpression extends Literal<IExpression>
{

	public AtomicExpression(IExpression value)
	{
		super(value);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, value);
		visitor.finish(this);
	}

}
