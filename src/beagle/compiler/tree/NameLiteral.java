package beagle.compiler.tree;

public class NameLiteral extends Literal<Name>
{

	public NameLiteral(Name value)
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
		return value.qualifiedName();
	}
}
