package beagle.compiler.tree;

public class IfThenElseStmt extends TreeElement implements IStatement
{

	IExpression condition;

	IStatement thenSide;

	IStatement elseSide;

	public IfThenElseStmt(IExpression condition, IStatement thenSide, IStatement elseSide)
	{
		condition(condition);
		thenSide(thenSide);
		elseSide(elseSide);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, condition);
			accept(visitor, thenSide);
			accept(visitor, elseSide);
		}
		visitor.finish(this);
	}

	public IExpression condition()
	{
		return condition;
	}

	public void condition(IExpression condition)
	{
		this.condition = condition;
		if (condition != null) condition.parent(this);
	}

	public IStatement thenSide()
	{
		return thenSide;
	}

	public void thenSide(IStatement thenSide)
	{
		this.thenSide = thenSide;
		if (thenSide != null) thenSide.parent(this);
	}

	public IStatement elseSide()
	{
		return elseSide;
	}

	public void elseSide(IStatement elseSide)
	{
		this.elseSide = elseSide;
		if (elseSide != null) elseSide.parent(this);
	}


}
