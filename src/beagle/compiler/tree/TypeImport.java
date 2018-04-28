package beagle.compiler.tree;

import beagle.compiler.CompilationContext;

public class TypeImport extends TreeElement
{

	Package pack;

	Name name;

	Name alias;

	/**
	 * Creates an package import.
	 *
	 * @param context
	 * @param packageName Qualified name of the package.
	 */
	public TypeImport( CompilationContext context, Name packageName)
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
	public TypeImport( CompilationContext context, Name packageName, Name typeName, Name alias)
	{
		this.pack = context.createPackage(packageName);
		this.name = typeName;
		this.alias = alias;
	}

	public Package namespace()
	{
		return pack;
	}

	public void namespace(Package value)
	{
		this.pack = value;
	}

	public String qualifiedName()
	{
		if (name != null)
			return pack.qualifiedName() + "." + name;
		else
			return pack.qualifiedName() + ".*";
	}

	public Name name()
	{
		return name;
	}

	public void name(Name value)
	{
		this.name = value;
	}

	public Name alias()
	{
		return alias;
	}

	public void alias(Name value)
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
