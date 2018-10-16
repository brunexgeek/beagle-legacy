package beagle.compiler.tree;

public class ExpressionStmt extends TreeElement implements IStatement
{

	IExpression expression;

	public ExpressionStmt(IExpression expression)
	{
		this.expression = expression;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, expression);
		visitor.finish(this);
	}

	public IExpression expression()
	{
		return expression;
	}

	public void expression(IExpression value)
	{
		this.expression = value;
	}

}
