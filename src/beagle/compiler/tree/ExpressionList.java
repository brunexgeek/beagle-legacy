package beagle.compiler.tree;

public class ExpressionList extends TreeElementList<IExpression> implements IExpression
{

	private static final long serialVersionUID = -7528096390537975721L;

	public ExpressionList()
	{
		super();
	}

	public ExpressionList(IExpression... items)
	{
		super();
		for (IExpression item : items)
			add(item);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (IExpression item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
