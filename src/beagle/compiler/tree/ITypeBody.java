package beagle.compiler.tree;

import java.util.List;

public interface ITypeBody extends ITreeElement
{

	List<IMethodDeclaration> getMethods();
	
	ITypeDeclaration getParent();
	
	void setParent( ITypeDeclaration parent );
		
}
