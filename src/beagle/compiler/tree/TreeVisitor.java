package beagle.compiler.tree;

import java.util.List;

public class TreeVisitor<T>
{

	public void visitModule( IModule target, T context )
	{
		for (ICompilationUnit current : target.getCompilationUnits().values())
			visitCompilationUnit(current, context);
	}
	
	public void visitCompilationUnit( ICompilationUnit target, T context )
	{
		if (target.getPackage() != null)
			visitPackage(target.getPackage(), context);
		
		for (IPackage current : target.getImportedPackages().values())
			visitImport(current, context);
		
		for (ITypeDeclaration current : target.getTypes().values())
			visitTypeDeclaration(current, context);
	}

	public void visitTypeDeclaration(ITypeDeclaration target, T context)
	{
		visitTypeBody(target.getBody(), context);
	}

	public void visitTypeBody(ITypeBody target, T context)
	{
		for (IMethodDeclaration current : target.getMethods())
			visitMethodDeclaration(current, context);
	}

	public void visitMethodDeclaration(IMethodDeclaration target, T context)
	{
		if (target.getModifiers() != null) 
			visitModifiers(target.getModifiers(), context);
		visitReturnType(target.getReturnType(), context);
		visitName(target.getName(), context);
		visitParameters(target.getParameters(), context);
		visitMethodBody(target.getBody(), context);
	}

	public void visitMethodBody(IMethodBody body, T context)
	{
	}

	public void visitReturnType(ITypeReference returnType, T context)
	{
	}

	public void visitName(IName name, T context)
	{
	}

	public void visitParameters(List<IFormalParameter> parameters, T context)
	{
		for (IFormalParameter param : parameters)
			visitParameter(param, context);
	}

	public void visitParameter(IFormalParameter param, T context)
	{
		visitType(param.getType(), context);
		visitName(param.getName(), context);
	}

	public void visitType(ITypeReference type, T context)
	{
	}

	public void visitModifiers(IModifiers target, T context)
	{
	}

	public void visitImport(IPackage target, T context)
	{
	}

	public void visitPackage(IPackage target, T context)
	{
	}
	
}
