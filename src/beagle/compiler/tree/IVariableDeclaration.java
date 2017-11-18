package beagle.compiler.tree;

import java.util.List;

public interface IVariableDeclaration extends ITreeElement
{

	List<IAnnotation> getAnnotations();

	IModifiers getModifiers();

	ITypeReference getType();

	IName getName();

	ITypeBody getParent();

	void setParent( ITypeBody parent );

}
