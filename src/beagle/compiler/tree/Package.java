package beagle.compiler.tree;

import java.util.HashMap;
import java.util.Set;

public class Package extends TreeElement implements IPackage
{

	private IName name;

	private HashMap<String, ITypeDeclaration> types;

	public Package( IName name )
	{
		this.name = name;
		types = new HashMap<String, ITypeDeclaration>();
	}

	public Package()
	{
		this.name = null;
	}

	@Override
	public String getQualifiedName()
	{
		return name.getQualifiedName();
	}

	@Override
	public Set<String> getTypeNames()
	{
		return types.keySet();
	}

	@Override
	public ITypeDeclaration getType( String name )
	{
		return types.get(name);
	}

	public void addType( ITypeDeclaration type )
	{
		types.put(type.getQualifiedName(), type);
	}

	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "]";
	}

//	@Override
//	public void print( Printer out, int level )
//	{
//		out.printTag(getClass().getSimpleName(), level);
//		out.printAttribute("name", name.getQualifiedName());
//		out.println();
//	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public ITypeDeclaration getType(IName name)
	{
		return getType(name.getQualifiedName());
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, name);
		visitor.finish(this);
	}

}
