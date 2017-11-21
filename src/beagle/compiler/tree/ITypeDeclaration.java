package beagle.compiler.tree;

public interface ITypeDeclaration extends ITreeElement
{

	/**
	 * Indicates whether the type is complete.
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
	public boolean complete();

	public IAnnotationList annotations();

	public IPackage namespace();

	public ICompilationUnit compilationUnit();

	public String qualifiedName();

	public IName name();

	public ITypeBody body();

	ITypeReferenceList extended();

	IModifiers modifiers();

}
