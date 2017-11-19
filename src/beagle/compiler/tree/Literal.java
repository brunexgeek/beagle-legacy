package beagle.compiler.tree;

public abstract class Literal<T> extends TreeElement implements ILiteral<T>
{

	protected T value;

	public Literal(T value)
	{
		this.value = value;
	}

	@Override
	public T value()
	{
		return value;
	}

	@Override
	public void value(T value)
	{
		this.value = value;
	}


}
