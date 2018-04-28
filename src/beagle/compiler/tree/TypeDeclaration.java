package beagle.compiler.tree;

public class TypeDeclaration extends TreeElement
{

	protected Package pack;

	protected boolean complete;

	protected CompilationUnit parent;

	protected AnnotationList annotations;

	protected Modifiers modifiers;

	protected Name name;

	protected TypeReferenceList extended;

	protected TypeBody body;

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
	public TypeDeclaration(CompilationUnit parent, AnnotationList annots, Modifiers modifiers, Name name,
			TypeReferenceList extended, TypeBody body)
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

		if (body != null) body.parent(this);
	}

	/**
	 * Create an incomplete type declaration
	 *
	 * @param pack
	 * @param name
	 */
	public TypeDeclaration(Package pack, Name name)
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

	public TypeDeclaration(CompilationUnit unit, Name name )
	{

	}

	public boolean isComplete()
	{
		return complete;
	}

	public void complete( boolean state )
	{
		complete = state;
	}

	public Package namespace()
	{
		return pack;
	}

	public CompilationUnit compilationUnit()
	{
		return parent;
	}

	public String qualifiedName()
	{
		return pack.qualifiedName() + "." + name;
	}

	public Name name()
	{
		return name;
	}

	public TypeBody body()
	{
		return body;
	}

	public AnnotationList annotations()
	{
		return annotations;
	}

	public TypeReferenceList extended()
	{
		return extended;
	}

	public Modifiers modifiers()
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
