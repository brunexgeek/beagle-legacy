package beagle.compiler.tree;

public class ConstantDeclaration extends StorageDeclaration implements IConstantDeclaration
{

	public ConstantDeclaration( IAnnotationList annotations, IName name, ITypeReference type, IExpression initializer)
	{
		super(annotations,  name, type, initializer);
	}

	public ConstantDeclaration( IAnnotationList annotations, IName name, IExpression initializer )
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
