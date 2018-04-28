package beagle.compiler.tree;

import java.util.LinkedList;
import java.util.List;

public class TypeBody extends TreeElement
{

	List<ConstantDeclaration> constants;

	List<VariableDeclaration> variables;

	List<MethodDeclaration> methods;

	TypeDeclaration parent;

	public TypeBody()
	{
		this(null);
	}

	public TypeBody( TypeDeclaration parent )
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

	public List<MethodDeclaration> methods()
	{
		return methods;
	}

	@Override
	public TypeDeclaration parent()
	{
		return parent;
	}

	public void parent( TypeDeclaration parent)
	{
		this.parent = parent;
	}

	public List<VariableDeclaration> variables()
	{
		return variables;
	}

	public List<ConstantDeclaration> constants()
	{
		return constants;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (ConstantDeclaration item : constants)
				item.accept(visitor);
			for (VariableDeclaration item : variables)
				item.accept(visitor);
			for (MethodDeclaration item : methods)
				item.accept(visitor);
		}
		visitor.finish(this);
	}


}
