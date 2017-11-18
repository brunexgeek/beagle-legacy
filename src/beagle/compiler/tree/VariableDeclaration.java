package beagle.compiler.tree;

public class VariableDeclaration extends TreeElement implements IVariableDeclaration
{

	protected IModifiers modifiers;

	protected ITypeReference type;

	protected IName name;

	protected IAnnotationList annotations;

	protected ITypeBody parent;

	public VariableDeclaration( IAnnotationList annotations, ITypeReference type, IName name )
	{
		this.annotations = annotations;
		this.modifiers = null;
		this.type = type;
		this.name = name;
	}

	public VariableDeclaration( IAnnotationList annotations, ITypeReference type )
	{
		this(annotations,  type, null);
	}

	@Override
	public IModifiers getModifiers()
	{
		return modifiers;
	}

	@Override
	public ITypeReference getType()
	{
		return type;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeBody getParent()
	{
		return parent;
	}

	@Override
	public void setParent(ITypeBody parent)
	{
		this.parent = parent;
	}

	@Override
	public IAnnotationList getAnnotations()
	{
		return annotations;
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
		}
		visitor.finish(this);
	}

}
