package beagle.compiler.tree;

import java.io.PrintStream;

public class TypeReference implements ITypeReference
{

	private IPackage pack;
	
	private ITypeDeclaration type;
	
	private IName typeName;
	
	private IName packageName;
	
	public TypeReference( IName qualifiedName )
	{
		if (qualifiedName.isQualified())
		{
			typeName = qualifiedName.slice(qualifiedName.getCount() - 1);
			packageName = qualifiedName.slice(0, qualifiedName.getCount() - 1);
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
			return type.getName();
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
			return type.getQualifiedName();
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
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.print("]  ");
		Printer.print(out, "type", getQualifiedName());
		out.println();
	}

}
