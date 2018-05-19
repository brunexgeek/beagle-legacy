package beagle.compiler.tree;

public abstract class Literal<T> extends TreeElement implements IExpression
{

	protected T value;

	protected TypeReference type;

	public Literal(T value)
	{
		this.value = value;
	}

	public T value()
	{
		return value;
	}

	public void value(T value)
	{
		this.value = value;
	}

	public TypeReference type()
	{
		return this.type;
	}

}
