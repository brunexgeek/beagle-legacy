package beagle.compiler.tree;

public interface IConstantDeclaration extends ITreeElement
{

	IAnnotationList getAnnotations();

	IModifiers getModifiers();

	ITypeReference getType();

	ITypeBody getParent();

	void setParent( ITypeBody parent );

	IName getName();

}
