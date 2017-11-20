package beagle.compiler.tree;

public class NameLiteral extends Literal<IName>
{

	public NameLiteral(IName value)
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
		return value.getQualifiedName();
	}
}
