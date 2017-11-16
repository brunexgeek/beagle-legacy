package beagle.compiler.tree;

import java.util.List;

public interface IVariableDeclaration extends ITreeElement
{

	List<IAnnotation> getAnnotations();

	IModifiers getModifiers();

	ITypeReference getType();

	List<IFieldVariable> getVariables();

	ITypeBody getParent();

	void setParent( ITypeBody parent );

}
