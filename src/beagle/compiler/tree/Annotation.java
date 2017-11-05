package beagle.compiler.tree;

public class Annotation implements IAnnotation
{

	ITypeReference type;

	public Annotation( ITypeReference type )
	{
		this.type = type;
	}

	@Override
	public ITypeReference getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return "@" + type.getQualifiedName();
	}
}
