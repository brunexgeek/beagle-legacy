package beagle.compiler.tree;

import java.util.List;

public class MethodDeclaration extends TreeElement implements IMethodDeclaration
{

	IAnnotationList annotations;

	IModifiers modifiers;

	IName name;

	List<IFormalParameter> parameters;

	ITypeReference type;

	IBlock body;

	ITypeBody parent;

	public MethodDeclaration(IAnnotationList annots, ITypeReference type, IName name, List<IFormalParameter> parameters, IBlock body)
	{
		this.annotations = annots;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.body = body;
		this.modifiers = null;
	}

	@Override
	public ITypeReference getReturnType()
	{
		return type;
	}

	@Override
	public List<IFormalParameter> getParameters()
	{
		return parameters;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public IBlock getBody()
	{
		return body;
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag(getClass().getSimpleName(), level);
//		out.printAttribute("isContructor", isContructor());
//		out.println();
//
//		if (modifiers != null)
//			modifiers.print(out, level + 1);
//		type.print(out, level + 1);
//		name.print(out, level + 1);
//
//		out.printTag("[FormalParameterList]", level + 1);
//		for (IFormalParameter param : parameters)
//			param.print(out, level + 2);
//	}

	@Override
	public boolean isContructor()
	{
		return (name.equals(type.getName()));
	}

	@Override
	public IModifiers getModifiers()
	{
		return modifiers;
	}

	@Override
	public ITypeBody getParent()
	{
		return parent;
	}

	@Override
	public void setParent(ITypeBody parent)
	{
		this.parent = parent;
	}

	@Override
	public IAnnotationList getAnnotations()
	{
		return annotations;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, annotations);
			accept(visitor, modifiers);
			accept(visitor, name);
			accept(visitor, modifiers);
			for (IFormalParameter item : parameters)
				item.accept(visitor);
			accept(visitor, type);
			accept(visitor, body);
		}
		visitor.finish(this);
	}

}
