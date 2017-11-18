package beagle.compiler.tree;

import java.util.List;

public interface ICompilationUnit extends ITreeElement
{

	public IPackage getPackage();

	public ITypeDeclarationList getTypes();

	public String getFileName();

	/**
	 * Returns the {@link ITypeDeclaration} object representing the type identified by the given
	 * name (qualified or not).
	 *
	 * @param name
	 * @return
	 */
	public ITypeDeclaration findImportedType(String name);

	public void addImport(ITypeImport typeImport);

	public void addType( ITypeDeclaration type );

	public List<ITypeImport> getImports();
}
