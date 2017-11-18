package beagle.compiler.tree;

public class Annotation extends TreeElement implements IAnnotation
{

	ITypeReference type;

	public Annotation( ITypeReference type )
	{
		this.type = type;
	}

	@Override
	public ITypeReference type()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return "@" + type.getQualifiedName();
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, type);
		visitor.finish(this);
	}
}
