package beagle.compiler.tree;

public class TypeDeclaration extends TreeElement implements ITypeDeclaration
{

	protected IPackage pack;

	protected boolean complete;

	protected ICompilationUnit parent;

	protected IAnnotationList annotations;

	protected IModifiers modifiers;

	protected IName name;

	protected ITypeReferenceList extended;

	protected ITypeBody body;


	/**
	 * Create a complete type declaration
	 *
	 * @param unit
	 * @param modifiers
	 * @param name
	 * @param extended
	 * @param implemented
	 * @param body
	 */
	public TypeDeclaration(ICompilationUnit parent, IAnnotationList annots, IModifiers modifiers, IName name,
			ITypeReferenceList extended, ITypeBody body)
	{
		if (name.count() > 1)
			throw new IllegalArgumentException("Invalid simple name");
		if (parent == null)
			throw new IllegalArgumentException("Compilation unit can not be null");

		this.parent = parent;
		this.annotations = annots;
		this.name = name;
		this.pack = parent.namespace();
		this.complete = true;
		this.modifiers = modifiers;
		this.extended = extended;
		this.body = body;

		if (body != null) body.setParent(this);
	}

	/**
	 * Create an incomplete type declaration
	 *
	 * @param pack
	 * @param name
	 */
	public TypeDeclaration(IPackage pack, IName name)
	{
		if (name.count() > 1)
			throw new IllegalArgumentException("Invalid simple name");
		if (pack == null)
			throw new IllegalArgumentException("Package can not be null");

		this.parent = null;
		this.name = name;
		this.pack = pack;
		this.complete = false;
	}

	public TypeDeclaration( ICompilationUnit unit, IName name )
	{

	}

	@Override
	public boolean complete()
	{
		return complete;
	}

	public void complete( boolean state )
	{
		complete = state;
	}

	@Override
	public IPackage namespace()
	{
		return pack;
	}

	@Override
	public ICompilationUnit compilationUnit()
	{
		return parent;
	}

	@Override
	public String qualifiedName()
	{
		return pack.getQualifiedName() + "." + name;
	}

	@Override
	public IName name()
	{
		return name;
	}

	@Override
	public ITypeBody body()
	{
		return body;
	}

	@Override
	public IAnnotationList annotations()
	{
		return annotations;
	}

	@Override
	public ITypeReferenceList extended()
	{
		return extended;
	}

	@Override
	public IModifiers modifiers()
	{
		return modifiers;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, annotations);
			accept(visitor, modifiers);
			accept(visitor, name);
			accept(visitor, extended);
			accept(visitor, body);
		}
		visitor.finish(this);
	}

}
