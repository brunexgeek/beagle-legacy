package beagle.compiler.tree;

public abstract class StorageDeclaration extends TreeElement implements IStatement
{

	protected Modifiers modifiers;

	protected TypeReference type;

	protected Name name;

	protected AnnotationList annotations;

	protected IExpression initializer;

	public StorageDeclaration( AnnotationList annotations, Name name, TypeReference type, IExpression initializer)
	{
		this.annotations = annotations;
		this.modifiers = null;
		this.type = type;
		this.name = name;
		this.initializer = initializer;
	}

	public StorageDeclaration( AnnotationList annotations, Name name, IExpression initializer )
	{
		this(annotations,  name, null, initializer);
	}

	public StorageDeclaration( AnnotationList annotations, Name name, TypeReference type )
	{
		this(annotations,  name, type, null);
	}

	public Modifiers modifiers()
	{
		return modifiers;
	}

	public void modifiers(Modifiers value)
	{
		this.modifiers = value;
	}

	public TypeReference type()
	{
		return type;
	}

	public void type(TypeReference value)
	{
		this.type = value;
	}

	public Name name()
	{
		return name;
	}

	public void name(Name value)
	{
		this.name = value;
	}

	public AnnotationList annotations()
	{
		return annotations;
	}

	public IExpression initializer()
	{
		return initializer;
	}

	public void initializer(IExpression value)
	{
		this.initializer = value;
	}

}
