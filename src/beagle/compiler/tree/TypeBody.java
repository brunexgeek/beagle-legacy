package beagle.compiler.tree;

import java.util.LinkedList;
import java.util.List;

public class TypeBody extends TreeElement implements ITypeBody
{

	List<IConstantDeclaration> constants;

	List<IVariableDeclaration> variables;

	List<IMethodDeclaration> methods;

	ITypeDeclaration parent;

	public TypeBody()
	{
		this(null);
	}

	public TypeBody( ITypeDeclaration parent )
	{
		this.methods = new LinkedList<>();
		this.variables = new LinkedList<>();
		this.constants = new LinkedList<>();
		this.parent = parent;
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag("Body", level);
//		out.println();
//		for (IMethodDeclaration item : methods)
//			item.print(out, level + 1);
//	}

	@Override
	public List<IMethodDeclaration> getMethods()
	{
		return methods;
	}

	@Override
	public ITypeDeclaration getParent()
	{
		return parent;
	}

	@Override
	public void setParent( ITypeDeclaration parent)
	{
		this.parent = parent;
	}

	@Override
	public List<IVariableDeclaration> getVariables()
	{
		return variables;
	}

	@Override
	public List<IConstantDeclaration> getConstants()
	{
		return constants;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (IConstantDeclaration item : constants)
				item.accept(visitor);
			for (IVariableDeclaration item : variables)
				item.accept(visitor);
			for (IMethodDeclaration item : methods)
				item.accept(visitor);
		}
		visitor.finish(this);
	}


}
