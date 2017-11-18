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
		if (name.getCount() > 1)
			throw new IllegalArgumentException("Invalid simple name");
		if (parent == null)
			throw new IllegalArgumentException("Compilation unit can not be null");

		this.parent = parent;
		this.annotations = annots;
		this.name = name;
		this.pack = parent.getPackage();
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
		if (name.getCount() > 1)
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
	public boolean isComplete()
	{
		return complete;
	}

	public void setComplete( boolean state )
	{
		complete = state;
	}

	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public ICompilationUnit getCompilationUnit()
	{
		return parent;
	}

	@Override
	public String getQualifiedName()
	{
		return pack.getQualifiedName() + "." + name;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeBody getBody()
	{
		return body;
	}

	/*@Override
	public void print(Printer out, int level)
	{
		out.printTag(getClass().getSimpleName(), level);
		out.printAttribute("name", getQualifiedName());
		out.printAttribute("isComplete", complete);
		out.println();

		if (modifiers != null)
			modifiers.print(out, level + 1);

		if (extended != null)
		{
			out.printTag("Extended", level + 1);
			out.println();
			for (ITypeReference item : extended)
				item.print(out, level + 2);
		}

		body.print(out, level + 1);
	}*/

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
			accept(visitor, extended);
			accept(visitor, body);
		}
		visitor.finish(this);
	}

}
