package beagle.compiler.tree;

public class Annotation extends TreeElement
{

	TypeReference type;

	public Annotation( TypeReference type )
	{
		this.type = type;
	}

	public TypeReference type()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return "@" + type.qualifiedName();
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, type);
		visitor.finish(this);
	}
}
