package beagle.compiler.tree;

import java.util.Set;

public interface IPackage extends ITreeElement
{

	public String getQualifiedName();
	
	public IName getName();
	
	public Set<String> getTypeNames();
	
	public ITypeDeclaration getType( String name );
	
	public ITypeDeclaration getType( IName name );
	
}
