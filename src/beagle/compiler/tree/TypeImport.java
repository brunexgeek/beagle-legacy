package beagle.compiler.tree;

import beagle.compiler.CompilationContext;

public class TypeImport extends TreeElement implements ITypeImport
{

	IPackage pack;

	IName name;

	/**
	 * Creates an package import.
	 *
	 * @param context
	 * @param packageName Qualified name of the package.
	 */
	public TypeImport( CompilationContext context, IName packageName)
	{
		this(context, packageName, null);
	}

	/**
	 * Creates an type import.
	 *
	 * If the imported type is unknown, an incomplete type will be created.
	 *
	 * @param context
	 * @param packageName Qualified name of the package.
	 * @param typeName Simple name of the type.
	 */
	public TypeImport( CompilationContext context, IName packageName, IName typeName)
	{
		this.pack = context.createPackage(packageName);
		this.name = typeName;
	}

	@Override
	public IPackage namespace()
	{
		return pack;
	}

	@Override
	public void namespace(IPackage value)
	{
		this.pack = value;
	}

	@Override
	public String qualifiedName()
	{
		if (name != null)
			return pack.getQualifiedName() + "." + name;
		else
			return pack.getQualifiedName() + ".*";
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
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, namespace());
			accept(visitor, name());
		}
		visitor.finish(this);
	}

}
