package beagle.compiler.tree;

public class ConstantDeclaration extends TreeElement implements IConstantDeclaration
{

	protected IAnnotationList annotations;

	protected IModifiers modifiers;

	protected IName name;

	protected ITypeReference type;

	protected ITypeBody parent;

	public ConstantDeclaration( IAnnotationList annotations, ITypeReference type, IName name )
	{
		this.annotations = annotations;
		this.modifiers = null;
		this.type = type;
		this.name = name;
	}

	public ConstantDeclaration( IAnnotationList annotations, ITypeReference type )
	{
		this(annotations, type, null);
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
