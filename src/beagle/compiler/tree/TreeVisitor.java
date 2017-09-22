package beagle.compiler.tree;

public class TreeVisitor
{

	public void visitModule( IModule target )
	{
		for (ICompilationUnit current : target.getCompilationUnits().values())
			visitCompilationUnit(current);
	}
	
	public void visitCompilationUnit( ICompilationUnit target )
	{
		if (target.getPackage() != null)
			visitPackage(target.getPackage());
		
		for (IPackage current : target.getImportedPackages().values())
			visitImport(current);
		
		for (ITypeDeclaration current : target.getTypes().values())
			visitTypeDeclaration(current);
	}

	public void visitTypeDeclaration(ITypeDeclaration target)
	{
		visitTypeBody(target.getBody());
	}

	public void visitTypeBody(ITypeBody target)
	{
		for (IMethodDeclaration current : target.getMethods())
			visitMethodDeclaration(current);
	}

	public void visitMethodDeclaration(IMethodDeclaration target)
	{
		
	}

	public void visitImport(IPackage target)
	{
	}

	public void visitPackage(IPackage target)
	{
	}
	
}
