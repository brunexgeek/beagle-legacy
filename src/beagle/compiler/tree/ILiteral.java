package beagle.compiler.tree;

public interface ILiteral<T> extends IExpression
{

	T value();

	void value(T value);

}