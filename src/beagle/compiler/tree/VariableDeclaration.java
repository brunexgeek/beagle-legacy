package beagle.compiler.tree;

public class VariableDeclaration extends StorageDeclaration implements IVariableDeclaration
{

	public VariableDeclaration( IAnnotationList annotations, IName name, ITypeReference type, IExpression initializer)
	{
		super(annotations,  name, type, initializer);
	}

	public VariableDeclaration( IAnnotationList annotations, IName name, IExpression initializer )
	{
		super(annotations,  name, null, initializer);
	}

	public VariableDeclaration( IAnnotationList annotations, IName name, ITypeReference type )
	{
		super(annotations,  name, type, null);
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
