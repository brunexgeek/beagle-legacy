package beagle.compiler.tree;

import java.util.List;

public interface IFieldDeclaration extends ITreeElement
{

	IModifiers getModifiers();
	
	ITypeReference getType();
	
	List<IFieldVariable> getVariables();
	
	ITypeBody getParent();
	
	void setParent( ITypeBody parent );
	
}
