package beagle.compiler.tree;

import beagle.compiler.TokenType;

public class UnaryExpression extends TreeElement implements IExpression
{

	TokenType operation;

	UnaryDirection direction;

	IExpression expression;

	public UnaryExpression(IExpression expression, TokenType operation)
	{
		this.operation = operation;
		this.expression = expression;
		this.direction = UnaryDirection.POSTFIX;
	}

	public UnaryExpression(TokenType operation, IExpression expression)
	{
		this.operation = operation;
		this.expression = expression;
		this.direction = UnaryDirection.PREFIX;
	}

	public TokenType operation()
	{
		return operation;
	}

	public void operation(TokenType value)
	{
		this.operation = value;
	}

	public UnaryDirection direction()
	{
		return direction;
	}

	public void direction(UnaryDirection value)
	{
		this.direction = value;
	}

	public IExpression expression()
	{
		return expression;
	}

	public void expression(IExpression value)
	{
		this.expression = value;
	}

	public static enum UnaryDirection
	{
		PREFIX,
		POSTFIX
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, expression);
		visitor.finish(this);
	}

}
