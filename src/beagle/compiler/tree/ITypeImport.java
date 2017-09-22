package beagle.compiler.tree;

public interface ITypeImport extends ITreeElement
{

	public IPackage getPackage();
	
	public String getQualifiedIdentifier();
	
	public IName getName();
	
	public ITypeDeclaration getType();
	
}
