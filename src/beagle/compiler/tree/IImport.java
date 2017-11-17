package beagle.compiler.tree;

public interface IImport extends ITreeElement
{

	IPackage getPackage();

	ITypeDeclaration getType();

}
