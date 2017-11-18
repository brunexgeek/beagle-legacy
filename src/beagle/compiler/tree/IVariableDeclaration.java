package beagle.compiler.tree;

public interface IVariableDeclaration extends ITreeElement
{

	IAnnotationList getAnnotations();

	IModifiers getModifiers();

	ITypeReference getType();

	IName getName();

	ITypeBody getParent();

	void setParent( ITypeBody parent );

}
