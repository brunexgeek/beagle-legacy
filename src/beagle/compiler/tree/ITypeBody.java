package beagle.compiler.tree;

import java.util.List;

public interface ITypeBody extends ITreeElement
{

	List<IMethodDeclaration> getMethods();
	
	public List<IFieldDeclaration> getFields();
	
	ITypeDeclaration getParent();
	
	void setParent( ITypeDeclaration parent );
		
}
