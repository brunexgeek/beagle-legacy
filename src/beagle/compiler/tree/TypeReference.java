package beagle.compiler.tree;

public class TypeReference extends TreeElement
{

	public static TypeReference UINT64 = new TypeReference(new Name("uint64"), true);

	public static TypeReference UINT32 = new TypeReference(new Name("uint32"), true);

	public static TypeReference UINT16 = new TypeReference(new Name("uint16"), true);

	public static TypeReference UINT8 = new TypeReference(new Name("uint8"), true);

	public static TypeReference INT64 = new TypeReference(new Name("int64"), true);

	public static TypeReference INT32 = new TypeReference(new Name("int32"), true);

	public static TypeReference INT16 = new TypeReference(new Name("int16"), true);

	public static TypeReference INT8 = new TypeReference(new Name("int8"), true);

	public static TypeReference FLOAT64 = new TypeReference(new Name("float64"), true);

	public static TypeReference FLOAT32 = new TypeReference(new Name("float32"), true);

	public static TypeReference STRING = new TypeReference(new Name("string"), true);

	public static TypeReference BOOL = new TypeReference(new Name("bool"), true);

	private Package pack;

	private TypeDeclaration type;

	private Name typeName;

	private Name packageName;

	public boolean isPrimitive = false;

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

	private TypeReference( Name typeName, boolean isPrimitive )
	{
		this.typeName= typeName;
		this.isPrimitive = isPrimitive;
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

	public static TypeReference fromName( Name name )
	{
		if (name.qualifiedName().equals("int"))
			return INT32;
		else
		if (name.qualifiedName().equals(STRING.qualifiedName()))
			return STRING;
		else
		if (name.qualifiedName().equals(BOOL.qualifiedName()))
			return BOOL;
		else
		if (name.qualifiedName().equals("float"))
			return FLOAT32;
		else
		if (name.qualifiedName().equals("double"))
			return FLOAT64;
		else
		if (name.qualifiedName().equals(UINT64.qualifiedName()))
			return UINT64;
		else
		if (name.qualifiedName().equals(UINT32.qualifiedName()))
			return UINT32;
		else
		if (name.qualifiedName().equals(UINT16.qualifiedName()))
			return UINT16;
		else
		if (name.qualifiedName().equals(UINT8.qualifiedName()))
			return UINT8;
		else
		if (name.qualifiedName().equals(INT64.qualifiedName()))
			return INT64;
		else
		if (name.qualifiedName().equals(INT32.qualifiedName()))
			return INT32;
		else
		if (name.qualifiedName().equals(INT16.qualifiedName()))
			return INT16;
		else
		if (name.qualifiedName().equals(INT8.qualifiedName()))
			return INT8;
		else
		if (name.qualifiedName().equals(FLOAT64.qualifiedName()))
			return FLOAT64;
		else
		if (name.qualifiedName().equals(FLOAT32.qualifiedName()))
			return FLOAT32;
		else
			return new TypeReference(name);
	}

}
