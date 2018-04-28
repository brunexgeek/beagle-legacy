package beagle.compiler.tree;

public class TypeReference extends TreeElement
{

	private Package pack;

	private TypeDeclaration type;

	private Name typeName;

	private Name packageName;

	public TypeReference( Name qualifiedName )
	{
		if (qualifiedName.isQualified())
		{
			typeName = qualifiedName.slice(qualifiedName.count() - 1);
			packageName = qualifiedName.slice(0, qualifiedName.count() - 1);
		}
		else
			typeName = qualifiedName;
	}

	public TypeReference( Name typeName, Package pack )
	{
		this(typeName, pack.name());
		this.pack = pack;
	}

	public TypeReference( Name typeName, Name packageName )
	{
		this.typeName = typeName;
		this.packageName = packageName;
	}


	public Name name()
	{
		if (type != null)
			return type.name();
		else
			return typeName;
	}

	public Name packageName()
	{
		if (pack != null)
			return pack.name();
		else
			return packageName;
	}

	public String qualifiedName()
	{
		if (type != null)
			return type.qualifiedName();
		if (pack != null)
			return pack.qualifiedName() + "." + typeName;
		if (packageName != null)
			return packageName + "." + typeName;
		return typeName.toString();
	}

	public Package pack()
	{
		return pack;
	}

	public TypeDeclaration type()
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
