package beagle.compiler.tree;

import beagle.compiler.TokenType;

public class BinaryExpression extends TreeElement implements IExpression
{

	protected IExpression left, right;

	protected TokenType operation;

	public BinaryExpression(IExpression left, TokenType operation, IExpression right)
	{
		this.left = left;
		this.operation = operation;
		this.right = right;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, left);
			accept(visitor, right);
		}
		visitor.finish(this);
	}

	public IExpression left()
	{
		return left;
	}

	public void left(IExpression value)
	{
		this.left = value;
	}

	public TokenType operation()
	{
		return operation;
	}

	public void operation(TokenType value)
	{
		this.operation = value;
	}

	public IExpression right()
	{
		return right;
	}

	public void right(IExpression value)
	{
		this.right = value;
	}

}
