package beagle.compiler.tree;

public interface ITypeReference extends ITreeElement
{

	public IName getName();
	
	public IName getPackageName();
	
	public String getQualifiedName();
	
	public IPackage getPackage();
	
	public ITypeDeclaration getType();
	
}
