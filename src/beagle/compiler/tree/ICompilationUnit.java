package beagle.compiler.tree;

public interface ICompilationUnit extends ITreeElement
{

	IPackage namespace();

	void namespace( IPackage value );

	ITypeDeclarationList types();

	String fileName();

	void filename( String value );

	ITypeImportList imports();
}
