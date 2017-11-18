package beagle.compiler.tree;

import java.util.List;

public interface ICompilationUnit extends ITreeElement
{

	IPackage namespace();

	void namespace( IPackage value );

	ITypeDeclarationList types();

	String fileName();

	void filename( String value );

	List<ITypeImport> imports();
}
