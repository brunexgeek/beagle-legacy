package beagle.compiler.tree;

import java.util.List;

public interface IConstantDeclaration extends ITreeElement
{

	List<IAnnotation> getAnnotations();

	IModifiers getModifiers();

	ITypeReference getType();

	ITypeBody getParent();

	void setParent( ITypeBody parent );

	IName getName();

}
