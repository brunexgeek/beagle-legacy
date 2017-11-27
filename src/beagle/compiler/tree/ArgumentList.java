package beagle.compiler.tree;

public class ArgumentList extends TreeElementList<Argument> implements IExpression
{
	private static final long serialVersionUID = 3159461383416047866L;

	public ArgumentList()
	{
		super();
	}

	public ArgumentList( Argument value )
	{
		super();
		add(value);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (Argument item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
