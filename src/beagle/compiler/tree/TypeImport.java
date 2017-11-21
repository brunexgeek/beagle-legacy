package beagle.compiler.tree;

import beagle.compiler.CompilationContext;

public class TypeImport extends TreeElement implements ITypeImport
{

	IPackage pack;

	IName name;

	IName alias;

	/**
	 * Creates an package import.
	 *
	 * @param context
	 * @param packageName Qualified name of the package.
	 */
	public TypeImport( CompilationContext context, IName packageName)
	{
		this(context, packageName, null, null);
	}

	/**
	 * Creates an type import.
	 *
	 * @param context
	 * @param packageName Qualified name of the package.
	 * @param typeName Simple name of the type.
	 * @param typeName Simple name to be used as alias.
	 */
	public TypeImport( CompilationContext context, IName packageName, IName typeName, IName alias)
	{
		this.pack = context.createPackage(packageName);
		this.name = typeName;
		this.alias = alias;
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
	public IName alias()
	{
		return alias;
	}

	@Override
	public void alias(IName value)
	{
		this.alias = value;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			accept(visitor, name);
			accept(visitor, alias);
		}
		visitor.finish(this);
	}

}
