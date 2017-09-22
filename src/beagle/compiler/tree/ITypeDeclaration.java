package beagle.compiler.tree;

public interface ITypeDeclaration extends ITreeElement
{

	/**
	 * Indicates if the type is complete.
	 * 
	 * A type is complete if its definition is already known. This happens if
	 * its compilation unit is part of the current compilation process or if its
	 * definitions have been loaded from a module.
	 * 
	 * Incomplete types are created during the syntax tree construction when
	 * first referenced.
	 * 
	 * @return
	 */
	public boolean isComplete();

	public IPackage getPackage();
	
	public ICompilationUnit getCompilationUnit();
	
	public String getQualifiedName();
	
	public IName getName();
	
	public ITypeBody getBody();
	
}
