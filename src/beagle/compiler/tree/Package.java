package beagle.compiler.tree;

import java.util.HashMap;
import java.util.Set;

public class Package extends TreeElement
{

	private Name name;

	private HashMap<String, TypeDeclaration> types;

	public Package( Name name )
	{
		this.name = name;
		types = new HashMap<String, TypeDeclaration>();
	}

	public Package()
	{
		this.name = null;
	}

	public String qualifiedName()
	{
		return name.qualifiedName();
	}

	public Set<String> typeNames()
	{
		return types.keySet();
	}

	public TypeDeclaration type( String name )
	{
		return types.get(name);
	}

	public void addType( TypeDeclaration type )
	{
		types.put(type.qualifiedName(), type);
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

	public Name name()
	{
		return name;
	}

	public TypeDeclaration type(Name name)
	{
		return type(name.qualifiedName());
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			accept(visitor, name);
		visitor.finish(this);
	}

}
