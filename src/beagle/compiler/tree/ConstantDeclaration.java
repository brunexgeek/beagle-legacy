package beagle.compiler.tree;

public class ConstantDeclaration extends StorageDeclaration
{

	public ConstantDeclaration( AnnotationList annotations, Name name, TypeReference type, IExpression initializer)
	{
		super(annotations,  name, type, initializer);
	}

	public ConstantDeclaration( AnnotationList annotations, Name name, IExpression initializer )
	{
		super(annotations,  name, null, initializer);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, annotations);
			accept(visitor, modifiers);
			accept(visitor, name);
			accept(visitor, type);
			accept(visitor, initializer);
		}
		visitor.finish(this);
	}

}
