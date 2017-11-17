package beagle.compiler.tree;

import beagle.compiler.CompilationContext;

public class TypeImport extends TreeElement implements ITypeImport
{

	IPackage pack;

	ITypeDeclaration type;

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
		pack = context.createPackage(packageName);
		/*if (typeName != null)
			type = context.createType(typeName, pack);*/
	}

	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public String getQualifiedIdentifier()
	{
		if (type != null)
			return pack.getQualifiedName() + "." + type.getName();
		else
			return pack.getQualifiedName() + ".*";
	}

	@Override
	public ITypeDeclaration getType()
	{
		return type;
	}

	@Override
	public IName getName()
	{
		if (type != null)
			return type.getName();
		else
			return pack.getName();
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, type);
		visitor.finish(this);
	}

	/*@Override
	public void print(Printer out, int level)
	{
		out.printTag(getClass().getSimpleName(), level);

		if (type != null)
			out.printAttribute("type", type.getQualifiedName());
		else
			out.printAttribute("package", pack.getQualifiedName());
		out.println();
	}*/

}
