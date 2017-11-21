package beagle.compiler.tree;

public class TypeReference extends TreeElement implements ITypeReference
{

	private IPackage pack;

	private ITypeDeclaration type;

	private IName typeName;

	private IName packageName;

	public TypeReference( IName qualifiedName )
	{
		if (qualifiedName.isQualified())
		{
			typeName = qualifiedName.slice(qualifiedName.count() - 1);
			packageName = qualifiedName.slice(0, qualifiedName.count() - 1);
		}
		else
			typeName = qualifiedName;
	}

	public TypeReference( IName typeName, IPackage pack )
	{
		this(typeName, pack.getName());
		this.pack = pack;
	}

	public TypeReference( IName typeName, IName packageName )
	{
		this.typeName = typeName;
		this.packageName = packageName;
	}

	@Override
	public IName getName()
	{
		if (type != null)
			return type.name();
		else
			return typeName;
	}

	@Override
	public IName getPackageName()
	{
		if (pack != null)
			return pack.getName();
		else
			return packageName;
	}

	@Override
	public String getQualifiedName()
	{
		if (type != null)
			return type.qualifiedName();
		if (pack != null)
			return pack.getQualifiedName() + "." + typeName;
		if (packageName != null)
			return packageName + "." + typeName;
		return typeName.toString();
	}

	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public ITypeDeclaration getType()
	{
		return type;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			accept(visitor, typeName);
		}
		visitor.finish(this);
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag(getClass().getSimpleName(), level);
//		out.printAttribute("type", getQualifiedName());
//		out.println();
//	}

}
