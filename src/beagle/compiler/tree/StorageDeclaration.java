package beagle.compiler.tree;

public abstract class StorageDeclaration extends TreeElement implements IStorageDeclaration
{

	protected IModifiers modifiers;

	protected ITypeReference type;

	protected IName name;

	protected IAnnotationList annotations;

	protected Object initializer;

	public StorageDeclaration( IAnnotationList annotations, IName name, ITypeReference type, Object initializer)
	{
		this.annotations = annotations;
		this.modifiers = null;
		this.type = type;
		this.name = name;
	}

	public StorageDeclaration( IAnnotationList annotations, ITypeReference type )
	{
		this(annotations,  null, type, null);
	}

	@Override
	public IModifiers modifiers()
	{
		return modifiers;
	}

	@Override
	public void modifiers(IModifiers value)
	{
		this.modifiers = value;
	}

	@Override
	public ITypeReference type()
	{
		return type;
	}

	@Override
	public void type(ITypeReference value)
	{
		this.type = value;
	}

	@Override
	public IName name()
	{
		return name;
	}

	@Override
	public void name(IName value)
	{
		this.name = value;
	}

	@Override
	public IAnnotationList annotations()
	{
		return annotations;
	}

}
