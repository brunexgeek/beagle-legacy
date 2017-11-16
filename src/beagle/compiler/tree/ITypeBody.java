package beagle.compiler.tree;

import java.util.List;

public interface ITypeBody extends ITreeElement
{

	List<IMethodDeclaration> getMethods();

	public List<IVariableDeclaration> getVariables();

	public List<IConstantDeclaration> getConstants();

	ITypeDeclaration getParent();

	void setParent( ITypeDeclaration parent );

}
