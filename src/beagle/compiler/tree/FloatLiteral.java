package beagle.compiler.tree;

public class FloatLiteral extends Literal<Float>
{

	public FloatLiteral(Float value)
	{
		super(value);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

	@Override
	public String toString()
	{
		return Float.toString(value);
	}

	@Override
	public TypeReference type()
	{
		// TODO: detect input type
		return TypeReference.FLOAT32;
	}


}
